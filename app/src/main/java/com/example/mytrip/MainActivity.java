package com.example.mytrip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
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
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.mytrip", Context.MODE_PRIVATE);
        ArrayList<String> latitude = new ArrayList<>();
        ArrayList<String> longitude = new ArrayList<>();
        places = new ArrayList<>();
        locations = new ArrayList<>();

        places.clear();
        latitude.clear();
        longitude.clear();
        locations.clear();

       /* try{

            places = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("places",ObjectSerializer.serialize(new ArrayList<String>())));
            latitude = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("latitude",ObjectSerializer.serialize(new ArrayList<String>())));
            longitude = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("longitude",ObjectSerializer.serialize(new ArrayList<String>())));

        } catch (
                IOException e) {
            e.printStackTrace();
        }

        if(places.size() > 0 && latitude.size() == longitude.size())
        {
            if(places.size() == latitude.size() && latitude.size() == longitude.size())
            {
                for (int i=0;i < latitude.size();i++)
                {
                    locations.add(new LatLng(Double.parseDouble(latitude.get(i)),Double.parseDouble(longitude.get(i))));
                }
            }
        }
        else
        {*/
            places.add("Add a new place");
            locations.add(new LatLng(0,0));



        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, places);
                
        listview.setAdapter(arrayAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

             //   Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                intent.putExtra("placenumber", i);

                startActivity(intent);

            }
        });
    }
}
