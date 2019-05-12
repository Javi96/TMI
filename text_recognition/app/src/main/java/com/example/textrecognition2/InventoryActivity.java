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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.textrecognition2.domain.FoodRepository;
import com.example.textrecognition2.domain.IngrCant;
import com.example.textrecognition2.domain.Ingredient;
import com.example.textrecognition2.domain.Plate;
import com.example.textrecognition2.utilities.EncodeDecodeUtil;
import com.marozzi.roundbutton.RoundButton;

import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import es.dmoral.toasty.Toasty;

/**
 * <h1>Actividad que muestra el inventario del usuario</h1>
 * Muestra los ingredientes en el inventario y ademas permite modificar
 * tanto su cantidad como sus unidades
 */
public class InventoryActivity extends AppCompatActivity implements View.OnClickListener{

    /**
     * Atributos privados
     */
    LinearLayout layout;
    Button btn_edit_plate;
    Button btn_add_ingr;


    /**
     * Genrea un LinearLayout configurado
     */
    private class HorLay extends LinearLayout{
        /**
         * Constructor de clase
         * @param context Contexto de la app
         */
        public HorLay(Context context) {
            super(context);
            super.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            super.setOrientation(HORIZONTAL);
        }
    }

    /**
     * Genera un EditText configurado para un ingrediente
     * @param context Contexto de la app
     * @param auxLay Layout padre del componente generado
     * @return EditText configurado
     */
    private EditText IngrText(Context context, final LinearLayout auxLay){
        EditText resul = new EditText(context);
        resul.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                layout.removeView(auxLay);
                return true;
            }
        });
        resul.setTextSize(20);
        resul.setTypeface(Typeface.createFromAsset(getAssets(), "lato_bold.ttf"));
        resul.setTextColor(getResources().getColor(R.color.colorBlue));
        return resul;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        btn_edit_plate = findViewById(R.id.btn_edit_plate);
        btn_add_ingr = findViewById(R.id.act_edit_plate_add_ingr);

        /*
        * Instancia los componentes de la vista*/
        LinearLayout myRoot = (LinearLayout) findViewById(R.id.act_inventory_layout1);
        myRoot.setPadding(64,16,64,16);
        layout =  findViewById(R.id.act_edit_plate_sub_layout);


        /*
        * Cargamos datos del almacenamiento interno y conusultamos a la BD*/
        SharedPreferences inventory = getSharedPreferences("inventory", Context.MODE_PRIVATE);
        ArrayList<IngrCant> ingredientes = new ArrayList<IngrCant>();
        StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        FoodRepository fr = new FoodRepository(this.getApplication());

        for( String key  : inventory.getAll().keySet()){
            Ingredient ing = fr.getIngredient(key);
            if(ing == null) //No debería ocurrir nunca, por si acaso
                continue;
            try {
                ingredientes.add(new IngrCant(key, ing.getUnits(), inventory.getInt(key, 0)));
            } catch ( ClassCastException e){continue;} // Esto es por el getInt, también por si acaso

        }

        StrictMode.setThreadPolicy(old);

        /*
        * Añade a la vista la informacion de los platos
        * */
        for(IngrCant ing : ingredientes){
            final LinearLayout auxLay = new InventoryActivity.HorLay(this);
            final EditText nombre = IngrText(getApplicationContext(), auxLay);
            nombre.setText(ing.getNombre());
            auxLay.addView(nombre);

            if( ! ing.getUnidades().equalsIgnoreCase("uncount") && ing.getQuantity() != 0) {
                final EditText cant = IngrText(getApplicationContext(), auxLay);
                cant.setText(Integer.toString(ing.getQuantity()));

                final EditText unid = IngrText(getApplicationContext(), auxLay);
                unid.setText(ing.getUnidades());

                auxLay.addView(cant);
                auxLay.addView(unid);

            }

            layout.addView(auxLay);

        }
        btn_edit_plate.setOnClickListener(this);
        btn_add_ingr.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.act_edit_plate_add_ingr:
                final LinearLayout auxLay = new InventoryActivity.HorLay(this);

                auxLay.addView(IngrText(getApplicationContext(), auxLay));
                auxLay.addView(IngrText(getApplicationContext(), auxLay));
                auxLay.addView(IngrText(getApplicationContext(), auxLay));

                layout.addView(auxLay);
                break;

            case R.id.btn_edit_plate:
                final Ingredient[] resultIngr = new Ingredient[layout.getChildCount()];
                SharedPreferences.Editor edit = getSharedPreferences("inventory", MODE_PRIVATE).edit();
                edit.clear();
                for( int i = 0; i < layout.getChildCount(); i++){
                    LinearLayout lay = (LinearLayout) layout.getChildAt(i);
                    String ingr = ((EditText)lay.getChildAt(0)).getText().toString();
                    if(ingr == ""){
                        Toasty.error(getApplicationContext(), "Introduce un ingrediente válido", Toast.LENGTH_SHORT * 10, true).show();
                        return;
                    }
                    int cant = 0;
                    String unit = "uncount";
                    if(lay.getChildCount() == 3){
                        try {
                            cant = Integer.parseInt(((EditText) lay.getChildAt(1)).getText().toString());
                        } catch (NumberFormatException e) {
                            Toasty.error(getApplicationContext(), "Introduce una cantidad válida", Toast.LENGTH_SHORT * 10, true).show();
                            return;
                        }
                        unit = ((EditText)lay.getChildAt(2)).getText().toString();
                        if(unit == ""){
                            unit = "uncount";
                            //break;
                        }
                    }
                    resultIngr[i] = new Ingredient(ingr, unit);
                    edit.putInt(ingr, cant);
                }

                edit.apply();

                final RoundButton btn = findViewById(R.id.btn_edit_plate);

                btn.startAnimation();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
                        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

                        new FoodRepository(getApplication()).insertIngredients(resultIngr);

                        StrictMode.setThreadPolicy(old);
                        btn.setResultState(RoundButton.ResultState.SUCCESS);
                    }
                }, 1614);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        finish();

                    }
                }, 2000);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
