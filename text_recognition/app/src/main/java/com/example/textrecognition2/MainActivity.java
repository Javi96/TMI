package com.example.textrecognition2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.example.textrecognition2.domain.FoodDatabase;
import com.example.textrecognition2.domain.FoodRepository;
import com.example.textrecognition2.domain.Ingredient;
import com.example.textrecognition2.domain.Plate;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs =
                getSharedPreferences("menus", Context.MODE_PRIVATE);

        if(prefs.contains("return")) {
            String ret = prefs.getString("return", "");
            assert ret != null;
            if (ret.equalsIgnoreCase("MenuActivity")) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("return");
                editor.apply();
                Toasty.success(getApplicationContext(), "Success!", Toast.LENGTH_SHORT * 10, true).show();
            }
        }

        FoodDatabase db = FoodDatabase.getDatabase(this.getApplicationContext());
        db.inTransaction();

        CardView cv_scan_diet_cart = findViewById(R.id.component_scan_diet_card_view);
        CardView cv_shop_cart = findViewById(R.id.component_shop_cart_card_view);
        CardView cv_pantry_cart = findViewById(R.id.component_pantry_card_view);

        Button btn_pantry_flip = findViewById(R.id.pantry_flip_button);
        Button btn_diet_flip = findViewById(R.id.scan_diet_flip_button);
        Button btn_shop_cart_flip = findViewById(R.id.shop_cart_flip_button);

        btn_pantry_flip.setOnClickListener(this);
        btn_diet_flip.setOnClickListener(this);
        btn_shop_cart_flip.setOnClickListener(this);

        cv_scan_diet_cart.setOnClickListener(this);
        cv_shop_cart.setOnClickListener(this);
        cv_pantry_cart.setOnClickListener(this);


        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1000);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(1000);

        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scan_diet_flip_button:
            case R.id.component_scan_diet_card_view:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), DietActivity.class));
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    }
                }, 150);
                break;
            case R.id.pantry_flip_button:
            case R.id.component_pantry_card_view:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), PantryActivity.class));
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    }
                }, 150);
                break;
            case R.id.shop_cart_flip_button:
            case R.id.component_shop_cart_card_view:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), ShopCartActivity.class));
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    }
                }, 150);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {

    }
}
