package com.example.textrecognition2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.textrecognition2.adapters.PlateArrayAdapter;
import com.example.textrecognition2.domain.Plate;

import java.util.ArrayList;
import java.util.Arrays;

public class PlatesActivity extends AppCompatActivity {

    private ListView listView;

    private ArrayAdapter<Plate> arrayAdapter;

    private ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plate);

        listView = findViewById(R.id.diet_list_food);

        Intent intent = getIntent();
        String message = intent.getStringExtra("food");
        String[] parts = message.split("\n");

        final ArrayList<String> fruits_list = new ArrayList<>(Arrays.asList(parts));
        final ArrayList<String> ingredients=  new ArrayList<>();
        ingredients.add("ingrediente 1");
        ingredients.add("ingrediente 2");
        ingredients.add("ingrediente 3");

        final ArrayList<Plate> plates = new ArrayList<>();
        for (int i=0; i<parts.length; i++){
            plates.add(new Plate("Plate " + i, ingredients));
        }


        Log.v("info", message);

        final PlateArrayAdapter arrayAdapter = new PlateArrayAdapter
                (this, android.R.layout.simple_list_item_1, plates);

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                Toast.makeText(getApplicationContext(), (String) listView.getItemAtPosition(pos) , Toast.LENGTH_LONG).show();
            }
        });*/

        listView.setAdapter(arrayAdapter);
        listView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                return false;
            }
        });
        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Plate plate = plates.get(position);
                plates.remove(position);
                arrayAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "You selected : " + plate.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
