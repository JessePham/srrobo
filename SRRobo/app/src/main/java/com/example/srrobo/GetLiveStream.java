package com.example.srrobo;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.android.gms.maps.model.MapStyleOptions;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.webkit.*;
import android.widget.Toast;

public class GetLiveStream extends AppCompatActivity implements OnMapReadyCallback{

    //EditText addrField;
    Button endBtn, autoBtn;
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
    private DatabaseReference commandDatabaseRef;
    private DatabaseReference gpsDatabaseRef;
    private DatabaseReference gpsLatDatabaseRef;
    private DatabaseReference gpsLongDatabaseRef;
    private DatabaseReference manualDatabaseRef;
    private LatLng currentLocation;

    private Circle currentPointCircle;
    private Marker currentPointMarker;
    private Polyline currentPolyline;
    ArrayList<LatLng> locationList;


    double latitude = 0.0;
    double longitude = 0.0;
    private LatLng zeroLatLng = new LatLng(0,0 );




    @Override
    public void onMapReady(final GoogleMap aMap){
        googleMap = aMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
        //what

        // TODO:
        //need to get lat lng from gps from sr robo
        //do the sockets matter or can we use the same port? or a port for the gps ?

        commandDatabaseRef = FirebaseDatabase.getInstance().getReference("command");
        gpsDatabaseRef = FirebaseDatabase.getInstance().getReference("gps");
        manualDatabaseRef = FirebaseDatabase.getInstance().getReference("manual");

        //has long and lat child as well
        gpsLatDatabaseRef = FirebaseDatabase.getInstance().getReference("gps/lat");
        gpsLongDatabaseRef = FirebaseDatabase.getInstance().getReference("gps/long");
        gpsLatDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String post = dataSnapshot.toString();
                System.out.println(post);
                latitude = (Double) dataSnapshot.getValue();
                currentLocation = new LatLng(latitude, longitude);
                if (latitude != 0.0 && longitude != 0.0) {
                    followCar(currentLocation);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());

            }
        });
        gpsLongDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String post = dataSnapshot.toString();
                System.out.println(post);
                longitude = (Double) dataSnapshot.getValue();
                currentLocation = new LatLng(latitude, longitude);
                if (latitude != 0.0 && longitude != 0.0) {
                    followCar(currentLocation);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());

            }
        });

        // longitude = -longitude;

       // currentLocation = new LatLng(latitude, longitude);

        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 4));

       // followCar(currentLocation);
        // googleMap.setOnPolylineClickListener


        //followCar to keep updating


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_live_stream);

        locationList = new ArrayList<>();

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

        //auto btn deactivation
        autoBtn = findViewById(R.id.autoBtn);
        autoBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                autoBtn.setEnabled(autoBtn.isEnabled()); //switches it back and forth
                manualDatabaseRef.setValue(autoBtn.isEnabled()); //saves to firebase for robot to read

            }
        });



        //manual control
        upBtn = (ImageButton)findViewById(R.id.goFowardBtn);
        upBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CMD = "Forward";
                commandDatabaseRef.setValue(CMD);

            }
        });

        backBtn = (ImageButton)findViewById(R.id.goBackwardBtn);
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CMD = "Backward";
                commandDatabaseRef.setValue(CMD);

            }
        });

        rightBtn = (ImageButton)findViewById(R.id.goRightBtn);
        rightBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CMD = "Right";
                commandDatabaseRef.setValue(CMD);

            }
        });

        leftBtn = (ImageButton)findViewById(R.id.goLeftBtn);
        leftBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CMD = "Left";
                commandDatabaseRef.setValue(CMD);

            }
        });

        endBtn = (Button)findViewById(R.id.endRunBtn);
        endBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                endRun();
                CMD = "End";
                commandDatabaseRef.setValue(CMD);

            }
        });
        //disables manual control if auto is disabled
        if (autoBtn.isEnabled() == false){
            upBtn.setEnabled(false);
            leftBtn.setEnabled(false);
            rightBtn.setEnabled(false);
            backBtn.setEnabled(false);
        } else {
            upBtn.setEnabled(true);
            leftBtn.setEnabled(true);
            rightBtn.setEnabled(true);
            backBtn.setEnabled(true);
        }

        pingBtn = (ImageButton) findViewById(R.id.pingHereBtn);
        pingBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //CMD = "Ping";
                pingLocation(currentLocation);

                commandDatabaseRef.setValue(CMD);


            }
        });



    }

    private void endRun(){
        //save video to google drive
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images"); //points to images folder



    }

    private void pingLocation(LatLng latlng){
        LatLng SJSU = new LatLng(37.3352, 121.8811);
        //LatLng newPin = new LatLng(lat, lng);
        System.out.println(latitude + " "+longitude);
        System.out.println("LatLng"+latlng.toString());



        googleMap.addMarker(new MarkerOptions().position(latlng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));

    }

    private void followCar(LatLng currentLocation){
        float zoomLevel = 19; //This goes up to 21
        if (this.currentPointCircle == null) {
            currentPointCircle = googleMap.addCircle(new CircleOptions()
                    .center(currentLocation)
                    .fillColor(Color.argb(50, 0, 0, 255))
                    .strokeWidth(0.0f)
                    .radius(12)

            );
        } else {
            this.currentPointCircle.setCenter(currentLocation);
        }
        if (this.currentPointMarker == null){
            currentPointMarker = googleMap.addMarker(new MarkerOptions()
                    .position(currentLocation)
                    .flat(true)
                    .anchor(0.5f, 0.5f)
                    .icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))


            );
        } else {
            this.currentPointMarker.setPosition(currentLocation);
        }
        if (currentLocation != null){
            locationList.add(currentLocation);
            addPolyline();
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, zoomLevel));
    }
    private void addPolyline() {


        if (locationList.size() == 2) {
            LatLng fromLatLng = locationList.get(0);
            LatLng toLatLng = locationList.get(1);
            this.currentPolyline = googleMap.addPolyline(new PolylineOptions()
                    .add(fromLatLng, toLatLng)
                    .width(3).color(Color.parseColor("#801B60FE")).geodesic(true));
        } else if (locationList.size() > 2) {
            LatLng toLatLng = locationList.get(locationList.size() - 1);
            List<LatLng> points = currentPolyline.getPoints();
            points.add(toLatLng);
            currentPolyline.setPoints(points);
        }
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



}
