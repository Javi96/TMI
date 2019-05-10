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

import java.util.ArrayList;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class InventoryActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout layout;

    Button btn_edit_plate;
    Button btn_add_ingr;

    /*private String message;

    private String todos_platos;

    private String position;*/

    private class HorLay extends LinearLayout{
        public HorLay(Context context) {
            super(context);
            super.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            super.setOrientation(HORIZONTAL);
        }
    }

    private static EditText PlateText(Context context){
        final EditText resul = new EditText(context);
        resul.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toasty.warning(resul.getContext().getApplicationContext(), "No se puede eliminar el título", Toast.LENGTH_SHORT * 10, true).show();
                return true;
            }
        });
        resul.setTextSize(28);
        resul.setTypeface(Typeface.createFromAsset(context.getAssets(), "ultra.ttf"));
        resul.setTextColor(context.getResources().getColor(R.color.colorRed));
        return resul;
    }

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

        /*Intent intent = getIntent();
        message = intent.getStringExtra("plate");
        todos_platos = intent.getStringExtra("plates");
        position = intent.getStringExtra("pos");*/

        LinearLayout myRoot = (LinearLayout) findViewById(R.id.act_inventory_layout1);
        myRoot.setPadding(64,16,64,16);
        layout =  findViewById(R.id.act_edit_plate_sub_layout);

        //layout.setPadding(16,16,16,16);

        //Toast.makeText(getApplicationContext(), "Has seleccionado: " + message, Toast.LENGTH_LONG).show();
        /*
        String info = "plato1___nombre1***unidad1***1___nombre2***unidad2***2___nombre3***unidad3***3---plato2___nombre4***unidad4***4___nombre5***unidad5***5___nombre6***unidad6***6";
        Plate plato = EncodeDecodeUtil.decodePlates(info).get(0);
        Toast.makeText(getApplicationContext(), "Has seleccionado: " + plato.toString(), Toast.LENGTH_LONG).show();
        */

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

        //final LinearLayout nombrLay = findViewById(R.id.act_edit_plate_sub_horz);
        /*
        final EditText editText = PlateText(getApplicationContext());
        editText.setText(plato.getName());

        layout.addView(editText);
        */

        //for(IngrCant ing : plato.getIngredients()){
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
                String nombrePlato = ((EditText)layout.getChildAt(0)).getText().toString();
                if(nombrePlato == ""){
                    Toasty.error(getApplicationContext(), "Introduce un nombre válido", Toast.LENGTH_SHORT * 10, true).show();
                    break;
                }
                ArrayList<IngrCant> ingredientes = new ArrayList<IngrCant>();
                for( int i = 1; i < layout.getChildCount(); i++){
                    LinearLayout lay = (LinearLayout) layout.getChildAt(i);
                    String ingr = ((EditText)lay.getChildAt(0)).getText().toString();
                    if(ingr == ""){
                        Toasty.error(getApplicationContext(), "Introduce un ingrediente válido", Toast.LENGTH_SHORT * 10, true).show();
                        break;
                    }
                    int cant = 0;
                    String unit = "uncount";
                    if(lay.getChildCount() == 3){
                        try {
                            cant = Integer.parseInt(((EditText) lay.getChildAt(1)).getText().toString());
                        } catch (NumberFormatException e) {
                            Toasty.error(getApplicationContext(), "Introduce una cantidad válida", Toast.LENGTH_SHORT * 10, true).show();
                            break;
                        }
                        unit = ((EditText)lay.getChildAt(2)).getText().toString();
                        if(ingr == ""){
                            ingr = "uncount";
                            break;
                        }
                    }
                    ingredientes.add(new IngrCant(ingr, cant, unit));
                }
                final ArrayList<Plate> resul = new ArrayList<Plate>();
                resul.add( new Plate(nombrePlato, ingredientes) );

                /*
                final StringBuilder stringBuilder = new StringBuilder();

                int count = layout.getChildCount();
                EditText edt = null;
                for(int i=0; i<count; i++) {
                    edt =  (EditText) layout.getChildAt(i);

                    stringBuilder.append( edt.getText().toString().replace(' ', '_') + "_");
                }
                */

                final RoundButton btn = findViewById(R.id.btn_edit_plate);

                btn.startAnimation();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn.setResultState(RoundButton.ResultState.SUCCESS);
                    }
                }, 1614);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        /*Intent intent = new Intent();
                        intent.putExtra("plate", EncodeDecodeUtil.encodePlates(resul));
                        intent.putExtra("pos", String.valueOf(position));
                        intent.putExtra("plates", todos_platos);
                        setResult(RESULT_OK, intent);*/

                        finish();

                    }
                }, 2000);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        /*Intent intent = new Intent();
        intent.putExtra("plate", message);
        intent.putExtra("pos", String.valueOf(position));
        intent.putExtra("plates", todos_platos);
        setResult(RESULT_OK, intent);*/



        finish();
        super.onBackPressed();

    }
}
