package com.example.textrecognition2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class EditPlateActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout layout;

    Button btn_edit_plate;

    private String message;

    private String position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plate);



        btn_edit_plate = findViewById(R.id.btn_edit_plate);

        Intent intent = getIntent();
        message = intent.getStringExtra("plate");
        position = intent.getStringExtra("pos");
        LinearLayout myRoot = (LinearLayout) findViewById(R.id.act_edit_plate_layout1);
        layout =  findViewById(R.id.act_edit_plate_sub_layout);


        //Toast.makeText(getApplicationContext(), "Has seleccionado: " + message, Toast.LENGTH_LONG).show();

        String[] items = message.split("_");
        for(String item: items){
            EditText editText = new EditText(getApplicationContext());
            editText.setText(item);
            editText.setTextSize(28);
            layout.addView(editText);
        }


        btn_edit_plate.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_edit_plate:
                StringBuilder stringBuilder = new StringBuilder("");

                int count = layout.getChildCount();
                EditText edt = null;
                for(int i=0; i<count; i++) {
                    edt =  (EditText) layout.getChildAt(i);
                    stringBuilder.append( edt.getText() + "_");
                }


                Log.e("vvvv", stringBuilder.toString());

                Intent intent = new Intent();
                intent.putExtra("plate", stringBuilder.toString());
                intent.putExtra("pos", String.valueOf(this.position));
                setResult(RESULT_OK, intent);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                finish();
        }
    }
}
