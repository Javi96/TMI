package com.example.ingrdatabase.Database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.ingrdatabase.Database.IngrSchema.IngredientEntry;

/**
 * {@link ContentProvider} for Pets app.
 */
public class IngrManager extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = com.example.ingrdatabase.Database.IngrManager.class.getSimpleName();
    /** Database helper that will provide us access to the database */
    private IngrDatabase ingredientsDB;
    /** URI matcher code for the content URI for the pets table */
    private static final int INGR = 100;
    /** URI matcher code for the content URI for a single pet in the pets table */
    private static final int INGR_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        sUriMatcher.addURI(IngrSchema.CONTENT_AUTHORITY, IngrSchema.PATH_INGR, INGR);
        sUriMatcher.addURI(IngrSchema.CONTENT_AUTHORITY, IngrSchema.PATH_INGR_ID, INGR_ID);
    }

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        ingredientsDB = new IngrDatabase(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = ingredientsDB.getReadableDatabase();
        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case INGR:
                cursor = database.query(IngredientEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case INGR_ID:
                selection = IngredientEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(IngredientEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INGR:
                return insertIngr(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert an ingredient into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertIngr(Uri uri, ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString(IngredientEntry.COLUMN_INGR_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Ingredient requires a name");
        }

        // Check that the gender is not null
        String measurement = values.getAsString(IngredientEntry.COLUMN_INGR_MEASUREMENT);
        Integer quantity = values.getAsInteger(IngredientEntry.COLUMN_INGR_QUANTITY);
        if (measurement == null) {
            throw new IllegalArgumentException("Ingredient requieres a measurement");
        } else {
            if (quantity == null) {
                throw new IllegalArgumentException("Ingredient requieres a quantity");
            } else {
                if (measurement != "none" && quantity < 1) {
                    throw new IllegalArgumentException("Ingredient requieres a quantity");
                }
            }
        }

        // Get writeable database
        SQLiteDatabase database = ingredientsDB.getWritableDatabase();
        // Insert the new pet with the given values
        long id = database.insert(IngredientEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INGR:
                return updateIngredient(uri, contentValues, selection, selectionArgs);
            case INGR_ID:
                // For the INGR_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = IngredientEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateIngredient(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update pets in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more pets).
     * Return the number of rows that were successfully updated.
     */
    private int updateIngredient(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Check that the name is not null
        if (values.containsKey(IngredientEntry.COLUMN_INGR_NAME)) {
            String name = values.getAsString(IngredientEntry.COLUMN_INGR_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }

        // Check that the gender is not null
        if (values.containsKey(IngredientEntry.COLUMN_INGR_QUANTITY)) {
            Integer quantity = values.getAsInteger(IngredientEntry.COLUMN_INGR_QUANTITY);
            if (quantity == null) {
                throw new IllegalArgumentException("Ingredient requieres a quantity");
            } else {
                if (quantity < 1) {
                    throw new IllegalArgumentException("Ingredient requieres a quantity");
                }
            }
        }

        // Check that the weight is not null and at least 0
        if (values.containsKey(IngredientEntry.COLUMN_INGR_MEASUREMENT)) {
            String measurement = values.getAsString(IngredientEntry.COLUMN_INGR_MEASUREMENT);
            if (measurement == null) {
                throw new IllegalArgumentException("Pet weight must be at least 0");
            }
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = ingredientsDB.getWritableDatabase();
        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(IngredientEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = ingredientsDB.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case INGR:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(IngredientEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case INGR_ID:
                // Delete a single row given by the ID in the URI
                selection = IngredientEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(IngredientEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INGR:
                return IngredientEntry.CONTENT_LIST_TYPE;
            case INGR_ID:
                return IngredientEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}