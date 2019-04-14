package com.example.ingrdatabase.Database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class IngrSchema {

    public static final String CONTENT_AUTHORITY = "com.example.ingrdatabase.Database";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_INGR = "ingredients";
    public static final String PATH_INGR_ID = "ingredients/#";

    public IngrSchema() {}

    public static abstract class IngredientEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INGR);

        public static final String TABLE_NAME = "ingredients";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_INGR_NAME = "name";
        public static final String COLUMN_INGR_QUANTITY = "quantity";
        public static final String COLUMN_INGR_MEASUREMENT = "measurement";

        /**
         * Possible values for the unit od measurement.
         */
        public static final String MEASURE_UNITS = "units";
        public static final String MEASURE_GRAMS = "grams";
        public static final String MEASURE_KGRAMS = "kilograms";
        public static final String MEASURE_NONE = "- - - -";

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INGR;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INGR;

    }
}

