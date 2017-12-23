package com.example.chrismagnemi.textchange;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    EditText loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//        loc = (EditText) findViewById(R.id.edittext);

//        Intent myLocalIntent = getIntent();
//        Bundle myBundle = myLocalIntent.getExtras();
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

//        mMap.setMyLocationEnabled(true);

        LatLng Boston = new LatLng(42.0, -71.0);
        mMap = googleMap;
        googleMap.addMarker(new MarkerOptions().position(Boston).title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Boston, 14));
    }

//    public void onSearch(View view) {
//        String location = loc.getText().toString();
//
//        List<android.location.Address> addressList;
//        if (location != null || !location.equals("")) {
//            Geocoder geocoder = new Geocoder(this);
//            try {
//                addressList = geocoder.getFromLocationName(location,1);
//                Address address = addressList.get(0);
//                LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
//                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
//                float zoomLevel = (float) 6.0;
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            }
//        }
    }




