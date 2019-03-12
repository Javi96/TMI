package com.example.dandelion_test;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);

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

    public String generaURL(String text) {
        final String apiKey = "";//PONER LA CREDENCIAL ASOCIADA A TU CUENTA
        final String endpoint = "https://api.dandelion.eu/datatxt/nex/v1/?";

        String textURL = "text=";
        String topEntitiesURL= "top_entities=10";
        String includeURL = "include=types%2Ccategories";

        String token = "token="+apiKey;

        String[] splitText = text.split(" ");
        for (int i = 0; i < splitText.length-1; i++) {
            textURL = textURL + splitText[i] + "+";
        }
        textURL = textURL +  splitText[splitText.length-1];

        return  endpoint+textURL+"&"+topEntitiesURL+"&"+includeURL+"&"+token;
    }
}
