package com.example.textrecognition2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button text_recognition;

    private Button dandelion;

    private Button text_razor;

    private CardView scan_diet;
    private CardView shop_cart;

    private Button btn_scan_fridge_flip;
    private Button btn_scan_diet_flip;
    private Button btn_pantry_flip;
    private Button btn_shop_cart_flip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //text_recognition = findViewById(R.id.text_recognition);
        //dandelion = findViewById(R.id.dandelion);
        //text_razor = findViewById(R.id.text_razor);

        scan_diet = findViewById(R.id.component_scan_diet_card_view);
        shop_cart = findViewById(R.id.component_shop_cart_card_view);

        btn_scan_fridge_flip = findViewById(R.id.scan_fridge_flip_button);
        btn_pantry_flip = findViewById(R.id.pantry_flip_button);
        btn_scan_diet_flip = findViewById(R.id.scan_diet_flip_button);
        btn_shop_cart_flip = findViewById(R.id.shop_cart_flip_button);

        btn_scan_fridge_flip.setOnClickListener(this);
        btn_pantry_flip.setOnClickListener(this);
        btn_scan_diet_flip.setOnClickListener(this);
        btn_shop_cart_flip.setOnClickListener(this);
        scan_diet.setOnClickListener(this);
        shop_cart.setOnClickListener(this);
        //text_recognition.setOnClickListener(this);
        //dandelion.setOnClickListener(this);
        //text_razor.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final Drawable changed = ContextCompat.getDrawable(getApplicationContext(), R.drawable.card);
        final Drawable normal = ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_inverted);
        switch (v.getId()) {
            case R.id.scan_fridge_flip_button:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(btn_scan_fridge_flip, "scaleX", 1f, 0f);
                        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(btn_scan_fridge_flip, "scaleX", 0f, 1f);
                        oa1.setInterpolator(new DecelerateInterpolator());
                        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                        oa1.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                btn_scan_fridge_flip.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.right_arrow_inverted));
                                oa2.start();
                            }
                        });
                        oa1.start();
                    }
                }, 500);
                break;
            case R.id.scan_diet_flip_button:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(btn_scan_diet_flip, "scaleX", 1f, 0f);
                        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(btn_scan_diet_flip, "scaleX", 0f, 1f);
                        oa1.setInterpolator(new DecelerateInterpolator());
                        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                        oa1.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                btn_scan_diet_flip.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.right_arrow_inverted));
                                oa2.start();
                            }
                        });
                        oa1.start();
                    }
                }, 500);
                break;
            case R.id.pantry_flip_button:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(btn_pantry_flip, "scaleX", 1f, 0f);
                        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(btn_pantry_flip, "scaleX", 0f, 1f);
                        oa1.setInterpolator(new DecelerateInterpolator());
                        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                        oa1.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                btn_pantry_flip.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.right_arrow_inverted));
                                oa2.start();
                            }
                        });
                        oa1.start();
                    }
                }, 500);
                break;

            /*case R.id.text_recognition:
                text_recognition.setBackground(changed);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        text_recognition.setBackground(normal);
                    }
                }, 1618);
                startActivity(new Intent(getApplicationContext(), TextRecognitionActivity.class));
                break;*/
            case R.id.component_scan_diet_card_view:
                //text_recognition.setBackground(changed);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    }
                }, 1618);
                startActivity(new Intent(getApplicationContext(), TextRecognitionActivity.class));
                break;
            case R.id.component_shop_cart_card_view:
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
                }, 500);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), ShopCartActivity.class));
                    }
                }, 1500);

                break;
            case R.id.shop_cart_flip_button:
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
                }, 500);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), ShopCartActivity.class));
                    }
                }, 1500);
                break;



            /*case R.id.dandelion:
                dandelion.setBackground(changed);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dandelion.setBackground(normal);
                    }
                }, 1618);
                //startActivity(new Intent(getApplicationContext(), DandelionActivity.class));
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
                break;*/
            // Do something
            default:
                break;
        }
    }
}
