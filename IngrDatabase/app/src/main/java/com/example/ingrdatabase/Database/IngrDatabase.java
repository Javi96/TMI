package com.example.ingrdatabase.Database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.ingrdatabase.Database.IngrSchema.IngredientEntry;

public class IngrDatabase extends SQLiteOpenHelper {
    public static final String LOG_TAG = com.example.ingrdatabase.Database.IngrDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "ingredientsDB.db";
    private static final int DATABASE_VERSION = 1;
    private ContentResolver myCR;

    public IngrDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        myCR = context.getContentResolver();
    }

    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the ingredients table
        String SQL_CREATE_INGREDIENTS_TABLE =  "CREATE TABLE " + IngredientEntry.TABLE_NAME + " ("
                + IngredientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + IngredientEntry.COLUMN_INGR_NAME + " TEXT NOT NULL, "
                + IngredientEntry.COLUMN_INGR_QUANTITY + " INTEGER NOT NULL, "
                + IngredientEntry.COLUMN_INGR_MEASUREMENT + " TEXT NOT NULL);";
        // Execute the SQL statement
        db.execSQL(SQL_CREATE_INGREDIENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
