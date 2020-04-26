package com.example.mytrip;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
//import java.text.SimpleDateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                centreMapOnLocation(lastKnownLocation,"My Location");

            }
        }
    }

    public void centreMapOnLocation(Location location, String title)
    {
        LatLng myLocation = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.clear();

        if(title!="my location") {
            mMap.addMarker(new MarkerOptions().position(myLocation).title(title));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,10));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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
        mMap = googleMap;

        mMap.setOnMapLongClickListener(this);

        Intent intent=getIntent();

       if(intent.getIntExtra("placenumber",0)== 0)
        {
            // zoom in
            locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    centreMapOnLocation(location , "my location");
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };
            if(Build.VERSION.SDK_INT <23)
            {
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0,0,locationListener);
            }
            else
            {
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
                {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                    Location lastKnownLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                    centreMapOnLocation(lastKnownLocation,"My Location");
                }
                else
                {
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                }
            }
        }

     //   Toast.makeText(this, intent.getStringExtra("place"), Toast.LENGTH_SHORT).show();
else {
           Location placelocation = new Location(LocationManager.GPS_PROVIDER);
           placelocation.setLatitude(MainActivity.locations.get(intent.getIntExtra("placenumber", 0)).latitude);
           placelocation.setLongitude(MainActivity.locations.get(intent.getIntExtra("placenumber", 0)).longitude);
           centreMapOnLocation(placelocation, MainActivity.places.get(intent.getIntExtra("placenumber", 0)));
       }
        // Add a marker in Sydney and move the camera
      // LatLng sydney = new LatLng(-34, 151);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
        String address = "";

        List<Address> listaddress = null;
        try {
            listaddress = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (listaddress != null && listaddress.size() > 0) {
                if (listaddress.get(0).getThoroughfare() != null) {
                    if (listaddress.get(0).getSubThoroughfare() != null) {
                        address = listaddress.get(0).getSubThoroughfare()+" ";
                    }
                    address  +=listaddress.get(0).getThoroughfare();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(address == "")
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            address = sdf.format(new Date());
        }
        mMap.addMarker(new MarkerOptions().position(latLng).title(address));

MainActivity.places.add(address);
MainActivity.locations.add(latLng);
        MainActivity.arrayAdapter.notifyDataSetChanged();

        Toast.makeText(this, "Location_saved", Toast.LENGTH_SHORT).show();

    }
}
