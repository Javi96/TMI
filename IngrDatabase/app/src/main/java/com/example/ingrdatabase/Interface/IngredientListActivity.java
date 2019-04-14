package com.example.ingrdatabase.Interface;


import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.example.ingrdatabase.R;
import com.example.ingrdatabase.Database.IngrSchema.IngredientEntry;

/*
    Esta clase se utiliza para la consulta de todos los streamings contenidos en la base de datos.
    En función de los parámetros introducidos por el usuario se realizará una consulta a la base de datos y se extraerán
    aquellos que reunen los requisitos
*/
public class IngredientListActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int INGREDIENTS_LOADER = 0;
    /** Adapter for the ListView */
    private IngredientAdapter ingrAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory);

        // Find the ListView which will be populated with the ingredient data
        ListView listaIngredientes = (ListView) findViewById(R.id.list);

        // Setup an Adapter to create a list item for each row of pet data in the Cursor.
        // There is no pet data yet (until the loader finishes) so pass in null for the Cursor.
        ingrAdapter = new IngredientAdapter(this, null);
        listaIngredientes.setAdapter(ingrAdapter);
        listaIngredientes.setEmptyView(findViewById(R.id.empty_view));

        // Kick off the loader
        getLoaderManager().initLoader(INGREDIENTS_LOADER, null, this);

        FloatingActionButton añadir = (FloatingActionButton) findViewById(R.id.button);
        añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditorActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                IngredientEntry._ID,
                IngredientEntry.COLUMN_INGR_NAME,
                IngredientEntry.COLUMN_INGR_QUANTITY,
                IngredientEntry.COLUMN_INGR_MEASUREMENT };

        String sortOrder = IngredientEntry.COLUMN_INGR_NAME + " ASC";

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                IngredientEntry.CONTENT_URI,           // Provider content URI to query
                projection,                     // Columns to include in the resulting Cursor
                null,                  // No selection clause
                null,              // No selection arguments
                sortOrder);                // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ingrAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ingrAdapter.swapCursor(null);
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Salida")
                .setMessage("¿Seguro que quieres salir?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }
                }).setNegativeButton("No", null).show();
    }
}
