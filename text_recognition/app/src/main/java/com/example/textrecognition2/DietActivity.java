package com.example.textrecognition2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class DietActivity extends AppCompatActivity implements View.OnClickListener{


    CardView see_diet;
    CardView scan_diet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);


        see_diet = findViewById(R.id.component_see_diet_card_view);
        scan_diet = findViewById(R.id.component_scan_diet_card_view);

        see_diet.setOnClickListener(this);
        scan_diet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.component_see_diet_card_view:
                startActivity(new Intent(getApplicationContext(), DietScheduleActivity.class));
                break;
            case R.id.component_scan_diet_card_view:
                startActivity(new Intent(getApplicationContext(), TextRecognitionActivity.class));
                break;
        }
    }
}
