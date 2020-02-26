package com.example.srrobo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.srrobo.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import androidx.appcompat.app.AppCompatActivity;

public class GetGPSLocation extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    public LatLng locationPoint;
    Button testBtn; //testButton
    TextView longlat; //longLatText
    public static String CMD = "0";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_gps_location);

        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);



        mapFrag.getMapAsync(this);

        Button testBtn = findViewById(R.id.testButton);

        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getIPandPort();
                CMD = "GetGPS";
                GetGPSLocation.Socket_AsyncTask cmd_ping_test = new Socket_AsyncTask();
                cmd_ping_test.execute();
                //code raspberry to print a text

            }
        });



    }

    public void getIPandPort()
    {
        String iPandPort = txtAddress.getText().toString();
        Log.d("MYTEST","IP String: "+ iPandPort);
        String temp[]= iPandPort.split(":");
        wifiModuleIp = temp[0];
        wifiModulePort = Integer.valueOf(temp[1]);
        Log.d("MY TEST","IP:" +wifiModuleIp);
        Log.d("MY TEST","PORT:"+wifiModulePort);
    }


    public class Socket_AsyncTask extends AsyncTask<Void,Void,Void>
    {
        Socket socket;

        @Override
        protected Void doInBackground(Void... params){
            try{
                InetAddress inetAddress = InetAddress.getByName(BluetoothConnection.wifiModuleIp);
                socket = new java.net.Socket(inetAddress,BluetoothConnection.wifiModulePort);
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeBytes(CMD);

                dataOutputStream.close();

                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                int count = dataInputStream.available(); //# of bytes

                String string ="";
                byte[] byteString = new byte[count];
                for (byte b:byteString) {

                    // convert byte into character
                    char c = (char) b;
                    string += c;
                }
                longlat.setText(string);

               // longlat = dataInputStream.read(byteString);


                socket.close();
            }catch (UnknownHostException e){e.printStackTrace();}catch (IOException e){e.printStackTrace();}

           // longlat.setText(socket.);
            //have the
            return null;
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        LatLng SJSU = new LatLng(37.337243, -121.881597);
        //TO-DO:
        // be able to get GPS location from module and replace above values
        // rename location to "Current Location"
        //send marker to module/robot
        map.addMarker(new MarkerOptions().position(SJSU).title("SJSU"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(SJSU, 19.0f));

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                map.clear();
                locationPoint = latLng;
                TextView view = findViewById(R.id.testing);
                view.setText(latLng.toString());
                map.addMarker(new MarkerOptions().position(locationPoint).title("Start Point"));

            }
        });
    }




}
