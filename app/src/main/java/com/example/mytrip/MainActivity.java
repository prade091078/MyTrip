package com.example.mytrip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static  ArrayList<String> places ;
    static  ArrayList<LatLng> locations ;
    static ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listview = (ListView) findViewById(R.id.listViewPlaces);

       places = new ArrayList<>();
       locations = new ArrayList<>();

        places.add("Add a new place");
        locations.add(new LatLng(0,0));

         arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, places);
                
        listview.setAdapter(arrayAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

             //   Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                intent.putExtra("place", i);

                startActivity(intent);

            }
        });
    }
}
