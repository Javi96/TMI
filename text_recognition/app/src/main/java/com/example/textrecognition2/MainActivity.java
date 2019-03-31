package com.example.textrecognition2;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button text_recognition;

    private Button dandelion;

    private Button text_razor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text_recognition = findViewById(R.id.text_recognition);
        dandelion = findViewById(R.id.dandelion);
        text_razor = findViewById(R.id.text_razor);

        text_recognition.setOnClickListener(this);
        dandelion.setOnClickListener(this);
        text_razor.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final Drawable changed = ContextCompat.getDrawable(getApplicationContext(), R.drawable.button1);
        final Drawable normal = ContextCompat.getDrawable(getApplicationContext(), R.drawable.button2);
        switch (v.getId()) {
            case R.id.text_recognition:
                text_recognition.setBackground(changed);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        text_recognition.setBackground(normal);
                    }
                }, 1618);
                //startActivity(new Intent(getApplicationContext(), TextRecognitionActivity.class));
                break;
            case R.id.dandelion:
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
                break;
            // Do something
            default:
                break;
        }
    }
}
