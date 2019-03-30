package com.example.textrazor_test;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.textrazor.account.AccountManager;
import com.textrazor.account.model.Account;
import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import com.textrazor.TextRazor;
import com.textrazor.annotations.AnalyzedText;
import com.textrazor.annotations.NounPhrase;
import com.textrazor.annotations.Sentence;
import com.textrazor.annotations.Word;
import com.textrazor.annotations.Entity;


import java.util.List;
import java.util.Map;


public class TextRazorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String apiKey = "91065a5181cf6651f6f901569197480e97e7bf4b3e524210d5a1b17a"; //INSERTAR APIKEY

        //Cuando el usuario ha completado el formulario recogemos todos los datos introducidos
        Button enviar = (Button) findViewById(R.id.enviar);
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Leer nombre
                EditText nombreInput = (EditText) v.getRootView().findViewById(R.id.text_input);
                String text = nombreInput.getText().toString();

                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT >= 8)
                {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    try {
                        testAccount(apiKey);
                        analisis(apiKey, text);
                    } catch(AnalysisException  e) {
                        System.out.println("-------- Esto no se puede procesar --------");
                    } catch(NetworkException  e) {
                        System.out.println("-------- Ha petado la conexión --------");
                    }

                    TextView resultados = (TextView) v.getRootView().findViewById(R.id.resultados);
                    resultados.setText(text);
                }
            }
        });
    }

    public static void analisis(String apiKey, String text) throws NetworkException, AnalysisException {

        TextRazor cliente = new TextRazor(apiKey);

        cliente.addExtractor("words");
        cliente.addExtractor("phrases");
        cliente.addExtractor("entities");

        AnalyzedText respuesta = cliente.analyze(text);

        for (Sentence sentence : respuesta.getResponse().getSentences()) {
            for (Word word : sentence.getWords()) {

                System.out.println("----------------");
                System.out.println("Word: " + word.getToken());

                for (Word child : word.getChildren())
                    System.out.println("Child: " + child.getToken());

                for (NounPhrase phrase : word.getNounPhrases())
                    System.out.println("Phrase: " + phrase.getWords());

                for (Entity entity : word.getEntities())
                    System.out.println("Matched Entity: " + entity.getEntityEnglishId());
            }
        }
    }

    public static void testAccount(String apiKey) throws NetworkException, AnalysisException {
        AccountManager manager = new AccountManager(apiKey);

        Account account = manager.getAccount();

        System.out.println("Your current account plan is " + account.getPlan() + ", which includes " + account.getPlanDailyRequestsIncluded() + " daily requests, " + account.getRequestsUsedToday() + " used today");
    }
}
