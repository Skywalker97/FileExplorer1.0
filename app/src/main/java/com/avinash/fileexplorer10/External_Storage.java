package com.avinash.fileexplorer10;

//import android.app.FragmentManager;
//import android.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.isExternalStorageRemovable;


//import android.app.FragmentTransaction;

public class External_Storage extends AppCompatActivity {
    ArrayList<String> FileList;
    RecyclerView RV;

    Adapter fileAdapter;
    String FilePath;
    FileOps FO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storage_external);
        RV = (RecyclerView) findViewById(R.id.rv1);
        FileList = new ArrayList<>();
        String[] Flist = GenerateFileList();
        for(int i=0;i<Flist.length;i++)
            FileList.add(Flist[i]);

        fileAdapter = new Adapter();
        RV.setLayoutManager(new LinearLayoutManager(this));
        RV.setAdapter(fileAdapter);
        int s = FileList.size();
        // FO = new FileOps();
       /* FO.copyFile(getExternalStorageDirectory().toString() + "/" + "Han Solo" + "/",
                "Timbaland-Way.mp3",
                getExternalStorageDirectory().toString() + "/" + "New Folder" + "/");
        */
     /*   FO.moveFile(getExternalStorageDirectory().toString() + "/" + "New Folder" + "/",
                "av",
                getExternalStorageDirectory().toString() + "/" + "Han Solo" + "/" );
*/
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions, menu);


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                Toast.makeText(External_Storage.this, "You selected search", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_paste:
            {
                //pasteFile();
                return true;
            }


            case R.id.action_CreateDir:
            {AlertDialog.Builder builder = new AlertDialog.Builder(External_Storage.this);
                builder.setTitle("Create new folder");


                final EditText input = new EditText(External_Storage.this);

                input.setInputType(InputType.TYPE_CLASS_TEXT );

                builder.setView(input);


                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = input.getText().toString();
                        // inpText.setText(m_Text);
                        FO.CreateDir(FilePath,name);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();


                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }





    public static String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    }


 /*   void GenerateFileList() {
        // String extState = Environment.getExternalStorageState();
      /*  if(!extState.equals(Environment.MEDIA_MOUNTED) || !extState.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
        {
            Log.d("Media not mounted", "Mount SD card");
        }
        else
         FilePath = ().toString();
        Log.d("GFL",FilePath);
        File sd = new File(FilePath);

        File[] sdDirList = sd.listFiles();

        for (int i = 0; i < sdDirList.length; i++) {
            FileList.add(new String(sdDirList[i].getName()));
            Log.d("GFL", "File");

        }
    }*/

    /* returns external storage paths (directory of external memory card) as array of Strings */
    public String[] GenerateFileList() {

        List<String> results = new ArrayList<>();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //Method 1 for KitKat & above
            File[] externalDirs = getExternalFilesDirs(null);

            for (File file : externalDirs) {
                String path = file.getPath().split("/Android")[0];

                boolean addPath = false;

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    addPath = Environment.isExternalStorageRemovable(file);
                }
                else{
                    addPath = Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(file));
                }

                if(addPath){
                    results.add(path);
                }
            }
        }

        if(results.isEmpty()) { //Method 2 for all versions
            // better variation of: http://stackoverflow.com/a/40123073/5002496
            String output = "";
            try {
                final Process process = new ProcessBuilder().command("mount | grep /dev/block/vold")
                        .redirectErrorStream(true).start();
                process.waitFor();
                final InputStream is = process.getInputStream();
                final byte[] buffer = new byte[1024];
                while (is.read(buffer) != -1) {
                    output = output + new String(buffer);
                }
                is.close();
            } catch (final Exception e) {
                e.printStackTrace();
            }
            if(!output.trim().isEmpty()) {
                String devicePoints[] = output.split("\n");
                for(String voldPoint: devicePoints) {
                    results.add(voldPoint.split(" ")[2]);
                }
            }
        }

        //Below few lines is to remove paths which may not be external memory card, like OTG (feel free to comment them out)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < results.size(); i++) {
                if (!results.get(i).toLowerCase().matches(".*[0-9a-f]{4}[-][0-9a-f]{4}")) {
                    Log.d("Not SD", results.get(i) + " might not be extSDcard");
                    results.remove(i--);
                }
            }
        } else {
            for (int i = 0; i < results.size(); i++) {
                if (!results.get(i).toLowerCase().contains("ext") && !results.get(i).toLowerCase().contains("sdcard")) {
                    Log.d("Not SD", results.get(i)+" might not be extSDcard");
                    results.remove(i--);
                }
            }
        }

        String[] storageDirectories = new String[results.size()];
        for(int i=0; i<results.size(); ++i) storageDirectories[i] = results.get(i);
        FilePath = storageDirectories[0];
        return storageDirectories;
    }

    private class VH extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tv;
        CardView CV;

        private VH(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.icon1);
            tv = (TextView) itemView.findViewById(R.id.tvFname);
            CV = (CardView) itemView.findViewById(R.id.CV);
        }
    }

    public class Adapter extends RecyclerView.Adapter<VH> {



        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater LI = getLayoutInflater();
            View v = LI.inflate(R.layout.list_item, parent, false);
            return new VH(v);
        }


        @Override
        public void onBindViewHolder(final VH holder, int position) {
            final String str = FileList.get(position);
            holder.tv.setText(str);

            File selc = new File(FilePath + "/" + str);
            String extension = getFileExt(str);
            if(selc.isDirectory()) {
                holder.iv.setImageResource(R.mipmap.folder256);
            }
            else {
                if (extension.equals("pdf")) {
                    holder.iv.setImageResource(R.mipmap.pdf256);
                } else if (extension.equals("jpeg") || extension.equals("jpg")) {
                    holder.iv.setImageResource(R.mipmap.jpg256);
                } else if (extension.equals("png")) {
                    holder.iv.setImageResource(R.mipmap.png);
                } else if (extension.equals("docx") || extension.equals("doc")) {
                    holder.iv.setImageResource(R.mipmap.docs);
                } else if (extension.equals("xlsx")) {
                    holder.iv.setImageResource(R.mipmap.xls);
                } else if (extension.equals("mp4") || extension.equals("mpeg") || extension.equals("wmv") || extension.equals("3gp") || extension.equals("mkv")) {
                    holder.iv.setImageResource(R.mipmap.vid);
                } else if (extension.equals("mp3") || extension.equals("ogg")) {
                    holder.iv.setImageResource(R.mipmap.music3);
                } else if (extension.equals("ppt") || extension.equals("pptx")) {
                    holder.iv.setImageResource(R.mipmap.ppt);
                } else {
                    holder.iv.setImageResource(R.mipmap.file);
                }
            }

            holder.itemView.setLongClickable(true);
            holder.itemView.setClickable(true);
            holder.CV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        File sel = new File(FilePath );
                        if(sel.isDirectory()) {
                            Intent intent = new Intent(External_Storage.this, Folder_Intent.class);
                            intent.putExtra("path", FilePath );
                            // FO.CreateDir(FilePath,"New Folder");
                            if(!isExternalStorageRemovable ())
                                Log.d("SD card", "Detected");

                         /*  File Dir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() +File.separator + "Han Solo");
                            if(!Dir.mkdirs())
                            {
                                System.out.println("Not Created");
                            }*/
                       /*  File Dir2 = new File(getFilesDir().getAbsoluteFile() + File.separator + "Luke Skywalker");
                            if(!Dir2.mkdirs())
                            {
                                System.out.println("Not Created");
                            }*/
                            startActivity(intent);
                        }
                        else {
                            Intent intent = new Intent();
                            String extension = getFileExt(str);
                            //Add support for more file extensions
                            if(extension.equals("pdf"))
                            {
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(sel),"application/pdf");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                Intent chooser =Intent.createChooser(intent, " Open using");
                                if(intent.resolveActivity(getPackageManager())!=null)
                                    startActivity(chooser);
                                else
                                {
                                    Toast.makeText(External_Storage.this,"Cannot open", Toast.LENGTH_LONG).show();
                                }
                            }
                            else if(extension.equals("txt")) {
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(sel), "text/*");
                                // intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                Intent chooser = Intent.createChooser(intent, " Open using");
                                if (intent.resolveActivity(getPackageManager()) != null)
                                    startActivity(chooser);
                                else {
                                    Toast.makeText(External_Storage.this, "Cannot open", Toast.LENGTH_LONG).show();
                                }
                            }


                            else if(extension.equals("png")|| extension.equals("jpg") || extension.equals("jpeg"))
                            {
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(sel),"image/*");
                                // intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                Intent chooser = Intent.createChooser(intent, " Open using");
                                if(intent.resolveActivity(getPackageManager())!=null)
                                    startActivity(chooser);
                                else
                                {
                                    Toast.makeText(External_Storage.this,"Cannot open", Toast.LENGTH_LONG).show();
                                }

                            }
                            else if(extension.equals("mp4")||extension.equals("mpeg")||extension.equals("wmv")||extension.equals("3gp")||extension.equals("mkv"))
                            {
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(sel),"video/*");
                                // intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                Intent chooser = Intent.createChooser(intent, " Open using");
                                if(intent.resolveActivity(getPackageManager())!=null)
                                    startActivity(chooser);
                                else
                                {
                                    Toast.makeText(External_Storage.this,"Cannot open", Toast.LENGTH_LONG).show();
                                }

                            }
                            else if(extension.equals("pptx")||extension.equals("ppt")||extension.equals("docx")||extension.equals("doc")||extension.equals("xlsx"))
                            {
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(sel),"application/*");
                                // intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                Intent chooser = Intent.createChooser(intent, " Open using");
                                if(intent.resolveActivity(getPackageManager())!=null)
                                    startActivity(chooser);
                                else
                                {
                                    Toast.makeText(External_Storage.this,"Cannot open", Toast.LENGTH_LONG).show();
                                }

                            }
                            else{
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(sel),"application/*");
                                // intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                Intent chooser = Intent.createChooser(intent, " Open using");
                                if(intent.resolveActivity(getPackageManager())!=null)
                                    startActivity(chooser);
                                else
                                {
                                    Toast.makeText(External_Storage.this,"Cannot open", Toast.LENGTH_LONG).show();
                                }
                            }


                        }


                    }
                    catch (Exception e)
                    {
                        Log.e("EXC",e.getMessage());
                    }
                }
            });

        /*  holder.CV.setOnLongClickListener(new View.OnLongClickListener()
                                     {
                                         @Override
                                         public boolean onLongClick(View v)
                                         {
                                           Toast.makeText(MainActivity.this,"Long Click Triggered",Toast.LENGTH_LONG).show();
                                             return false;
                                         }
                                     }
            );
*/

        }

        @Override
        public int getItemCount()
        {
            return FileList.size();
        }


    }



}
