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

/**
 * Created by Avinash Sharma on 27-Apr-17.
 */

public class Folder_Intent extends AppCompatActivity {
    String path;
    ArrayList<String>FileList;
    RecyclerView RV;
    recyclerView RView;
    int Paste;
    FileOps FO;
    Intent intent;
    Bundle extras;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intent_folder);

        RV= (RecyclerView) findViewById(R.id.RV);
         intent=getIntent();
         extras = intent.getExtras();
        path=extras.getString("path");
        Paste=extras.getInt("Paste",0);
        System.out.println(Paste);
        FO = new FileOps();
        Log.d("OC", "onCreate: "+path);
        FileList=new ArrayList<>();
        GenerateFileList();
        RView =new recyclerView();
        RV.setLayoutManager(new LinearLayoutManager(this));
        RV.setAdapter(RView);
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
                Toast.makeText(Folder_Intent.this, "You selected search", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Folder_Intent.this);
                builder1.setTitle("Search...");
                final EditText input = new EditText(Folder_Intent.this);

                input.setInputType(InputType.TYPE_CLASS_TEXT );

                builder1.setView(input);


                builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = input.getText().toString();
                        // inpText.setText(m_Text);

                       // Intent intent = new Intent(Folder_Intent.this, Folder_Intent.class);
                       // intent.putExtra("path",path);
                      //  startActivity(intent);
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
                pasteFile();
                return true;
            }


            case R.id.action_CreateDir:
            {AlertDialog.Builder builder = new AlertDialog.Builder(Folder_Intent.this);
                builder.setTitle("Create new folder");


                final EditText input1 = new EditText(Folder_Intent.this);

                input1.setInputType(InputType.TYPE_CLASS_TEXT );

                builder.setView(input1);


                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       String name = input1.getText().toString();
                       // inpText.setText(m_Text);
                        FO.CreateDir(path,name);
                        Intent intent = new Intent(Folder_Intent.this, Folder_Intent.class);
                        intent.putExtra("path",path);
                        startActivity(intent);
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


public void pasteFile()
{
    if(Paste==0)
        Toast.makeText(Folder_Intent.this, "No file to be pasted!!!", Toast.LENGTH_SHORT).show();
    else if(Paste==1) {
        String InputPath = extras.getString("InputPath");
       String InputName= extras.getString("InputName");
        FO.copyFile(InputPath,InputName,path);
    }
    else if(Paste==2) {
        String InputPath = extras.getString("InputPath");
        String InputName= extras.getString("InputName");
        FO.moveFile(InputPath,InputName,path);
    }

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



            final Bundle b = new Bundle();
            b.putString("path",path);

            File selc = new File(path + "/" + s);
            String extension = getFileExt(s);
            if(selc.isDirectory()) {
                holder.iv.setImageResource(R.mipmap.folder256);
            }
            else{
                if(extension.equals("pdf")) {
                    holder.iv.setImageResource(R.mipmap.pdf256);
                }
                else if(extension.equals("jpeg")||extension.equals("jpg")){
                    holder.iv.setImageResource(R.mipmap.jpg256);
                }
                else if(extension.equals("png")){
                    holder.iv.setImageResource(R.mipmap.png);
                }
                else if(extension.equals("docx")||extension.equals("doc")){
                    holder.iv.setImageResource(R.mipmap.docs);
                }
                else if(extension.equals("xlsx")){
                    holder.iv.setImageResource(R.mipmap.xls);
                }
                else if(extension.equals("mp4")||extension.equals("mpeg")||extension.equals("wmv")||extension.equals("3gp")||extension.equals("mkv")){
                    holder.iv.setImageResource(R.mipmap.vid);
                }
                else if(extension.equals("mp3")||extension.equals("m4a")||extension.equals("aac")||extension.equals("opus")||extension.equals("ogg")){
                    holder.iv.setImageResource(R.mipmap.music3);
                }
                else if(extension.equals("ppt")||extension.equals("pptx")){
                    holder.iv.setImageResource(R.mipmap.ppt);
                }

                else{
                    holder.iv.setImageResource(R.mipmap.file);
                }





            }



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
                               Intent chooser = Intent.createChooser(intent, " Open using");
                               if(intent.resolveActivity(getPackageManager())!=null)
                                   startActivity(chooser);
                               else
                               {
                                   Toast.makeText(Folder_Intent.this,"Cannot open", Toast.LENGTH_LONG).show();
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
                                   Toast.makeText(Folder_Intent.this, "Cannot open", Toast.LENGTH_LONG).show();
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
                                   Toast.makeText(Folder_Intent.this,"Cannot open", Toast.LENGTH_LONG).show();
                               }

                           }
                           else if(extension.equals("mp3")||extension.equals("m4a")||extension.equals("aac")||extension.equals("opus")||extension.equals("ogg"))
                           {
                               intent.setAction(Intent.ACTION_VIEW);
                               intent.setDataAndType(Uri.fromFile(sel),"audio/*");
                               // intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                               Intent chooser = Intent.createChooser(intent, " Open using");
                               if(intent.resolveActivity(getPackageManager())!=null)
                                   startActivity(chooser);
                               else
                               {
                                   Toast.makeText(Folder_Intent.this,"Cannot open", Toast.LENGTH_LONG).show();
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
                                   Toast.makeText(Folder_Intent.this,"Cannot open", Toast.LENGTH_LONG).show();
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
                                   Toast.makeText(Folder_Intent.this,"Cannot open", Toast.LENGTH_LONG).show();
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

            holder.cv.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(Folder_Intent.this,s ,Toast.LENGTH_LONG).show();
                    //final CharSequence colors[] = new CharSequence[] {"red", "green", "blue", "black"};
                    final CharSequence options[] = new CharSequence[] {"Move", "Share", "Copy", "Properties", "Delete", "Compress"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle(s);
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(Folder_Intent.this, "You Clicked : " + options[which], Toast.LENGTH_SHORT).show();
                            if(options[which].equals("Copy"))
                            {
                                b.putInt("Paste", 1);
                                String InputPath = path;
                                String InputName = s;
                                b.putString("InputPath",InputPath);
                                b.putString("InputName",InputName);
                                Log.d("tag","Copied");

                            }
                            else
                            if(options[which].equals("Delete"))
                            {
                                FO.deleteFile(path, s);
                                //Toast.makeText(Internal_Storage.this, str + " Deleted", Toast.LENGTH_SHORT).show();
                            }
                            else
                            if(options[which].equals("Move"))
                            {
                                b.putInt("Paste", 2);
                                String InputPath = path;
                                String InputName = s;
                                b.putString("InputPath",InputPath);
                                b.putString("InputName",InputName);
                                Log.d("tag","Copied");
                            }
                            else
                            if(options[which].equals("Compress"))
                            {
                              /* CompressManager CM;
                                CM.compress(sel,);*/

                            }



                        }
                    });
                    builder.show();

                    return false;
                }
            });




        }

        @Override
        public int getItemCount() {
            return FileList.size();
        }
    }

}
