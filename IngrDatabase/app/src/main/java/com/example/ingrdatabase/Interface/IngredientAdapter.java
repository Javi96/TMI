package com.example.ingrdatabase.Interface;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.ingrdatabase.R;
import com.example.ingrdatabase.Database.IngrSchema.IngredientEntry;

/**
 * {@link IngredientAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */
public class IngredientAdapter extends CursorAdapter {

    public IngredientAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        TextView measurementTextView = (TextView) view.findViewById(R.id.measurement);

        // Find the columns of pet attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGR_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGR_QUANTITY);
        int measurementColumnIndex = cursor.getColumnIndex(IngredientEntry.COLUMN_INGR_MEASUREMENT);

        // Read the pet attributes from the Cursor for the current pet
        String ingrName = cursor.getString(nameColumnIndex);
        Integer ingrQuantity = cursor.getInt(quantityColumnIndex);
        String ingrMeasurement = cursor.getString(measurementColumnIndex);

        // If the pet breed is empty string or null, then use some default text
        // that says "Unknown breed", so the TextView isn't blank.
        if (TextUtils.isEmpty(ingrMeasurement)) {
            ingrMeasurement = "- - - -";
            ingrQuantity = 1;
        }

        // Update the TextViews with the attributes for the current pet
        nameTextView.setText(ingrName);
        quantityTextView.setText(ingrQuantity.toString());
        measurementTextView.setText(ingrMeasurement);
    }
}