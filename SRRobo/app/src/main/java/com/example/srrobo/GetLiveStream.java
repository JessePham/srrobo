package com.example.srrobo;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.se.omapi.Session;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.webkit.*;
import android.widget.Toast;
import android.net.Uri;
public class GetLiveStream extends AppCompatActivity implements OnMapReadyCallback{

    //EditText addrField;
    Button endBtn;
    WebView streamView;
    ImageButton upBtn, backBtn, leftBtn, rightBtn, pingBtn;
    public static String CMD = "0";

    //XML file to store current Ip and Port
    public static final String MyPREFERENCES = "CurrentUserIP";
    public static final String IP = "ipKey";
    SharedPreferences sharedPreferences;

    private static final String PORT_NO = "8081";
    private static String ipText = "";
    SupportMapFragment mapFragment;
    GoogleMap googleMap;
    private DatabaseReference databaseRef;



    @Override
    public void onMapReady(final GoogleMap aMap){
        googleMap = aMap;

        // TODO:
        //need to get lat lng from gps from sr robo
        //do the sockets matter or can we use the same port? or a port for the gps ?
        CMD = "GetLocation";
        Socket_AsyncTask cmd_get_location = new Socket_AsyncTask();
        cmd_get_location.execute();

        databaseRef = FirebaseDatabase.getInstance().getReference("command");


        //followCar to keep updating
        pingBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CMD = "Ping";
                System.out.println(GetConnection.wifiModuleIp);
                Socket_AsyncTask cmd_go_ping = new Socket_AsyncTask();
               // cmd_go_ping.execute();
                pingLocation(1,1);
                databaseRef.setValue(CMD);


            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_live_stream);

        //gps map stuff
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync((OnMapReadyCallback) this);
        //googleMap.

        //addrField = (EditText)findViewById(R.id.addr);
        streamView = (WebView)findViewById(R.id.webview);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //check if string has "http://" and "/" at the end just in case
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ipText =  sharedPreferences.getString(IP, null);
        if (ipText != null){
            playStream();
        } else {
            Toast.makeText(GetLiveStream.this,
                    "no ip found", Toast.LENGTH_LONG).show();
        }
        upBtn = (ImageButton)findViewById(R.id.goFowardBtn);
        upBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CMD = "Forward";
                System.out.println(GetConnection.wifiModuleIp);
                Socket_AsyncTask cmd_go_foward = new Socket_AsyncTask();
                //cmd_go_foward.execute();
                databaseRef.setValue(CMD);

            }
        });

        backBtn = (ImageButton)findViewById(R.id.goBackwardBtn);
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CMD = "Backward";
                System.out.println(GetConnection.wifiModuleIp);
                Socket_AsyncTask cmd_go_backward = new Socket_AsyncTask();
               // cmd_go_backward.execute();
                databaseRef.setValue(CMD);

            }
        });

        rightBtn = (ImageButton)findViewById(R.id.goRightBtn);
        rightBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CMD = "Right";
                System.out.println(GetConnection.wifiModuleIp);
                Socket_AsyncTask cmd_go_right = new Socket_AsyncTask();
               // cmd_go_right.execute();
                databaseRef.setValue(CMD);

            }
        });

        leftBtn = (ImageButton)findViewById(R.id.goLeftBtn);
        leftBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CMD = "Left";
                System.out.println(GetConnection.wifiModuleIp);
                Socket_AsyncTask cmd_go_left = new Socket_AsyncTask();
                //cmd_go_left.execute();
                databaseRef.setValue(CMD);

            }
        });

        endBtn = (Button)findViewById(R.id.endRunBtn);
        endBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                endRun();
                CMD = "End";
                System.out.println(GetConnection.wifiModuleIp);
                Socket_AsyncTask cmd_go_end = new Socket_AsyncTask();
                //cmd_go_end.execute();
                databaseRef.setValue(CMD);

            }
        });

        pingBtn = (ImageButton) findViewById(R.id.pingHereBtn);



    }

    private void endRun(){
        //save video to google drive
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images"); //points to images folder



    }

    private void pingLocation(double lat, double lng){
        LatLng SJSU = new LatLng(37.3352, 121.8811);
        //LatLng newPin = new LatLng(lat, lng);


        googleMap.addMarker(new MarkerOptions().position(SJSU));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(SJSU));

    }

    private void followCar(LatLng currentLocation){
        float zoomLevel = 16.0f; //This goes up to 21
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, zoomLevel));
    }


    private void playStream(){//String src){
        //Uri UriSrc = Uri.parse(src);
        //Uri aTest = Uri.parse("http://10.251.111.161:8081/");
        //Uri aTest = Uri.parse("http://www.ted.com/talks/download/video/8584/talk/761");



        //use aTest for testing the video feed
        String aTest = "http://10.251.111.161:8081/";

        String ipAndPortAddress = "http://"+ipText+":"+PORT_NO+"/";

        //change to aTest if you wanna do debugging

            streamView.loadUrl(ipAndPortAddress);
            //add code to auto add "http://" - required to run

            Toast.makeText(GetLiveStream.this,
                    "Connecting to: " + ipText,
                    Toast.LENGTH_LONG).show();

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
                InetAddress inetAddress = InetAddress.getByName(ipText);
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