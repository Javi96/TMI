package com.example.textrecognition2;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.textrecognition2.domain.Plate;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuActivity extends AppCompatActivity {

    TextView textView;

    private TextView button3;

    private LinearLayout layout;

    private String plates;

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

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.putExtra("plates", plates);
        setResult(RESULT_OK, intent);



        finish();
        super.onBackPressed();
    }
}
