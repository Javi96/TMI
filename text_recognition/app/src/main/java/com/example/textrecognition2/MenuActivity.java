package com.example.textrecognition2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.textrecognition2.domain.FoodRepository;
import com.example.textrecognition2.domain.IngrCant;
import com.example.textrecognition2.domain.Plate;
import com.example.textrecognition2.utilities.EncodeDecodeUtil;
import com.marozzi.roundbutton.RoundButton;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

/**
 * <h1>Actividad que muestra la configuracion del menu</h1>
 * Permite al usuario configurar el menu de cada dia. Para ello
 * cuenta con un Spinner para seleccionar el dia de la semana asi
 * como un TextView con la informacion del menu de ese dia
 */
public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Atributos privados
     */
    private TextView button3;

    private LinearLayout layout;

    private ArrayList<Plate> plates;

    private String day;

    private RoundButton floatingActionButton;

    /**
     * Genere un componente TextView autoconfigurado para un plato
     * @param context Contexto de la aplicacion
     * @return Devuelve un objeto TextView ya configurado
     */
    private static TextView PlateText(Context context){
        TextView resul = new TextView(context);
        resul.setTextSize(24);
        resul.setTypeface(Typeface.createFromAsset(context.getAssets(), "ultra.ttf"));
        resul.setTextColor(context.getResources().getColor(R.color.colorBlack));
        return resul;
    }

    /**
     * Genere un componente TextView autoconfigurado para un ingrediente
     * @param context Contexto de la aplicacion
     * @return Devuelve un objeto TextView ya configurado
     */
    private static TextView IngrText(Context context){
        TextView resul = new TextView(context);
        resul.setTextSize(20);
        resul.setTypeface(Typeface.createFromAsset(context.getAssets(), "lato_bold.ttf"));
        resul.setTextColor(context.getResources().getColor(R.color.colorBlue));
        return resul;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // decodificamos los platos
        plates = EncodeDecodeUtil.decodePlates(getIntent().getStringExtra("plates").toLowerCase());

        /*
        * Configuramos los atributos privados
        */
        TextView textView = findViewById(R.id.cmp_sub_title1);
        button3 = findViewById(R.id.cmp_sub_title2);
        textView.setText("Day:");
        button3.setText("Plates:");

        Spinner spinner = findViewById(R.id.act_menu_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.act_menu_days, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        layout =  findViewById(R.id.act_menu_sub_layout);
        /*
        * Generamos el layout de forma dinamica con la informacion de los platos
        * */
        for(Plate plate : plates){
            TextView editText = PlateText(getApplicationContext());
            editText.setText(plate.getName());
            layout.addView(editText);
            for(IngrCant ing : plate.getIngredients()){
                TextView ingr = IngrText(getApplicationContext());
                ingr.setText("\t\t"+ing.getNombre());
                layout.addView(ingr);
            }
        }

        /*
        * Configuramos el comportamiento del spinner
        * */
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                day = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        floatingActionButton = findViewById(R.id.act_menu_confirm);

        floatingActionButton.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.putExtra("plates", getIntent().getStringExtra("plates"));
        setResult(RESULT_OK, intent);

        finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.act_menu_confirm:
                // cargamos el almacenamiento interno
                SharedPreferences prefs =
                        getSharedPreferences("menus", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                // ejecutamos sobre un hilo en parelelo las peticiones a la DB
                StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
                new FoodRepository(this.getApplication()).insertPlates(plates);
                StrictMode.setThreadPolicy(old);

                // compactamos la informacion de los platos y la guardamos en el alm interno
                StringBuilder sb = new StringBuilder();
                for( Plate plate : plates)
                    sb.append(plate.getName() + '_');

                sb.deleteCharAt(sb.length()-1);
                editor.putString(this.day, sb.toString());
                editor.putString("return", "MenuActivity");
                editor.apply();


                // configuramos las animaciones de los botones
                floatingActionButton.startAnimation();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        floatingActionButton.setResultState(RoundButton.ResultState.SUCCESS);
                    }
                }, 1614);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    }
                }, 2000);
        }
    }
}
