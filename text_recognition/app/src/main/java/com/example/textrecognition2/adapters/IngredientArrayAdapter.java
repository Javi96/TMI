package com.example.textrecognition2.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.textrecognition2.R;
import com.example.textrecognition2.domain.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientArrayAdapter extends ArrayAdapter<Ingredient> {

    private Context context;
    private List<Ingredient> ingredients;

    //constructor, call on creation
    public IngredientArrayAdapter(Context context, int resource, ArrayList<Ingredient> objects) {
        super(context, resource, objects);

        this.context = context;
        this.ingredients = objects;
    }

    //called when rendering the list
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the property we are displaying
        Ingredient ingredient = ingredients.get(position);

        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.component_card_ingredient, null);

        TextView ingredientName = view.findViewById(R.id.component_ingredient_name);

        String name = ingredient.getName();
        ingredientName.setText(name);

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        view.startAnimation(animation);
        return view;
    }
}
