package com.avinash.fileexplorer10;

import android.content.Intent;
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

import java.io.File;
import java.util.ArrayList;

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
            String str = FileList.get(position);
            holder.tv.setText(str);
            holder.CV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(MainActivity.this, Folder_Intent.class);
                        intent.putExtra("path", FilePath + "/" + holder.tv.getText());
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.e("EXC", e.getMessage());
                    }
                }
            });
        }

            @Override
            public int getItemCount()
            {
                return FileList.size();
            }


    }
}
