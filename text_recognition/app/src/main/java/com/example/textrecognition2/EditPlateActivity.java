package com.example.textrecognition2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.solver.widgets.ConstraintHorizontalLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.LinearLayout.LayoutParams;

import com.example.textrecognition2.domain.IngrCant;
import com.example.textrecognition2.domain.Plate;
import com.example.textrecognition2.utilities.EncodeDecodeUtil;
import com.marozzi.roundbutton.RoundButton;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class EditPlateActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout layout;

    Button btn_edit_plate;
    Button btn_add_ingr;

    private String message;

    private String todos_platos;

    private String position;

    private class HorLay extends LinearLayout{

        private final LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        public HorLay(Context context) {
            super(context);
            super.setLayoutParams(params);
            super.setOrientation(HORIZONTAL);
        }
    }

    private EditText PlateText(Context context){
        EditText resul = new EditText(context);
        resul.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toasty.warning(getApplicationContext(), "No se puede eliminar el título", Toast.LENGTH_SHORT * 10, true).show();
                return true;
            }
            });
        resul.setTextSize(28);
        resul.setTypeface(Typeface.createFromAsset(getAssets(), "ultra.ttf"));
        resul.setTextColor(getResources().getColor(R.color.colorRed));
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
        setContentView(R.layout.activity_edit_plate);

        btn_edit_plate = findViewById(R.id.btn_edit_plate);
        btn_add_ingr = findViewById(R.id.act_edit_plate_add_ingr);

        Intent intent = getIntent();
        message = intent.getStringExtra("plate");
        todos_platos = intent.getStringExtra("plates");
        position = intent.getStringExtra("pos");

        LinearLayout myRoot = (LinearLayout) findViewById(R.id.act_edit_plate_layout1);
        myRoot.setPadding(64,16,64,16);
        layout =  findViewById(R.id.act_edit_plate_sub_layout);

        //layout.setPadding(16,16,16,16);

        //Toast.makeText(getApplicationContext(), "Has seleccionado: " + message, Toast.LENGTH_LONG).show();

        Plate plato = EncodeDecodeUtil.decodePlates(message).get(0);

        //final LinearLayout nombrLay = findViewById(R.id.act_edit_plate_sub_horz);
        final EditText editText = PlateText(getApplicationContext());
        editText.setText(plato.getName());

        layout.addView(editText);

        for(IngrCant ing : plato.getIngredients()){
            final LinearLayout auxLay = new HorLay(this);
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
                final LinearLayout auxLay = new HorLay(this);

                auxLay.addView(IngrText(getApplicationContext(), auxLay));
                auxLay.addView(IngrText(getApplicationContext(), auxLay));
                auxLay.addView(IngrText(getApplicationContext(), auxLay));

                layout.addView(auxLay);
                break;

            case R.id.btn_edit_plate:
                String nombrePlato = ((EditText)layout.getChildAt(0)).getText().toString();
                if(nombrePlato == ""){
                    Toasty.error(getApplicationContext(), "Introduce un nombre válido", Toast.LENGTH_SHORT * 10, true).show();
                    return;
                }
                ArrayList<IngrCant> ingredientes = new ArrayList<IngrCant>();
                for( int i = 1; i < layout.getChildCount(); i++){
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

                        Intent intent = new Intent();
                        intent.putExtra("plate", EncodeDecodeUtil.encodePlates(resul));
                        intent.putExtra("pos", String.valueOf(position));
                        intent.putExtra("plates", todos_platos);
                        setResult(RESULT_OK, intent);

                        finish();

                    }
                }, 2000);
            break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        Intent intent = new Intent();
        intent.putExtra("plate", message);
        intent.putExtra("pos", String.valueOf(position));
        intent.putExtra("plates", todos_platos);
        setResult(RESULT_OK, intent);



        finish();
        super.onBackPressed();

    }
}
