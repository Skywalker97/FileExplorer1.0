package com.avinash.fileexplorer10;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

/**
 * Created by Avinash Sharma on 27-Apr-17.
 */

public class Folder_Intent extends AppCompatActivity {
    String path;
    ArrayList<String>FileList;
    RecyclerView RV;
    recyclerView RView;






    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intent_folder);

        RV= (RecyclerView) findViewById(R.id.RV);
        Intent intent=getIntent();
        path=intent.getStringExtra("path");
        Log.d("OC", "onCreate: "+path);
        FileList=new ArrayList<>();
        GenerateFileList();
        RView =new recyclerView();
        RV.setLayoutManager(new LinearLayoutManager(this));
        RV.setAdapter(RView);
    }





    public static String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    }


    void GenerateFileList()
    {
        Log.d("GenerateFileList", path);
        File sd = new File(path);
        File[] sdDirList = sd.listFiles();
        for (int i = 0; i < sdDirList.length; i++) {
            FileList.add(new String(sdDirList[i].getName()));
            Log.d("GFL", "File");

        }
    }

    public class VH extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView tv;
        CardView cv;
        public VH(View itemView) {
            super(itemView);
            iv= (ImageView) itemView.findViewById(R.id.icon1);
            tv= (TextView) itemView.findViewById(R.id.tvFname);
            cv= (CardView) itemView.findViewById(R.id.CV);
        }
    }
    public class recyclerView extends RecyclerView.Adapter<VH>{


        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater=getLayoutInflater();
            View v=inflater.inflate(R.layout.list_item,parent,false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(final VH holder, int position) {
            final String s=FileList.get(position);
            holder.tv.setText(s);
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {


                        File sel = new File(path + "/" + s);
                       if(sel.isDirectory()) {
                           Intent intent = new Intent(Folder_Intent.this, Folder_Intent.class);
                           intent.putExtra("path", path + "/" + holder.tv.getText());
                           startActivity(intent);
                       }
                       else {
                           Intent intent = new Intent();
                           String extension = getFileExt(s);
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
                                   Toast.makeText(Folder_Intent.this,"Cannot open", Toast.LENGTH_LONG).show();
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
        }

        @Override
        public int getItemCount() {
            return FileList.size();
        }
    }

}
