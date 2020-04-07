package com.example.srrobo;

import android.content.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    private Button search, connect, video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = findViewById(R.id.setButton);

        /*search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchView();
            }
        });

         */

        connect = findViewById(R.id.connectButton);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGPSView();
            }
        });

        video = findViewById(R.id.videoButton);
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVideoView();
            }
        });



    }




    private void openGPSView(){
        Intent intent = new Intent(this, GetConnection.class);
        startActivity(intent);
    }

    private void openVideoView(){
        Intent intent = new Intent(this, GetLiveStream.class);
        startActivity(intent);
    }
}
