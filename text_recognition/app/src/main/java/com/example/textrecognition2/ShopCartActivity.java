package com.example.textrecognition2;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.textrecognition2.domain.FoodRepository;
import com.example.textrecognition2.domain.IngrCant;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * <h1>Esta actividad se encarga de mostrar la lista de la compra</h1>
 * Consta de un scroll con toda la informacion de la lista de la compra
 * la cual se calcula de forma dinámica desde el almacenamiento interno
 */
public class ShopCartActivity extends AppCompatActivity {

    /**
     * Atributos privados
     */
    private LinearLayout layout;


    /**
     * Configura un objeto TextView
     * @param context context de la aplicacion
     * @param auxLay layout padre del componente
     * @return objeto de la clase TextView configurado
     */
    private TextView IngrText(Context context, final LinearLayout auxLay){
        TextView resul = new TextView(context);
        auxLay.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                layout.removeView(auxLay);
                Toasty.success(getApplicationContext(), "Sucessfully removed" , Toasty.LENGTH_SHORT).show();
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

        TextView title = findViewById(R.id.cmp_tile_title);
        title.setText(getString(R.string.shop_cart));

        layout = findViewById(R.id.act_shop_cart_layout);

        if(layout != null){
            /*
            Ejecutamos el procesamiento de datos sobre un hilo externo
             */
            StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

            ArrayList<IngrCant> lista =  new FoodRepository(this.getApplication()).getShoppingList(this.getApplication());

            StrictMode.setThreadPolicy(old);

            for(IngrCant ing : lista){
                // configuramos el primer layout sobre el que añadir los demas
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,0,0,0);
                LinearLayout sub_layout = new LinearLayout(getApplicationContext());
                sub_layout.setLayoutParams(params);
                sub_layout.setOrientation(LinearLayout.VERTICAL);

                // configuramos el segundo layout sobre el que añadiremos el texto
                LinearLayout.LayoutParams aux_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                aux_params.setMargins(0,0,32,0);
                LinearLayout aux = new LinearLayout(getApplicationContext());
                aux.setLayoutParams(aux_params);
                aux.setOrientation(LinearLayout.VERTICAL);
                aux.setPadding(100,85,64,0);
                aux.setWeightSum(2);

                // creamos los componentes con texto
                TextView text1 = IngrText(getApplicationContext(), sub_layout);
                text1.setText(ing.getNombre());
                TextView text2 = IngrText(getApplicationContext(), sub_layout);
                text2.setGravity(Gravity.END);
                text2.setText( "Quantity: " +  Integer.toString( ing.getQuantity()) + " " + ing.getUnidades());

                // añadimos cada vista a su correspondiente contenedor
                aux.addView(text1);
                aux.addView(text2);
                aux.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shop_cart_item));
                sub_layout.addView(aux);
                layout.addView(sub_layout);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

}
