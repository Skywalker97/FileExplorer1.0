package com.avinash.fileexplorer10;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import static android.R.attr.path;
import static com.avinash.fileexplorer10.R.id.rv;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> FileList;
    RecyclerView RV;

    Adapter fileAdapter;
    String FilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RV = (RecyclerView) findViewById(rv);
        FileList = new ArrayList<>();
        GenerateFileList();
        fileAdapter = new Adapter();
        RV.setAdapter(fileAdapter);
        RV.setLayoutManager(new LinearLayoutManager(this));
        int s = FileList.size();


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
         FilePath = Environment.getExternalStorageDirectory().toString();
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

            holder.itemView.setLongClickable(true);
            holder.itemView.setClickable(true);
            holder.CV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {


                        File sel = new File(path + "/" + str);
                        if(sel.isDirectory()) {
                            Intent intent = new Intent(MainActivity.this, Folder_Intent.class);
                            intent.putExtra("path", path + "/" + holder.tv.getText());
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
                                    Toast.makeText(MainActivity.this,"Cannot open", Toast.LENGTH_LONG).show();
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

          holder.CV.setOnLongClickListener(new View.OnLongClickListener()
                                     {
                                         @Override
                                         public boolean onLongClick(View v)
                                         {
                                           Toast.makeText(MainActivity.this,"Long Click Triggered",Toast.LENGTH_LONG).show();
                                             return false;
                                         }
                                     }
            );


        }

            @Override
            public int getItemCount()
            {
                return FileList.size();
            }


    }



}
