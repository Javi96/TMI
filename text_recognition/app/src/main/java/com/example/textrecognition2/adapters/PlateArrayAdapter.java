package com.example.textrecognition2.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Animatable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.textrecognition2.R;
import com.example.textrecognition2.domain.Plate;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

        ArrayList<String> ingredients = plate.getIngredients();
        plateName.setText(StringUtils.capitalize( plate.getName()) );
        StringBuilder sb = new StringBuilder();
        for (String str : ingredients)
            sb.append(str+'\n');
        sb.deleteCharAt(sb.length()-1);
        ingredient1.setText(sb.toString());

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        view.startAnimation(animation);
        return view;
    }
}
