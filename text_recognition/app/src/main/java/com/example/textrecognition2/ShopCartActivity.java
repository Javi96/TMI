package com.example.textrecognition2;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.textrecognition2.R;
import com.example.textrecognition2.domain.FoodRepository;
import com.example.textrecognition2.domain.IngrCant;

import java.util.ArrayList;

public class ShopCartActivity extends AppCompatActivity {

    private TextView title;

    private LinearLayout layout;


    private TextView IngrText(Context context, final LinearLayout auxLay){
        TextView resul = new TextView(context);
        auxLay.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                layout.removeView(auxLay);
                return true;
            }
            });
        resul.setTypeface(Typeface.createFromAsset(getAssets(), "lato_bold.ttf"));
        resul.setTextColor(getResources().getColor(R.color.colorBlue));
        resul.setTextSize(16);
        return resul;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_cart);

        title = findViewById(R.id.cmp_tile_title);
        title.setText("Shopping\ncart");

        layout = findViewById(R.id.act_shop_cart_layout);

        if(layout != null){

            StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

            ArrayList<IngrCant> lista =  new FoodRepository(this.getApplication()).getShoppingList(this.getApplication());

            StrictMode.setThreadPolicy(old);

            for(IngrCant ing : lista){
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(128,0,32,0);
                LinearLayout sub_layout = new LinearLayout(getApplicationContext());
                sub_layout.setLayoutParams(params);
                sub_layout.setOrientation(LinearLayout.VERTICAL);

                TextView text1 = IngrText(getApplicationContext(), sub_layout);
                text1.setText(ing.getNombre());

                TextView text2 = IngrText(getApplicationContext(), sub_layout);
                text2.setGravity(Gravity.END);
                text2.setText( Integer.toString(ing.getQuantity()));

                sub_layout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card));
                sub_layout.addView(text1);
                sub_layout.addView(text2);

                layout.addView(sub_layout);
            }
        }
    }

}
