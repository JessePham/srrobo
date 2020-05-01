package com.example.srrobo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.Socket;

public class GetConnection extends AppCompatActivity {
    //UI Element
    Button connect;
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
    TextView test, status;

    private static final String PORT_NUM = "21567";
    private DatabaseReference databaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_connection);

        connect = (Button) findViewById(R.id.connectBtn);

        txtAddress = (EditText) findViewById(R.id.ipAddress);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        test = (TextView) findViewById(R.id.testText);


        databaseRef = FirebaseDatabase.getInstance().getReference("ipAddress");
        test.setText("IP Address: " + sharedPreferences.getString(IP, null));



        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipText = txtAddress.getText().toString();
                if (ipText.matches("[0-9.]*")) {
                    getIPandPort();
                    databaseRef.setValue(ipText);

                } else {

                    CharSequence text = "Please input the correct IP address.";
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                }
            }
        });

    }

    public void getIPandPort() {

        String iPandPort = ipText + ":" + PORT_NUM;
        Log.d("TEST", "IP String: " + iPandPort);
        String temp[] = iPandPort.split(":");
        wifiModuleIp = temp[0];
        wifiModulePort = Integer.valueOf(temp[1]);
        Log.d("test", "IP:" + wifiModuleIp);
        Log.d("test", "PORT:" + wifiModulePort);

        //adds the IP and Port number into the shared preferences folder
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(IP, ipText);
        editor.commit();

        System.out.println(sharedPreferences.getString("IP Address:" + ipText, null));

        test.setText("IP Address: " + sharedPreferences.getString(IP, null));






    }
}





