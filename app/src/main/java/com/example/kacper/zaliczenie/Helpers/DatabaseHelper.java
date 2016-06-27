package com.example.kacper.zaliczenie.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.kacper.zaliczenie.Helpers.DbConstants.*;

/**
 * Created by Kacper on 18-Jun-16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String USERS_CREATE =
            "CREATE TABLE " + TABLE1_NAME + " (" +
                    COLUMN_ID + " TEXT, " +
                    COLUMN_USERNAME + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT);";

    private static final String FAVOURITES_CREATE =
            "CREATE TABLE " + TABLE2_NAME + " (" +
                    COLUMN_ID + " TEXT, " +
                    COLUMN_USERID + " TEXT, " +
                    COLUMN_PHOTOURL + " TEXT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_LOCATION + " TEXT," +
                    COLUMN_FACEBOOK + " TEXT," +
                    COLUMN_TWITTER + " TEXT," +
                    COLUMN_GOOGLE + " TEXT," +
                    COLUMN_GITHUB + " TEXT," +
                    COLUMN_WEBSITE + " TEXT);";

    private static final String SQL_DELETE_USERS_ENTRIES = "DROP TABLE IF EXISTS " + TABLE1_NAME;
    private static final String SQL_DELETE_FAVOURITES_ENTRIES = "DROP TABLE IF EXISTS " + TABLE2_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USERS_CREATE);
        db.execSQL(FAVOURITES_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_USERS_ENTRIES);
        db.execSQL(SQL_DELETE_FAVOURITES_ENTRIES);
    }

}
