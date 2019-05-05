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
import com.example.textrecognition2.domain.FoodDatabase;
import com.example.textrecognition2.domain.FoodRepository;
import com.example.textrecognition2.domain.IngrCant;
import com.example.textrecognition2.domain.Ingredient;
import com.example.textrecognition2.domain.IngredientesPlatos;
import com.example.textrecognition2.domain.Plate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class OldPlatesActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;

    //private ArrayAdapter<Plate> arrayAdapter;

    //private ArrayList<String> arrayList;

    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plate);

        listView = findViewById(R.id.diet_list_food);

        String query;
        //query = DandelionActivity.generaURL(getIntent().getStringExtra("food")).replace('\n',' ');
        query = getIntent().getStringExtra("food");

        //Toast.makeText(getApplicationContext(), getIntent().getStringExtra("food"), Toast.LENGTH_LONG).show();

        ArrayList<IngrCant> ingredients;
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



                //FoodDatabase db = FoodDatabase.getDatabase(getApplicationContext());
                /*
                db.beginTransaction();
                db.ingredientDao().insert(new Ingredient("leche", "ml"));
                db.ingredientDao().insert(new Ingredient("huevo", "count"));
                db.plateDao().insert(new Plate("flan"));
                db.ingrPlatDao().insert(new IngredientesPlatos( db.ingredientDao().getIdByName("leche").getValue(), db.plateDao().getIdByName("flan").getValue(), 500 ));
                db.ingrPlatDao().insert(new IngredientesPlatos( db.ingredientDao().getIdByName("huevo").getValue(), db.plateDao().getIdByName("flan").getValue(), 6 ));
                //*/

                FoodRepository fr = new FoodRepository(this.getApplication());
                //ArrayList<Ingredient> aux =  new ArrayList<Ingredient>();
                //aux.add(new Ingredient("leche", "ml"));
                //aux.add(new Ingredient("huevo", "count"));
                //fr.insertIngredient(aux.get(0));
                //fr.insertIngredient(aux.get(1));
                //Plate flan = new Plate("flan");

                //fr.insertPlate(flan);
                //fr.insertIngredientsPlate(flan, aux, new int[]{200,6});


                for( String str: query.split("\n") ){
                    Plate plato = fr.getPlate(str);
                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
                    ingredients =  new ArrayList<IngrCant>();
                    if ( plato != null  ){
                    //if ( plato != null  ){
                        plato.setIngredients(fr.getIngredientsForPlate(plato));
                        plates.add(plato);
                    }
                    else {
                        try {
                            JSONObject jsonResponse = new JSONObject(QueryUtils.makeHttpRequest(new URL(cons + str.replace(' ', '+'))));
                            if (jsonResponse.getString("state").compareToIgnoreCase("Success") != 0)
                                continue;
                            //fr.insertPlate(new Plate(str));
                            JSONArray ingredientes = jsonResponse.getJSONArray("recipe");
                            ArrayList<String> nuevos = new ArrayList<String>();
                            int[] cantidades = new int[ingredientes.length()];
                            for (int j = 0; j < ingredientes.length(); j++) {
                                JSONObject act = ingredientes.getJSONObject(j);
                                String nombre = act.getString("name");
                                String unidades = act.getString("units");
                                int cantidad = act.getInt("num");
                                ingredients.add(new IngrCant(nombre, unidades, cantidad));

                            }
                            plates.add(new Plate(str, ingredients));
                        } catch (JSONException e) { e.printStackTrace(); continue; } //this.onBackPressed(); }
                    }
                }
            }
            catch (IOException e) { e.printStackTrace(); this.onBackPressed(); }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.act_plates_float_btn:
                Toast.makeText(getApplicationContext(), "Guardar platos para el menu", Toast.LENGTH_LONG).show();
            break;
        }
    }
}
