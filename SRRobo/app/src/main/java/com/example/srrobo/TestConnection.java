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

                try {
                    testTestCommand();
                    status.setText("hi");
                } catch (JSchException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });




    }


    public void testTestCommand() throws JSchException, IOException {
        JSch jsch = new JSch();
        Session session = jsch.getSession("admin", "192.168.1.24", 21567); //maybe change port to 22
        session.setPassword("SRRobo2020");
        jsch.addIdentity("src/test/resources/id_rsa"); //idk if we need this
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();

        ChannelShell channel = (ChannelShell) session.openChannel("shell");
        PipedInputStream pis = new PipedInputStream();
        PipedOutputStream pos = new PipedOutputStream();
        channel.setInputStream(new PipedInputStream(pos));
        channel.setOutputStream(new PipedOutputStream(pis));
        channel.connect();
        pos.write("python server.py".getBytes()); //might need to change this but prob not?
        pos.flush();
        //verifyResponse(pis, "test run bob");
        status.setText("it works?");
        pis.close();
        pos.close();
        channel.disconnect();
        session.disconnect();
    }






}