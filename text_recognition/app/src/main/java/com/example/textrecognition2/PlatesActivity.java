package com.example.textrecognition2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.example.textrecognition2.adapters.PlateArrayAdapter;
import com.example.textrecognition2.domain.FoodRepository;
import com.example.textrecognition2.domain.Plate;
import com.example.textrecognition2.utilities.EncodeDecodeUtil;
import java.util.ArrayList;

/**
 * <h1>Esta actividad muestra la informacion de los platos reconocidos por el reconocedor de textp</h1>
 */
public class PlatesActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Atributos privados
     */
    private ArrayAdapter<Plate> arrayAdapter;

    private ArrayList<String> arrayList;

    private ArrayList<Plate> plates = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plate);

        ListView listView = findViewById(R.id.diet_list_food);
        FloatingActionButton floatingActionButton = findViewById(R.id.act_plates_float_btn);

        plates = new ArrayList<>();

        // tomamos los datos enviados de la anterior vista (los platos)
        Intent intent = getIntent();
        String message = intent.getStringExtra("food");
        String[] parts = message.split("\n");

        /*
        Accedemos al almacenamiento interno y comprobamos si ya tenemos esos platos escaneados
        en la base de datos. De ser asi no es necesario usar las APIs externas
         */
        SharedPreferences sp = getSharedPreferences("menus", Context.MODE_PRIVATE);

        if( sp.getBoolean("PlatesFirstCall", false) ) {
            StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

            FoodRepository fr = new FoodRepository(this.getApplication());

            for (String part : parts) {
                Plate aux = fr.getPlate(part);
                if (aux != null)
                    plates.add(aux);
            }

            StrictMode.setThreadPolicy(old);

            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("PlatesFirstCall", false);
            editor.apply();

            Log.v("info", message);
        }

        /*
        Configuramos el ArrayAdapter y añadimos los comportamientos oportunos a cada elemento
        contenido en el
         */
        arrayAdapter = new PlateArrayAdapter
                (this, android.R.layout.simple_list_item_1, plates);
        arrayAdapter.setNotifyOnChange(true);


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
                Intent intent = new Intent(getApplicationContext(), EditPlateActivity.class);

                intent.putExtra("plates", EncodeDecodeUtil.encodePlates(plates));

                ArrayList<Plate> aux = new ArrayList<Plate>();
                aux.add(actual);
                intent.putExtra("plate", EncodeDecodeUtil.encodePlates(aux));

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

                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);

                intent.putExtra("plates", EncodeDecodeUtil.encodePlates(plates));
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

            if(resultCode == RESULT_OK) {

                ArrayList<Plate> platos = EncodeDecodeUtil.decodePlates(data.getStringExtra("plates"));
                Plate platoMod = EncodeDecodeUtil.decodePlates(data.getStringExtra("plate")).get(0);
                int pos = Integer.parseInt(data.getStringExtra("pos"));

                platos.set(pos, platoMod);

                plates.addAll(platos);
                arrayAdapter.notifyDataSetChanged();

            }
        }else if(requestCode==2){

            if(resultCode == RESULT_OK) {

                plates.addAll(EncodeDecodeUtil.decodePlates(data.getStringExtra("plates")));
                arrayAdapter.notifyDataSetChanged();


            }
        }
    }
}