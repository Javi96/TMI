package com.example.textrecognition2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DietActivity extends AppCompatActivity {

    private ListView listView;

    private Button button;

    private ArrayAdapter<String> arrayAdapter;

    private ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        listView = findViewById(R.id.diet_list_food);
        button = findViewById(R.id.diet_button_food);

        Intent intent = getIntent();
        String message = intent.getStringExtra("food");
        String[] parts = message.split("\n");

        final List<String> fruits_list = new ArrayList<>(Arrays.asList(parts));
        Log.v("info", message);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, fruits_list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                Toast.makeText(getApplicationContext(), (String) listView.getItemAtPosition(pos) , Toast.LENGTH_LONG).show();
            }
        });

        listView.setAdapter(arrayAdapter);
    }

}
