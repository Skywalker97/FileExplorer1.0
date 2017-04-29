package com.avinash.fileexplorer10;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RV = (RecyclerView) findViewById(rv);
        FileList = new ArrayList<>();
        GenerateFileList();
        fileAdapter=new Adapter();
        RV.setAdapter(fileAdapter);
        RV.setLayoutManager(new LinearLayoutManager(this));
        int s=FileList.size();
 




    }




    void GenerateFileList()
    {
       // String extState = Environment.getExternalStorageState();
      /*  if(!extState.equals(Environment.MEDIA_MOUNTED) || !extState.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
        {
            Log.d("Media not mounted", "Mount SD card");
        }
        else*/
         String FilePath = Environment.getExternalStorageDirectory().toString();
            File sd = new File(FilePath);
            File[] sdDirList = sd.listFiles();
            for (File aSdDirList : sdDirList) {
                FileList.add(new String(aSdDirList.getName()));
                Log.d("GFL", "File");

        }
    }

    private class VH extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView tv;
        CardView CV;
        private VH(View itemView) {
            super(itemView);
            iv= (ImageView) itemView.findViewById(R.id.icon1);
            tv= (TextView) itemView.findViewById(R.id.tvFname);
            CV= (CardView) itemView.findViewById(R.id.CV);
        }
    }
    public class Adapter extends RecyclerView.Adapter<VH>{

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater LI=getLayoutInflater();
            View v=LI.inflate(R.layout.list_item,parent,false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(final VH holder, int position) {
            String str= FileList.get(position);
            holder.tv.setText(str);


        }

        @Override
        public int getItemCount()
        {
            return FileList.size();
        }
    }

}
