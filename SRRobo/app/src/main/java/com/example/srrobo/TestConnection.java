package com.example.srrobo;
import com.jcraft.jsch.*;
import java.io.*;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.Socket;


public class TestConnection extends AppCompatActivity {


    EditText uuid;
    Socket myAppSocket = null;
    public static String wifiModuleIp = "";
    public static int wifiModulePort = 0;
    public static String CMD = "0";
    Button testBtn;
    TextView status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_connection);
       // connectionStatus = findViewById(R.id.statusText);
        testBtn = findViewById(R.id.testButton);
        uuid = findViewById(R.id.uuidText);
        status = findViewById(R.id.statusText);

        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




    }






}