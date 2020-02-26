package com.example.srrobo;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.appcompat.app.AppCompatActivity;

public class SetEndPointActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap endMap;
    public LatLng endPoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_end_point);

        SupportMapFragment startMapFrag = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.endMapView);



        startMapFrag.getMapAsync(this);



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        endMap = googleMap;
        LatLng SJSU = new LatLng(37.337243, -121.881597);
        //TO-DO:
        // be able to get GPS location from module and replace above values
        // rename location to "Current Location"
        //send marker to module/robot
        endMap.addMarker(new MarkerOptions().position(SJSU).title("SJSU"));
        endMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SJSU, 19.0f));

        endMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                endMap.clear();
                endPoint = latLng;
                TextView view = findViewById(R.id.testing);
                view.setText(latLng.toString());
                endMap.addMarker(new MarkerOptions().position(endPoint).title("Start Point"));

            }
        });
    }
}