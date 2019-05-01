package com.example.textrecognition2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.textrecognition2.domain.FoodRepository;
import com.example.textrecognition2.domain.IngrCant;
import com.google.android.gms.vision.text.Line;

import java.util.ArrayList;
import java.util.List;

public class DietScheduleActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_schedule);


        TextView textView = findViewById(R.id.cmp_tile_title);
        textView.setText("Diet schedule");
        String[] days = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};



        SharedPreferences prefs =
                getSharedPreferences("menus", Context.MODE_PRIVATE);

        LinearLayout layout =  findViewById(R.id.act_diet_sched_layout);



        TextView child;
        Typeface font1 = Typeface.createFromAsset(getAssets(), "ultra.ttf");
        Typeface font2 = Typeface.createFromAsset(getAssets(), "lato_bold.ttf");

        StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        FoodRepository fr = new FoodRepository(this.getApplication());

        for(String day: days){
            String data = prefs.getString(day, "");
            if(!data.equalsIgnoreCase("")) {

                child = new TextView(getApplicationContext());
                child.setText(day);
                child.setTextSize(28);
                child.setTypeface(font1);
                child.setTextColor(getResources().getColor(R.color.colorWhite));
                layout.addView(child);

                String[] plates = data.split("_");

                for(int i=0; i< plates.length; i++) {
                    /*
                    String[] ingredients = plates[i].split("_");
                    child = new TextView(getApplicationContext());
                    child.setTextSize(22);
                    child.setText(ingredients[0]);
                    child.setTypeface(font1);
                    child.setTextColor(getResources().getColor(R.color.cpb_green));
                    layout.addView(child);
                    for(int j=1; j< ingredients.length; j++) {
                        child = new TextView(getApplicationContext());
                        child.setTextSize(18);
                        child.setText(ingredients[j]);
                        child.setTypeface(font2);
                        child.setTextColor(getResources().getColor(R.color.colorBlue));
                        layout.addView(child);
                    }
                    */
                    List<IngrCant> ingredients = fr.getIngredientsForPlate(plates[i]);
                    child = new TextView(getApplicationContext());
                    child.setTextSize(22);
                    child.setText(plates[i]);
                    child.setTypeface(font1);
                    child.setTextColor(getResources().getColor(R.color.cpb_green));
                    layout.addView(child);
                    for( IngrCant ing :  ingredients ) {
                        child = new TextView(getApplicationContext());
                        child.setTextSize(18);
                        child.setText(ing.toString());
                        child.setTypeface(font2);
                        child.setTextColor(getResources().getColor(R.color.colorBlue));
                        layout.addView(child);
                    }
                }
                     Log.e(day, data);
            }
        }
        StrictMode.setThreadPolicy(old);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*case R.id.act_diet_schedule_float_button:
                startActivity(new Intent(getApplicationContext(), TextRecognitionActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;*/
        }
    }
}
