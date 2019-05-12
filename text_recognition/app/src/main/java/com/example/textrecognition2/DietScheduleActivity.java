package com.example.textrecognition2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.textrecognition2.domain.FoodRepository;
import com.example.textrecognition2.domain.IngrCant;
import com.google.android.gms.vision.text.Line;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;


/**
 * <h1>Actividad que muestra la dieta del usuario</h1>
 */
public class DietScheduleActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Atributos privados
     */
    public static final String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    /**
     * Genera un TextView configurado para los dias
     * @param context Contexto de la aplicacion
     * @return Devuelve un TextView configurado
     */
    private TextView DayText(Context context){
        TextView resul = new TextView(context);
        resul.setTextSize(28);
        resul.setTypeface(Typeface.createFromAsset(getAssets(), "ultra.ttf"));
        resul.setTextColor(getResources().getColor(R.color.colorRed));
        return resul;
    }

    /**
     * Genera un TextView configurado para los platos
     * @param context Contexto de la aplicacion
     * @return Devuelve un TextView configurado
     */
    private TextView PlateText(Context context){
        TextView resul = new TextView(context);
        resul.setTextSize(22);
        resul.setTypeface(Typeface.createFromAsset(getAssets(), "ultra.ttf"));
        resul.setTextColor(getResources().getColor(R.color.colorBlue));
        return resul;
    }

    /**
     * Genera un TextView configurado para los ingredientes
     * @param context Contexto de la aplicacion
     * @return Devuelve un TextView configurado
     */
    private TextView IngrText(Context context){
        TextView resul = new TextView(context);
        resul.setTextSize(18);
        resul.setTypeface(Typeface.createFromAsset(getAssets(), "lato_bold.ttf"));
        resul.setTextColor(getResources().getColor(R.color.colorBlue));
        return resul;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_schedule);

        // cargamos los datos del alm interno y configuramos los ocmponentes de las vistas
        TextView textView = findViewById(R.id.cmp_tile_title);
        textView.setText("Diet schedule");

        SharedPreferences prefs =
                getSharedPreferences("menus", Context.MODE_PRIVATE);

        LinearLayout main_layout =  findViewById(R.id.act_diet_sched_layout);

        TextView child;

        StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        FoodRepository fr = new FoodRepository(this.getApplication());

        for(String day: days){
            if(!prefs.contains(day))
                continue;
            String data = prefs.getString(day, "");
            if (data.isEmpty())
                continue;

            LinearLayout layout =  new LinearLayout(getApplicationContext());
            layout.setOrientation(LinearLayout.VERTICAL);

            child = DayText(getApplicationContext());
            child.setText(day);

            layout.addView(child);

            String[] plates = data.split("_");

            for(int i=0; i < plates.length; i++) {
                child = PlateText(getApplicationContext());
                child.setText("\t" + plates[i]);
                layout.addView(child);
                for( IngrCant ing :  fr.getIngredientsForPlate(plates[i]) ) {
                    child = IngrText(getApplicationContext());
                    child.setText("\t\t" + ing.toString());
                    layout.addView(child);
                }
            }
            main_layout.addView(layout);
            Log.e(day, data);

        }
        StrictMode.setThreadPolicy(old);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
}
