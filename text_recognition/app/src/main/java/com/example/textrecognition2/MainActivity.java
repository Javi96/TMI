package com.example.textrecognition2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button text_recognition;

    private Button dandelion;

    private Button text_razor;

    private CardView cv_scan_diet_cart;
    private CardView cv_shop_cart;
    private CardView cv_pantry_cart;

    private Button btn_diet_flip;
    private Button btn_pantry_flip;
    private Button btn_shop_cart_flip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //text_recognition = findViewById(R.id.text_recognition);
        //dandelion = findViewById(R.id.dandelion);
        //text_razor = findViewById(R.id.text_razor);

        cv_scan_diet_cart = findViewById(R.id.component_scan_diet_card_view);
        cv_shop_cart = findViewById(R.id.component_shop_cart_card_view);
        cv_pantry_cart = findViewById(R.id.component_pantry_card_view);

        btn_pantry_flip = findViewById(R.id.pantry_flip_button);
        btn_diet_flip = findViewById(R.id.scan_diet_flip_button);
        btn_shop_cart_flip = findViewById(R.id.shop_cart_flip_button);

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
        //cv_shop_cart.setAnimation(animation);

        /*
        FoodDatabase db = FoodDatabase.getDatabase(this.getApplicationContext());
        FoodRepository fr = new FoodRepository(this.getApplication());

        //SystemClock.sleep(5000);

        //if ( fr.getPlate("flan") != null)
        if ( db.plateDao().findByName("flan").getValue() != null)
            Toast.makeText(getApplicationContext(), "Plato : " + fr.getPlate("flan").toString() , Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(), "Fuck no estaba el flan" , Toast.LENGTH_LONG).show();
        //*/
    }

    @Override
    public void onClick(View v) {

        final Drawable changed = ContextCompat.getDrawable(getApplicationContext(), R.drawable.card);
        final Drawable normal = ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_inverted);
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
                /*
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(btn_shop_cart_flip, "scaleX", 1f, 0f);
                        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(btn_shop_cart_flip, "scaleX", 0f, 1f);
                        oa1.setInterpolator(new DecelerateInterpolator());
                        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                        oa1.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                btn_shop_cart_flip.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.right_arrow_inverted));
                                oa2.start();
                            }
                        });
                        oa1.start();
                    }
                }, 250);
                //*/
                //*
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), IngredientsActivity.class));
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    }
                }, 150);
                break;
                //*/

            /*
            case R.id.dandelion:

                dandelion.setBackground(changed);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //dandelion.setBackground(normal);
                        startActivity(new Intent(getApplicationContext(), DandelionActivity.class));
                    }
                }, 150);
                break;

                case R.id.text_razor:
                text_razor.setBackground(changed);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        text_razor.setBackground(normal);
                    }
                }, 1618);
                //startActivity(new Intent(getApplicationContext(), TextRazorActivity.class));
                break;
                */
            // Do something
            default:
                break;
        }
    }


}
