package com.example.srrobo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class GetConnection extends AppCompatActivity {
    //UI Element
    Button btnUp;
    Button btnDown;
    EditText txtAddress;
    Socket myAppSocket = null;
    public static String wifiModuleIp = "";
    public static int wifiModulePort = 0;
    public static String CMD = "0";
    private static String ipText = "";

    //XML file to store current Ip and Port
    public static final String MyPREFERENCES = "CurrentUserIP";
    public static final String IP = "ipKey";
    SharedPreferences sharedPreferences;

    private static final String PORT_NO = "21567";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_connection);

        btnUp = (Button) findViewById(R.id.btnUp);

        txtAddress = (EditText) findViewById(R.id.ipAddress);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipText = txtAddress.getText().toString();
                if (ipText.matches("[0-9]+")){
                    getIPandPort();

                    CMD = "GetGPS";
                    Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                    cmd_increase_servo.execute();

                } else {

                    CharSequence text = "Please input the correct IP address.";
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                }
            }
        });

    }
    public void getIPandPort()
    {

        String iPandPort = ipText +":"+PORT_NO;
        Log.d("MYTEST","IP String: "+ iPandPort);
        String temp[]= iPandPort.split(":");
        wifiModuleIp = temp[0];
        wifiModulePort = Integer.valueOf(temp[1]);
        Log.d("MY TEST","IP:" +wifiModuleIp);
        Log.d("MY TEST","PORT:"+wifiModulePort);


        //adds the IP and Port number into the shared preferences folder
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(IP, ipText);
        editor.commit();

    }
    public class Socket_AsyncTask extends AsyncTask<Void,Void,Void>
    {
        Socket socket;

        @Override
        protected Void doInBackground(Void... params){
            try{
                InetAddress inetAddress = InetAddress.getByName(GetConnection.wifiModuleIp);
                socket = new java.net.Socket(inetAddress, GetConnection.wifiModulePort);
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeBytes(CMD);
                dataOutputStream.close();
                socket.close();
            }catch (UnknownHostException e){e.printStackTrace();}catch (IOException e){e.printStackTrace();}
            return null;
        }
    }
}