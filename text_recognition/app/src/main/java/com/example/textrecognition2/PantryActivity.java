package com.example.textrecognition2;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

public class PantryActivity extends AppCompatActivity implements View.OnClickListener{

    private CardView scan_fridge;
    private CardView see_pantry;
    private ConstraintLayout layout_fridge;
    private ConstraintLayout layout_pantry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        scan_fridge = findViewById(R.id.component_fridge_scan_card_view);
        see_pantry = findViewById(R.id.component_see_pantry_card_view);
        layout_fridge = findViewById(R.id.cmp_scan_fridge_layout);
        layout_pantry = findViewById(R.id.cmp_see_pantry_layout);


        scan_fridge.setOnClickListener(this);
        see_pantry.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.component_fridge_scan_card_view:

                break;
            case R.id.component_see_pantry_card_view:

                break;
        }
    }
}
