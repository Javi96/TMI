package com.example.textrecognition2;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.textrecognition2.adapters.PlateArrayAdapter;
import com.example.textrecognition2.domain.Plate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class PlatesActivity extends AppCompatActivity {

    private ListView listView;

    //private ArrayAdapter<Plate> arrayAdapter;

    //private ArrayList<String> arrayList;

    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plate);

        listView = findViewById(R.id.diet_list_food);

        String query = DandelionActivity.generaURL(getIntent().getStringExtra("food")); //.replace('\n',' '));

        //Toast.makeText(getApplicationContext(), getIntent().getStringExtra("food"), Toast.LENGTH_LONG).show();

        ArrayList<String> ingredients;
        //ingredients.add("ingrediente 1");
        //ingredients.add("ingrediente 2");
        //ingredients.add("ingrediente 3");

        final ArrayList<Plate> plates = new ArrayList<>();

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT >= 8) {
            /*StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                   .permitAll().build();
                   StrictMode.setThreadPolicy(policy);*/
            StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

            //ArrayList<String> respuesta = QueryUtils.fetchEarthquakeData(query);
            String respuesta;
            try {
                String cons = "http://10.1.1.112:5555/recipe/";
                /*
                respuesta = QueryUtils.makeHttpRequest( new URL(query));
                JSONObject json = new JSONObject(respuesta);

                JSONArray array = json.getJSONArray("annotations");
                //Toast.makeText(getApplicationContext(), "Tiene  : " + array.length() + " platos", Toast.LENGTH_LONG).show();

                //Toast.makeText(getApplicationContext(), "La respuesta ha sido  : " + respuesta, Toast.LENGTH_LONG).show();

                for ( int i = 0; i<array.length(); i++  ) {
                    respuesta = QueryUtils.makeHttpRequest( new URL(cons+array.getJSONObject(i).getString("title") ));
                    JSONObject jsonResponse = new JSONObject(respuesta);
                    if ( jsonResponse.getString("state").compareToIgnoreCase("Success") != 0 )
                        continue;
                    JSONArray ingredientes = jsonResponse.getJSONArray("recipe");
                    ingredients =  new ArrayList<String>();
                    for ( int j = 0; j<ingredientes.length(); j++  ) {
                        ingredients.add(ingredientes.getJSONObject(j).getString("name"));

                    }
                    plates.add(new Plate( array.getJSONObject(i).getString("title") , ingredients));
                    //Toast.makeText(getApplicationContext(), "Plato : " + array.getJSONObject(i).getString("title"), Toast.LENGTH_LONG).show();
                }
                */
                for( String str: query.split("\n")){
                    JSONObject jsonResponse = new JSONObject(QueryUtils.makeHttpRequest( new URL(cons+str.replace(' ', '+') )));
                    if ( jsonResponse.getString("state").compareToIgnoreCase("Success") != 0 )
                        continue;
                    JSONArray ingredientes = jsonResponse.getJSONArray("recipe");
                    ingredients =  new ArrayList<String>();
                    for ( int j = 0; j<ingredientes.length(); j++  ) {
                        ingredients.add(ingredientes.getJSONObject(j).getString("name"));

                    }
                    plates.add(new Plate( str , ingredients));
                }
            }
            catch (IOException e) { e.printStackTrace(); this.onBackPressed(); }
            catch (JSONException e) { e.printStackTrace(); this.onBackPressed(); }

            //Toast.makeText(getApplicationContext(), "La longitud de respuesta es : " + respuesta.size(), Toast.LENGTH_LONG).show();



            StrictMode.setThreadPolicy(old);
        }




        //Log.v("info", message);

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
                /*
                plates.remove(position);
                arrayAdapter.notifyDataSetChanged();
                */
                Toast.makeText(getApplicationContext(), "You selected : " + plate.toString(), Toast.LENGTH_LONG).show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Plate plate = plates.get(position);
                plates.remove(position);
                arrayAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "You erased : " + plate.toString(), Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
