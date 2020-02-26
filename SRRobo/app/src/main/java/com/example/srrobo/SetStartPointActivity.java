package com.example.srrobo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.appcompat.app.AppCompatActivity;

public class SetStartPointActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap startMap;
    public LatLng startPoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_start_point);

        SupportMapFragment startMapFrag = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.startMapView);



        startMapFrag.getMapAsync(this);

        Button btn = findViewById(R.id.setStartButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEndPointView();
            }
        });



    }

    private void openEndPointView(){
        Intent intent = new Intent(this, SetEndPointActivity.class);
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        startMap = googleMap;
        LatLng SJSU = new LatLng(37.337243, -121.881597);
        //TO-DO:
        // be able to get GPS location from module and replace above values
        // rename location to "Current Location"
        //send marker to module/robot
        startMap.addMarker(new MarkerOptions().position(SJSU).title("SJSU"));
        startMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SJSU, 19.0f));

        startMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                startMap.clear();
                startPoint = latLng;
                TextView view = findViewById(R.id.testing);
                view.setText(latLng.toString());

                startMap.addMarker(new MarkerOptions().position(startPoint).title("Start Point"));

            }
        });
    }
}
