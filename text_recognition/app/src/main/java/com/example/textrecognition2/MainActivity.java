package com.example.textrecognition2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

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


        text_recognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TextRecognitionActivity.class);
                startActivity(intent);
            }
        });

        dandelion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DandelionActivity.class);
                startActivity(intent);
            }
        });

        text_razor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TextRazorActivity.class);
                startActivity(intent);
            }
        });
    }

}
