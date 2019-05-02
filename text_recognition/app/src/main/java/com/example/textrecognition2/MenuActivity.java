package com.example.textrecognition2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
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

import com.example.textrecognition2.domain.Plate;
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

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textView;

    private TextView button3;

    private LinearLayout layout;

    private String plates;


    private String day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        plates = intent.getStringExtra("plates");
        String[] data = plates.split("-");

        TextView button2 = findViewById(R.id.cmp_sub_title1);
        button3 = findViewById(R.id.cmp_sub_title2);
        button2.setText("Selecciona \nel día:");
        button3.setText("Platos:");

        Spinner spinner = (Spinner) findViewById(R.id.act_menu_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.act_menu_days, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        layout =  findViewById(R.id.act_menu_sub_layout);

        //layout.setPadding(16,16,16,16);

        //Toast.makeText(getApplicationContext(), "Has seleccionado: " + message, Toast.LENGTH_LONG).show();
        for(String plate: data) {
            String[] items = plate.split("_");
            int len = items.length;
            for (int i = 0; i < len; i++) {
                TextView editText = new TextView(getApplicationContext());
                editText.setText(items[i]);
                if (i == 0) {
                    editText.setTextSize(24);
                    Typeface font = Typeface.createFromAsset(getAssets(), "ultra.ttf");
                    editText.setTypeface(font);
                    editText.setTextColor(getResources().getColor(R.color.colorBlack));
                } else {
                    editText.setTextSize(20);
                    Typeface font = Typeface.createFromAsset(getAssets(), "lato_bold.ttf");
                    editText.setTypeface(font);
                    editText.setTextColor(getResources().getColor(R.color.colorBlue));
                }
                layout.addView(editText);
            }
        }






        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //textView.setText(parent.getItemAtPosition(position).toString());
                //button3.setText("Estos son los platos para el " + parent.getItemAtPosition(position).toString());
                day = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        RoundButton floatingActionButton = findViewById(R.id.act_menu_confirm);
        floatingActionButton.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.putExtra("plates", plates);
        setResult(RESULT_OK, intent);



        finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.act_menu_confirm:
                SharedPreferences prefs =
                        getSharedPreferences("menus", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                StringBuilder sb = new StringBuilder();
                for( String str : this.plates.split("-")){
                    String[] ingr = str.split("_");
                    sb.append(ingr[0]+ '_');
                }

                editor.putString(this.day, sb.toString());
                editor.apply();


                editor.putString("return", "MenuActivity");
                editor.apply();

                startActivity(new Intent(getApplicationContext(), MainActivity.class));



        }
    }
}
