package com.avinash.fileexplorer10;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

public class MainActivity extends AppCompatActivity {
CardView CV1, CV2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CV1 = (CardView) findViewById(R.id.CV);
        CV2 = (CardView) findViewById(R.id.CV2);

        CV1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this,Internal_Storage.class);
                startActivity(intent);
            }
        });
        CV2.setOnClickListener(new View.OnClickListener()
                               {
                                   @Override
                                   public void onClick(View v) {
                                       Intent intent =new Intent(MainActivity.this,External_Storage.class);
                                       startActivity(intent);
                                   }
                               }

        );

    }







}
