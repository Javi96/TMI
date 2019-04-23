package com.example.textrecognition2;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class DandelionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dandelion);

        //Cuando el usuario ha completado el formulario recogemos todos los datos introducidos
        Button enviar = (Button) findViewById(R.id.enviar);
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText textInput = (EditText) v.getRootView().findViewById(R.id.text_input);
                String text = textInput.getText().toString();

                String URL = generaURL(text);

                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT >= 8) {
                    /*StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);*/
                    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

                    ArrayList<String> respuesta = QueryUtils.fetchEarthquakeData(URL);

                    String respuestaString = "";
                    for (int i = 0; i < respuesta.size(); i++)
                        respuestaString = respuestaString + respuesta.get(i) + "- - - - - - - - - -\n";

                    TextView resultados = (TextView) v.getRootView().findViewById(R.id.resultados);
                    resultados.setText(respuestaString);
                }
            }
        });
    }

    public static String generaURL(String text) {
        /*
        final String apiKey = "cf93e08cff2a412fb7516667a1640a2f"; //PONER LA CREDENCIAL ASOCIADA A TU CUENTA
        final String endpoint = "https://api.dandelion.eu/datatxt/nex/v1/?";

        String textURL = "text=";
        String topEntitiesURL= "top_entities=10";
        String includeURL = "include=types%2Ccategories";


        String token = "token="+apiKey;

        StringBuilder sb = new StringBuilder(endpoint + textURL);

        /*
        for ( String str : text.split(" "))
            sb.append(str + '+');
        sb.setCharAt(sb.length()-1, '&');
        * /
        sb.append(text.replace( ' ', '+'));




        sb.append('&' + topEntitiesURL + '&' + includeURL + '&' + token );

        /*
        String[] splitText = text.split(" ");
        for (int i = 0; i < splitText.length-1; i++) {
            textURL = textURL + splitText[i] + "+";
        }
        textURL = textURL +  splitText[splitText.length-1];


        return  endpoint+textURL+"&"+topEntitiesURL+"&"+includeURL+"&"+token;
        */

        return "https://api.dandelion.eu/datatxt/nex/v1/?text=" + text.replace( ' ', '+') + "&top_entities=10&lang=es&include=types%2Ccategories&token=cf93e08cff2a412fb7516667a1640a2f";
        //return sb.toString();
    }

}
