package com.example.textrecognition2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.textrecognition2.adapters.IngredientArrayAdapter;
import com.example.textrecognition2.adapters.PlateArrayAdapter;
import com.example.textrecognition2.domain.Ingredient;
import com.example.textrecognition2.domain.Plate;

import java.util.ArrayList;
import java.util.Arrays;

public class IngredientsActivity extends AppCompatActivity {

    private ListView listView;

    private ArrayAdapter<Ingredient> arrayAdapter;

    private ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        final TextView textView1 = (TextView) findViewById(R.id.cmp_tile_title);
        textView1.setText("Shopping\ncart");

        listView = findViewById(R.id.ingredient_list);

        final ArrayList<Ingredient> ingredients = new ArrayList<>();
        for (int i=0; i<15; i++){
            ingredients.add(new Ingredient("ingrediente " + i, ""));
        }

        final IngredientArrayAdapter arrayAdapter = new IngredientArrayAdapter
                (this, android.R.layout.simple_list_item_1, ingredients);

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
                Ingredient plate = ingredients.get(position);
                ingredients.remove(position);
                arrayAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "You selected : " + plate.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}

