package com.example.srrobo;

import android.content.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    private Button search, connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = findViewById(R.id.setButton);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchView();
            }
        });

        connect = findViewById(R.id.connectButton);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBluetoothView();
            }
        });



    }


    private void openSearchView(){
        Intent intent = new Intent(this, SetStartPointActivity.class);
        startActivity(intent);

    }

    private void openBluetoothView(){
        Intent intent = new Intent(this, BluetoothConnection.class);
        startActivity(intent);
    }
}
