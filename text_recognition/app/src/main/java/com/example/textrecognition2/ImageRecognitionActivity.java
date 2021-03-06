package com.example.textrecognition2;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.textrecognition2.domain.FoodRepository;
import com.example.textrecognition2.domain.Ingredient;
import com.example.textrecognition2.utilities.EncodeDecodeUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.marozzi.roundbutton.RoundButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class ImageRecognitionActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String CLOUD_VISION_API_KEY = BuildConfig.API_KEY;
    public static final String FILE_NAME = "temp.jpg";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private static final int MAX_LABEL_RESULTS = 10;
    private static final int MAX_DIMENSION = 1200;

    private static final String TAG = ImageRecognitionActivity.class.getSimpleName();
    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;

    private TextView mImageDetails;         // Mensaje informativo
    private ImageView mMainImage;           // Foto tomada por el usuario
    private TextView title;                 // Nombre de la actividad
    public static JSONObject json;          // Diccionario de ingredientes

    private RoundButton btn;

    private String selectedMeasurement = "units";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_recognition);

        title = findViewById(R.id.cmp_tile_title);
        title.setText("Food\nscanner");

        // Permite al usuario seleccionar una foto de la galería o tomarla con la cámara
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ImageRecognitionActivity.this);
                builder
                        .setMessage(R.string.dialog_select_prompt)
                        .setPositiveButton(R.string.dialog_select_gallery, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ImageRecognitionActivity.this.startGalleryChooser();
                            }
                        })
                        .setNegativeButton(R.string.dialog_select_camera, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ImageRecognitionActivity.this.startCamera();
                            }
                        });
                builder.create().show();
            }
        });

        mImageDetails = findViewById(R.id.image_details);
        mMainImage = findViewById(R.id.main_image);

        String jsonS = null;    // Cargamos el diccionario con los ingredientes
        try {
            InputStream is = getAssets().open("ingredients.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonS = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            json = new JSONObject(jsonS);
        } catch (JSONException e) {}

        System.out.println(jsonS);
    }

    /**
     *  Abre la galería para que el usuario seleccione una imagen
     */
    public void startGalleryChooser() {
        if (PermissionUtils.requestPermission(this, GALLERY_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select a photo"),
                    GALLERY_IMAGE_REQUEST);
        }
    }

    /**
     *  Arranca la cámara a pantalla completa
     */
    public void startCamera() {
        if (PermissionUtils.requestPermission(
                this,
                CAMERA_PERMISSIONS_REQUEST,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
        }
    }

    /**
     * Devuelve la foto recién tomada con la cámara
     * @return foto de la cámara
     */
    public File getCameraFile() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            uploadImage(data.getData());
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            uploadImage(photoUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
                    startCamera();
                }
                break;
            case GALLERY_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, GALLERY_PERMISSIONS_REQUEST, grantResults)) {
                    startGalleryChooser();
                }
                break;
        }
    }

    /**
     * Muestra la imagen en la Activity
     * @param uri foto seleccionada por el usuario
     */
    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                Bitmap bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                                MAX_DIMENSION);

                callCloudVision(bitmap);
                if(mMainImage != null) {
                    mMainImage.setImageBitmap(bitmap);
                    mMainImage.setPadding(24, 24, 24, 24);
                    mMainImage.setBackgroundColor(Color.parseColor("#ffffff"));
                }
            } catch (IOException e) {
                Log.d(TAG, "Image picking failed because " + e.getMessage());
                Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(TAG, "Image picker gave us a null image.");
            Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Prepara la consulta a la API con la imagen
     * @param bitmap foto seleccionada por el usuario
     * @return petición a la API
     * @throws IOException
     */
    private Vision.Images.Annotate prepareAnnotationRequest(final Bitmap bitmap) throws IOException {
        HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        VisionRequestInitializer requestInitializer =
                new VisionRequestInitializer(CLOUD_VISION_API_KEY) {
                    /**
                     * We override this so we can inject important identifying fields into the HTTP
                     * headers. This enables use of a restricted cloud platform API key.
                     */
                    @Override
                    protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                            throws IOException {
                        super.initializeVisionRequest(visionRequest);

                        String packageName = getPackageName();
                        visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                        String sig = PackageManagerUtils.getSignature(getPackageManager(), packageName);

                        visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                    }
                };

        Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
        builder.setVisionRequestInitializer(requestInitializer);

        Vision vision = builder.build();

        BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                new BatchAnnotateImagesRequest();
        batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
            AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

            // Add the image
            Image base64EncodedImage = new Image();
            // Convert the bitmap to a JPEG
            // Just in case it's a format that Android understands but Cloud Vision
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            // Base64 encode the JPEG
            base64EncodedImage.encodeContent(imageBytes);
            annotateImageRequest.setImage(base64EncodedImage);

            // add the features we want
            annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                Feature labelDetection = new Feature();
                labelDetection.setType("LABEL_DETECTION");
                labelDetection.setMaxResults(MAX_LABEL_RESULTS);
                add(labelDetection);
            }});

            // Add the list of one thing to the request
            add(annotateImageRequest);
        }});

        Vision.Images.Annotate annotateRequest =
                vision.images().annotate(batchAnnotateImagesRequest);
        // Due to a bug: requests to Vision API containing large images fail when GZipped.
        annotateRequest.setDisableGZipContent(true);
        Log.d(TAG, "created Cloud Vision request object, sending request");

        LinearLayout counter = findViewById(R.id.quantity); // Oculta los ajustes del ingrediente
        counter.setVisibility(View.INVISIBLE);

        LinearLayout medida = findViewById(R.id.measurement); // Oculta los ajustes del ingrediente
        medida.setVisibility(View.INVISIBLE);

        ((Spinner)findViewById(R.id.measurement_input)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMeasurement = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
            });

        LinearLayout añadir = findViewById(R.id.add_ingr); // Oculta los ajustes del ingrediente
        añadir.setVisibility(View.INVISIBLE);

        btn = findViewById(R.id.add_ingr_button);
        findViewById(R.id.add_ingr_button).setOnClickListener(this);
        return annotateRequest;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_ingr_button:
                SharedPreferences prefs =
                        getSharedPreferences("inventory", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                /*
                PENDIENTE DE TERMINAR LA VISTA
                FALTA UNIDAD DE MEDIDA Y PODER CAMBIAR NOMBRE
                */

                String nombre = ( (TextView) findViewById(R.id.image_details)).getText().toString().substring(9);
                if(nombre.isEmpty()){
                    Toasty.error(getApplicationContext(), "Introduce un nombre válido", Toast.LENGTH_SHORT * 10, true).show();
                }
                String unidades = selectedMeasurement;
                int cantidad = 0;
                try {
                    cantidad = Integer.parseInt( ((EditText)findViewById(R.id.quantity_input)).getText().toString() );
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Toasty.error(getApplicationContext(), "Introduce una cantidad válida", Toast.LENGTH_SHORT * 10, true).show();
                    return;
                }

                StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

                new FoodRepository(this.getApplication()).insertIngredient(new Ingredient(nombre, unidades));

                StrictMode.setThreadPolicy(old);
                if ( unidades.equalsIgnoreCase("uncount"))
                    editor.putInt(nombre, 0);
                else
                    editor.putInt(nombre, cantidad);

                editor.apply();

                btn.startAnimation();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn.setResultState(RoundButton.ResultState.SUCCESS);

                    }
                }, 1614);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences prefs =
                                getSharedPreferences("menus", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("return", "MenuActivity");

                        editor.apply();

                        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }, 2000);
                //*/

                break;
        }
    }

    /**
     * Clase para ejecutar la consulta de detección de etiquetas
     */
    private static class LableDetectionTask extends AsyncTask<Object, Void, String> {
        private final WeakReference<ImageRecognitionActivity> mActivityWeakReference;
        private Vision.Images.Annotate mRequest;

        LableDetectionTask(ImageRecognitionActivity activity, Vision.Images.Annotate annotate) {
            mActivityWeakReference = new WeakReference<>(activity);
            mRequest = annotate;
        }

        @Override
        protected String doInBackground(Object... params) {
            try {
                Log.d(TAG, "created Cloud Vision request object, sending request");
                BatchAnnotateImagesResponse resultado = mRequest.execute();
                List<AnnotateImageResponse> listaRespuestas = resultado.getResponses();
                AnnotateImageResponse bloqueLabels = listaRespuestas.get(0);
                List<EntityAnnotation> listaLabels = bloqueLabels.getLabelAnnotations();

                float solScore = 0;
                String sol = "";

                // Extraemos la etiqueta con mayor puntuación cuyo descriptor figure en el diccionario de ingredientes
                for (int i=0; i < listaLabels.size(); i++) {
                    EntityAnnotation infoLabel = listaLabels.get(i);
                    String label = infoLabel.getDescription().toLowerCase();
                    float score = infoLabel.getScore();

                    if (json.has(label))
                        if (score > solScore) {
                            solScore = score;
                            sol = label;
                        }
                }

                if (sol != "")
                    return "This is: " + sol;
                else
                    return "I can't recognize that photo";

            } catch (GoogleJsonResponseException e) {
                Log.d(TAG, "failed to make API request because " + e.getContent());
            } catch (IOException e) {
                Log.d(TAG, "failed to make API request because of other IOException " +
                        e.getMessage());
            }
            return "Cloud Vision API request failed. Check logs for details.";
        }

        /**
         * Muestra el ingrediente detectado y activa los ajustes
         * @param result etiqueta asignada
         */
        protected void onPostExecute(String result) {
            ImageRecognitionActivity activity = mActivityWeakReference.get();
            if (activity != null && !activity.isFinishing()) {
                TextView imageDetail = activity.findViewById(R.id.image_details);
                imageDetail.setText(result);
                if (result != "I can't recognize that photo") {
                    LinearLayout counter = activity.findViewById(R.id.quantity);
                    counter.setVisibility(View.VISIBLE);
                    LinearLayout medida = activity.findViewById(R.id.measurement);
                    medida.setVisibility(View.VISIBLE);
                    LinearLayout añadir = activity.findViewById(R.id.add_ingr);
                    añadir.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /**
     * Ejecuta la llamada a la API de detección de etiquetas
     * @param bitmap imagen tomada por el usuario
     */
    private void callCloudVision(final Bitmap bitmap) {
        Log.e(TAG, "call CLoud vision ");

        // Switch text to loading
        mImageDetails.setText(R.string.loading_message);

        // Do the real work in an async task, because we need to use the network anyway
        try {
            AsyncTask<Object, Void, String> labelDetectionTask = new LableDetectionTask(this, prepareAnnotationRequest(bitmap));
            labelDetectionTask.execute();
        } catch (IOException e) {
            Log.d(TAG, "failed to make API request because of other IOException " +
                    e.getMessage());
        }
    }

    /**
     * Escala la imagen para mostrarla en la activity
     * @param bitmap imagen seleccionada por el usuario
     * @param maxDimension dimensión máxima
     * @return imagen redimensionada
     */
    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
