package com.example.textrecognition2;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.marozzi.roundbutton.RoundButton;

import es.dmoral.toasty.Toasty;

public class EditPlateActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout layout;

    Button btn_edit_plate;
    Button btn_add_ingr;

    private String message;

    private String todos_platos;

    private String position;

    private StringBuilder initialInfo = null;

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
        layout =  findViewById(R.id.act_edit_plate_sub_layout);
        myRoot.setPadding(64,16,64,16);

        //layout.setPadding(16,16,16,16);

        //Toast.makeText(getApplicationContext(), "Has seleccionado: " + message, Toast.LENGTH_LONG).show();

        String[] items = message.split("_");
        int len = items.length;
        final EditText editText = new EditText(getApplicationContext());
        editText.setText(items[0]);
        editText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toasty.warning(getApplicationContext(), "No se puede eliminar el título", Toast.LENGTH_SHORT * 10, true).show();
                return true;
            }
        });

        editText.setTextSize(28);
        Typeface font = Typeface.createFromAsset(getAssets(), "ultra.ttf");
        editText.setTypeface(font);
        editText.setTextColor(getResources().getColor(R.color.colorRed));
        layout.addView(editText);

        font = Typeface.createFromAsset(getAssets(), "lato_bold.ttf");
        for(int i=1; i< len; i+=3){
            final EditText texto = new EditText(getApplicationContext());
            editText.setText(items[i] + " " + items[i+1] + ' ' + items[i+2]);
            texto.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    layout.removeView(v);
                    return true;
                }
            });
            texto.setTextSize(20);
            texto.setTypeface(font);
            texto.setTextColor(getResources().getColor(R.color.colorBlue));

            layout.addView(texto);
        }
        btn_edit_plate.setOnClickListener(this);
        btn_add_ingr.setOnClickListener(this);

        initialInfo = new StringBuilder();
        int count = layout.getChildCount();
        EditText edt = null;
        for(int i=0; i<count; i++) {
            edt =  (EditText) layout.getChildAt(i);

            initialInfo.append( edt.getText().toString().replace(' ', '_') + "_");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.act_edit_plate_add_ingr:
                final EditText editText = new EditText(getApplicationContext());
                editText.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        layout.removeView(v);
                        return true;
                    }
                });
                editText.setTextSize(20);
                Typeface font = Typeface.createFromAsset(getAssets(), "lato_bold.ttf");
                editText.setTypeface(font);
                editText.setTextColor(getResources().getColor(R.color.colorBlue));
                layout.addView(editText);
                break;
            case R.id.btn_edit_plate:
                final StringBuilder stringBuilder = new StringBuilder();

                int count = layout.getChildCount();
                EditText edt = null;
                for(int i=0; i<count; i++) {
                    edt =  (EditText) layout.getChildAt(i);

                    stringBuilder.append( edt.getText().toString().replace(' ', '_') + "_");
                }


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
                        intent.putExtra("plate", stringBuilder.toString());
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
        intent.putExtra("plate", initialInfo.toString());
        intent.putExtra("pos", String.valueOf(position));
        intent.putExtra("plates", todos_platos);
        setResult(RESULT_OK, intent);



        finish();
        super.onBackPressed();

    }
}
