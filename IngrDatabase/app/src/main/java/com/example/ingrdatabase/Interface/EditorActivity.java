package com.example.ingrdatabase.Interface;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.ingrdatabase.Database.IngrSchema;
import com.example.ingrdatabase.R;

public class EditorActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor);

        Button añadir = (Button) findViewById(R.id.añadir);
        añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameText = (EditText) findViewById(R.id.ingr_input);
                EditText quantityText = (EditText) findViewById(R.id.quantity_input);
                Spinner measurementText = (Spinner) findViewById(R.id.measurement_input);

                String name = nameText.getText().toString();
                int quantity = Integer.parseInt(quantityText.getText().toString());
                String measurement = measurementText.getSelectedItem().toString();

                insertIngredient(name, quantity, measurement);
                finish();
            }
        });
    }

    private void insertIngredient(String name, int quantity, String measurement) {
        ContentValues values = new ContentValues();

        values.put(IngrSchema.IngredientEntry.COLUMN_INGR_NAME, name);
        values.put(IngrSchema.IngredientEntry.COLUMN_INGR_QUANTITY, quantity);
        values.put(IngrSchema.IngredientEntry.COLUMN_INGR_MEASUREMENT, measurement);

        Uri newUri = getContentResolver().insert(IngrSchema.IngredientEntry.CONTENT_URI, values);
    }
}
