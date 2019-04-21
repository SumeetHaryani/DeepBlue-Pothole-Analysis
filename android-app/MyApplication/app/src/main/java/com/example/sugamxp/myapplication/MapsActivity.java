package com.example.sugamxp.myapplication;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import spencerstudios.com.bungeelib.Bungee;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";
    private GoogleMap mMap;
    private DatabaseReference mDatabaseRef;
    private ArrayList<PicInfo> list;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        list = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        Log.d(TAG, "onCreate: " + auth.getUid());
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("result/"+auth.getUid());
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: Called");
                list.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {{
                    PicInfo locationInfo = d.getValue(PicInfo.class);
                    Log.d(TAG, "onDataChange: " + locationInfo.toString());
                    LatLng cod = new LatLng(locationInfo.getLatitude(),locationInfo.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(cod).title(locationInfo.getPothole_name()));
                    float zoomLevel = 13.0f; //This goes up to 21
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cod, zoomLevel));
                }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        Log.d(TAG, "onCreate: CALLED");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: CALLED");
        mMap = googleMap;
//        Log.d(TAG, "onMapReady: List Size" + String.valueOf(list.size()));
//
//        // Add a marker in Sydney and move the camera
////        for (int i=0; i<list.size();i++){
////
////        }
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bungee.zoom(this);

    }
}
