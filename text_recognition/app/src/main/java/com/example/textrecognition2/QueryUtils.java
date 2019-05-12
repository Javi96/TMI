package com.example.textrecognition2;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * <h1>Esta clase se encarga de procesar las querys usadas en el reconocimiento de texto</h1>
 * Proporciona varios metodos para convertir un String a una URL y para desglosar
 * la respuesta HTTP a un formato comodo
 */
public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }


    public static ArrayList<String> fetchEarthquakeData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (java.io.IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        ArrayList<String> entities = extractEntities(jsonResponse);

        // Return the {@link Event}
        return entities;
    }

    /**
     * Devuelve una nueva URL del String proporcionado con la informacion de la URL
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (java.net.MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Hace una peticion HTTP a la URL dada y devuelve in String como respuesta
     */
    public static String makeHttpRequest(URL url) throws java.io.IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (java.net.HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(3000 /* milliseconds */);
            urlConnection.setConnectTimeout(4000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (java.io.IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convierte el input en un String que contiene el JSON al completo de la respuesta del servidor
     */
    private static String readFromStream(InputStream inputStream) throws java.io.IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, java.nio.charset.Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Extrae las entidades de la respuesta del servidor
     * @param jsonResponse Strign con la respuesta del servidor
     * @return lista de String con la informacion relevante de la respuesta
     */
    public static ArrayList<String> extractEntities(String jsonResponse) {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<String> entities = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            JSONObject jsonRootObject = new JSONObject(jsonResponse);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray entityArray = jsonRootObject.optJSONArray("annotations");

            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < entityArray.length(); i++){
                JSONObject currentEntity = entityArray.getJSONObject(i);

                String name = currentEntity.getString("spot");
                ArrayList<String> types = new ArrayList<String>();
                ArrayList<String> categories= new ArrayList<String>();
                JSONArray typesURL = currentEntity.optJSONArray("types");
                JSONArray categoriesURL = currentEntity.optJSONArray("categories");
                for (int j=0; j < typesURL.length(); j++)
                    types.add(typesURL.getString(j));
                for (int j=0; j < categoriesURL.length(); j++)
                    categories.add(categoriesURL.getString(j));

                Entity ent = new Entity(name, types, categories);
                entities.add(ent.toString());
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the Entities JSON results", e);
        }

        return entities;
    }
}
