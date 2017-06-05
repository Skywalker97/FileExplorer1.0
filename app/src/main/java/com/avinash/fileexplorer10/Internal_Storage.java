package com.avinash.fileexplorer10;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import java.util.ArrayList;

import static android.os.Environment.getExternalStorageDirectory;
import static android.os.Environment.isExternalStorageRemovable;
import static com.avinash.fileexplorer10.R.id.rv;

public class Internal_Storage extends AppCompatActivity {

    ArrayList<String> FileList;
    RecyclerView RV;

    Adapter fileAdapter;
    String FilePath;
    FileOps FO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storage_internal);
        RV = (RecyclerView) findViewById(rv);
        FileList = new ArrayList<>();
        GenerateFileList();
        fileAdapter = new Adapter();
        RV.setAdapter(fileAdapter);
        RV.setLayoutManager(new LinearLayoutManager(this));
       // int s = FileList.size();
        FO = new FileOps();
       /* FO.copyFile(getExternalStorageDirectory().toString() + "/" + "Han Solo" + "/",
                "Timbaland-Way.mp3",
                getExternalStorageDirectory().toString() + "/" + "New Folder" + "/");
        */
       /* FO.moveFile(getExternalStorageDirectory().toString() + "/" + "New Folder" + "/",
                "av",
                getExternalStorageDirectory().toString() + "/" + "Han Solo" + "/" );*/

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
                Toast.makeText(Internal_Storage.this, "You selected search", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Internal_Storage.this);
                builder1.setTitle("Search...");
                final EditText input1 = new EditText(Internal_Storage.this);
                 String mtext = "";

                input1.setInputType(InputType.TYPE_CLASS_TEXT );

                builder1.setView(input1);


                builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = input1.getText().toString();
                       // mtext =  input1.getText().toString();
                        // inpText.setText(m_Text);

                        // Intent intent = new Intent(Folder_Intent.this, Folder_Intent.class);
                        // intent.putExtra("path",path);
                        //  startActivity(intent);
                        Toast.makeText(Internal_Storage.this, "You searched for " + name, Toast.LENGTH_LONG).show();
                    }
                });
                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }

                });
                builder1.show();
                return true;
            case R.id.action_paste:
            {
                //pasteFile();
                return true;
            }


            case R.id.action_CreateDir:
            {AlertDialog.Builder builder = new AlertDialog.Builder(Internal_Storage.this);
                builder.setTitle("Create new folder");


                final EditText input = new EditText(Internal_Storage.this);

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


    void GenerateFileList() {
        // String extState = Environment.getExternalStorageState();
      /*  if(!extState.equals(Environment.MEDIA_MOUNTED) || !extState.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
        {
            Log.d("Media not mounted", "Mount SD card");
        }
        else*/
        FilePath = getExternalStorageDirectory().toString();
        Log.d("GFL",FilePath);
        File sd = new File(FilePath);
        File[] sdDirList = sd.listFiles();
        for (int i = 0; i < sdDirList.length; i++) {
            FileList.add(new String(sdDirList[i].getName()));
            Log.d("GFL", "File");

        }
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

            String InputPath;
            String InputFile;
            final int Paste;
            final Bundle b = new Bundle();
            b.putString("path",FilePath+ "/" + str);

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
                        File sel = new File(FilePath + "/" + str);
                        if(sel.isDirectory()) {

                            Intent intent = new Intent(Internal_Storage.this, Folder_Intent.class);
                            intent.putExtras(b);

                            //FO.CreateDir(FilePath,"New Folder");
                            if(!isExternalStorageRemovable ())
                                Log.d("SD card", "Detected");

                         /*  File Dir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() +File.separator + "Han Solo");
                            if(!Dir.mkdirs())
                            {
                                System.out.println("Not Created");
                            }*/
                            File Dir2 = new File(getFilesDir().getAbsoluteFile() + File.separator + "Luke Skywalker");
                            if(!Dir2.mkdirs())
                            {
                                System.out.println("Not Created");
                            }
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
                                    Toast.makeText(Internal_Storage.this,"Cannot open", Toast.LENGTH_LONG).show();
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
                                    Toast.makeText(Internal_Storage.this, "Cannot open", Toast.LENGTH_LONG).show();
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
                                    Toast.makeText(Internal_Storage.this,"Cannot open", Toast.LENGTH_LONG).show();
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
                                    Toast.makeText(Internal_Storage.this,"Cannot open", Toast.LENGTH_LONG).show();
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
                                    Toast.makeText(Internal_Storage.this,"Cannot open", Toast.LENGTH_LONG).show();
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
                                    Toast.makeText(Internal_Storage.this,"Cannot open", Toast.LENGTH_LONG).show();
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

            holder.CV.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(Internal_Storage.this,str ,Toast.LENGTH_LONG).show();
                    //final CharSequence colors[] = new CharSequence[] {"red", "green", "blue", "black"};
                    final CharSequence options[] = new CharSequence[] {"Move", "Share", "Copy", "Properties", "Delete"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle(str);
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(Internal_Storage.this, "You Clicked : " + options[which], Toast.LENGTH_SHORT).show();
                            if(options[which].equals("Copy"))
                            {
                               b.putInt("Paste", 1);
                                String InputPath = FilePath;
                                String InputName = str;
                                b.putString("InputPath",InputPath);
                                b.putString("InputName",InputName);
                                Log.d("tag","Copied");

                            }
                            else
                                if(options[which].equals("Delete"))
                                {
                                    FO.deleteFile(FilePath, str);
                                    //Toast.makeText(Internal_Storage.this, str + " Deleted", Toast.LENGTH_SHORT).show();
                                }
                            else
                                if(options[which].equals("Move"))
                                {
                                    b.putInt("Paste", 2);
                                    String InputPath = FilePath;
                                    String InputName = str;
                                    b.putString("InputPath",InputPath);
                                    b.putString("InputName",InputName);
                                    Log.d("tag","Copied");
                                }



                        }
                    });
                    builder.show();

                    return false;
                }
            });


        }

        @Override
        public int getItemCount()
        {
            return FileList.size();
        }


    }




public String PasteFile()
{
    return FilePath;
}
}
