package com.example.srrobo;


import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.webkit.*;
import android.widget.Toast;
import android.widget.VideoView;
import android.net.Uri;
public class GetLiveStream extends AppCompatActivity {

    EditText addrField;
    Button btnConnect;
    WebView streamView;
    ImageButton upBtn;
    public static String CMD = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_live_stream);

        addrField = (EditText)findViewById(R.id.addr);
        streamView = (WebView)findViewById(R.id.webview);

        btnConnect = (Button)findViewById(R.id.connect);
        btnConnect.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                String s = addrField.getEditableText().toString();
                playStream(s);
            }});

        upBtn = (ImageButton)findViewById(R.id.goFowardBtn);
        upBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CMD = "Forward";
                System.out.println(GetGPSLocation.wifiModuleIp);
                Socket_AsyncTask cmd_go_foward = new Socket_AsyncTask();
                cmd_go_foward.execute();
            }
        });

    }

    private void playStream(String src){
        Uri UriSrc = Uri.parse(src);
        if(UriSrc == null){
            Toast.makeText(GetLiveStream.this,
                    "UriSrc == null", Toast.LENGTH_LONG).show();
        }else{


            //Uri aTest = Uri.parse("http://10.251.111.161:8081/");
            //Uri aTest = Uri.parse("http://www.ted.com/talks/download/video/8584/talk/761");
            String aTest = "http://10.251.111.161:8081/";
            streamView.loadUrl(aTest);
            //add code to auto add "http://" - required to run

            Toast.makeText(GetLiveStream.this,
                    "Connect: " + src,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public class Socket_AsyncTask extends AsyncTask<Void,Void,Void>
    {
        Socket socket;

        @Override
        protected Void doInBackground(Void... params){
            try{
                InetAddress inetAddress = InetAddress.getByName("10.251.111.161");
                //10.251.111.161
                socket = new java.net.Socket(inetAddress,21567);
                //21567
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeBytes(CMD);
                dataOutputStream.close();
                socket.close();
            }catch (UnknownHostException e){e.printStackTrace();}catch (IOException e){e.printStackTrace();}
            return null;
        }
    }

}