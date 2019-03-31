package com.example.textrecognition2.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.textrecognition2.R;
import com.example.textrecognition2.domain.Plate;

import java.util.ArrayList;
import java.util.List;

public class PlateArrayAdapter extends ArrayAdapter<Plate> {

    private Context context;
    private List<Plate> plates;

    //constructor, call on creation
    public PlateArrayAdapter(Context context, int resource, ArrayList<Plate> objects) {
        super(context, resource, objects);

        this.context = context;
        this.plates = objects;
    }

    //called when rendering the list
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the property we are displaying
        Plate plate = plates.get(position);

        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.component_card, null);

        TextView plateName = view.findViewById(R.id.component_card_plate_name);
        TextView ingredient1 = view.findViewById(R.id.component_card_text1);
        TextView ingredient2 = view.findViewById(R.id.component_card_text2);
        TextView ingredient3 = view.findViewById(R.id.component_card_text3);
        ArrayList<String> ingredients = plate.getIngredients();
        plateName.setText(plate.getName());
        ingredient1.setText(ingredients.get(0));
        ingredient2.setText(ingredients.get(1));
        ingredient3.setText(ingredients.get(2));

        return view;
    }
}
