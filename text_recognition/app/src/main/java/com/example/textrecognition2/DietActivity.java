package com.example.textrecognition2;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.textrecognition2.domain.FoodDatabase;
import com.example.textrecognition2.domain.FoodRepository;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * <h1>Actividad intermediaria entre la dieta y el reconocimiento de texto</h1>
 */
public class DietActivity extends AppCompatActivity implements View.OnClickListener{


    /**
     * Atributos privados
     */
    CardView see_diet;
    CardView scan_diet;

    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        title = findViewById(R.id.cmp_tile_title);
        see_diet = findViewById(R.id.component_see_diet_card_view);
        scan_diet = findViewById(R.id.component_scan_diet_card_view);


        title.setText("Diet");
        see_diet.setOnClickListener(this);
        scan_diet.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.component_see_diet_card_view:
                startActivity(new Intent(getApplicationContext(), DietScheduleActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
            case R.id.component_scan_diet_card_view:
                startActivity(new Intent(getApplicationContext(), TextRecognitionActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

}
