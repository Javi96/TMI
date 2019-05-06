package com.example.textrecognition2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

public class PantryActivity extends AppCompatActivity implements View.OnClickListener{

    private CardView scan_fridge;
    private CardView see_pantry;
    private ConstraintLayout layout_fridge;
    private ConstraintLayout layout_pantry;

    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        title = findViewById(R.id.cmp_tile_title);
        scan_fridge = findViewById(R.id.component_fridge_scan_card_view);
        see_pantry = findViewById(R.id.component_see_pantry_card_view);
        layout_fridge = findViewById(R.id.cmp_scan_fridge_layout);
        layout_pantry = findViewById(R.id.cmp_see_pantry_layout);

        title.setText("Pantry");
        scan_fridge.setOnClickListener(this);
        see_pantry.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.component_fridge_scan_card_view:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), ImageRecognitionActivity.class));
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    }
                }, 150);
                break;
            case R.id.component_see_pantry_card_view:

                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

}
