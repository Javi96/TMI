package com.example.textrecognition2.test;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
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
import android.widget.Toast;

import com.example.textrecognition2.DietActivity;
import com.example.textrecognition2.EditPlateActivity;
import com.example.textrecognition2.MenuActivity;
import com.example.textrecognition2.R;
import com.example.textrecognition2.adapters.PlateArrayAdapter;
import com.example.textrecognition2.domain.FoodRepository;
import com.example.textrecognition2.domain.IngrCant;
import com.example.textrecognition2.domain.Plate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPlatesActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private FloatingActionButton floatingActionButton;

    private ArrayAdapter<Plate> arrayAdapter;

    private ArrayList<String> arrayList;

    ArrayList<Plate> plates = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plate);

        listView = findViewById(R.id.diet_list_food);
        floatingActionButton = findViewById(R.id.act_plates_float_btn);

        plates = new ArrayList<>();



        Intent intent = getIntent();
        String message = intent.getStringExtra("food");

        String[] parts = message.split("\n");

        SharedPreferences sp = getSharedPreferences("menus", Context.MODE_PRIVATE);

        /*
        ****************************************************************
        Map<String , String > mapa = (Map<String , String >) sp.getAll();
        for( Map.Entry<String, String> act : mapa.entrySet() ) {
            act.getKey();
            act.getValue();
        ****************************************************************
        }
         */

        if( sp.getBoolean("TestPlatesFirstCall", false) ) {
            StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

            FoodRepository fr = new FoodRepository(this.getApplication());


            for (int i = 0; i < parts.length; i++) {
                //plates.add(new Plate("Plate " + i, ingredients));
                Plate aux = fr.getPlate(parts[i]);
                if (aux != null)
                    plates.add(aux);
            }

            StrictMode.setThreadPolicy(old);

            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("TestPlatesFirstCall", false);
            editor.apply();

            Log.v("info", message);
        }


        arrayAdapter = new PlateArrayAdapter
                (this, android.R.layout.simple_list_item_1, plates);
        arrayAdapter.setNotifyOnChange(true);

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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Plate plate = plates.get(position);
                plates.remove(position);
                arrayAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "REMOVED: long : " + plate.toString(), Toast.LENGTH_LONG).show();
                return true;
            }
        });
        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Plate actual = plates.get(position);
                //plates.remove(position);
                //arrayAdapter.notifyDataSetChanged();
                //Toast.makeText(getApplicationContext(), "sort : " + plate.toString(), Toast.LENGTH_LONG).show();

                /*
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                Intent intent = new Intent(getApplicationContext(), EditPlateActivity.class);
                intent.putExtra("plate", plate.toString());
                intent.putExtra("pos", String.valueOf(position));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                */
                Intent intent = new Intent(getApplicationContext(), EditPlateActivity.class);
                StringBuilder stringBuilder = new StringBuilder();

                for(Plate plate: plates){
                    stringBuilder.append(plate.toString() + "-");
                }

                intent.putExtra("plates", stringBuilder.toString());
                intent.putExtra("plate", actual.toString());
                intent.putExtra("pos", String.valueOf(position));
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        floatingActionButton.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.act_plates_float_btn:
                //Toast.makeText(getApplicationContext(), "Guardar platos para el menu", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);

                StringBuilder stringBuilder = new StringBuilder();

                for(Plate plate: plates){
                    stringBuilder.append(plate.toString() + "-");
                }

                intent.putExtra("plates", stringBuilder.toString());
                startActivityForResult(intent, 2);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
        }
    }




    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

        if( requestCode == 1){

            //Toast.makeText(getApplicationContext(), resultCode , Toast.LENGTH_LONG).show();
            if(resultCode == RESULT_OK) {

                String text = data.getStringExtra("plates");
                String plato = data.getStringExtra("plate");
                int pos = Integer.parseInt(data.getStringExtra("pos"));
                Toast.makeText(getApplicationContext(), text , Toast.LENGTH_LONG).show();
                plates.clear();
                arrayAdapter.notifyDataSetChanged();
                String[] platos = text.split("-");

                for(int i=0; i<platos.length; i++){
                    String[] ingredients;
                    if(i == pos){
                        ingredients = plato.split("_");
                    }
                    else {
                        ingredients = platos[i].split("_");
                    }
                    ArrayList<IngrCant> ingredientsPlate = new ArrayList<>();
                    for(int j=1; j<ingredients.length; j+=3) {
                        ingredientsPlate.add(new IngrCant(ingredients[j],Integer.parseInt(ingredients[j+1]),ingredients[j+2]));
                    }
                    plates.add(new Plate(ingredients[0], ingredientsPlate));
                    arrayAdapter.notifyDataSetChanged();

                }

                /*for(int i=0; i<9; i++){
                    ArrayList<String> d = new ArrayList<String>();
                    d.add("hola");
                    d.add("como");
                    plates.add(new Plate("Plate " + i, d));
                    arrayAdapter.notifyDataSetChanged();
                }*/


            }
        }else if(requestCode==2){

            //Toast.makeText(getApplicationContext(), resultCode , Toast.LENGTH_LONG).show();
            if(resultCode == RESULT_OK) {

                String text = data.getStringExtra("plates");
                Toast.makeText(getApplicationContext(), text , Toast.LENGTH_LONG).show();
                plates.clear();
                arrayAdapter.notifyDataSetChanged();
                String[] platos = text.split("-");

                for(int i=0; i<platos.length; i++){
                    String[] ingredients;
                    ingredients = platos[i].split("_");
                    ArrayList<IngrCant> ingredientsPlate = new ArrayList<>();
                    for(int j=1; j<ingredients.length; j+=3) {
                        ingredientsPlate.add(new IngrCant(ingredients[j],Integer.parseInt(ingredients[j+1]),ingredients[j+2]));
                    }
                    plates.add(new Plate(ingredients[0], ingredientsPlate));
                    arrayAdapter.notifyDataSetChanged();

                }

                /*for(int i=0; i<9; i++){
                    ArrayList<String> d = new ArrayList<String>();
                    d.add("hola");
                    d.add("como");
                    plates.add(new Plate("Plate " + i, d));
                    arrayAdapter.notifyDataSetChanged();
                }*/


            }
        }
    }
}