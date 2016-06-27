package com.example.kacper.zaliczenie.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.kacper.zaliczenie.Models.User;


import static com.example.kacper.zaliczenie.Helpers.DbConstants.*;

/**
 * Created by Kacper on 18-Jun-16.
 */
public class UsersDatabase {
    private DatabaseHelper databaseHelper;

    public UsersDatabase(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    public User addUser(User user) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, user.getUuid());
            values.put(COLUMN_USERNAME, user.getUsername());
            values.put(COLUMN_PASSWORD, user.getPassword());

            db.insert(TABLE1_NAME, null, values);

            return user;
        } catch (Exception e) {
            return null;
        } finally {
            db.close();
        }
    }

    public User getUser(String username) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        try {
            String[] projection = {COLUMN_ID, COLUMN_USERNAME, COLUMN_PASSWORD};

            Cursor cursor = db.query(TABLE1_NAME,  // The table to query
                    projection,                   // The columns to return
                    COLUMN_USERNAME + "=?",                         // The columns for the WHERE clause
                    new String[]{username},                         // The values for the WHERE clause
                    null,                         // don't group the rows
                    null,                         // don't filter by row groups
                    null                     // The sort order
            );

            return getUserFromCursor(cursor);
        } catch (Exception e) {
            return null;
        } finally {
            db.close();
        }
    }

    private User getUserFromCursor(Cursor cursor) {
        try {

            cursor.moveToFirst();

            User tempUser = new User();

            tempUser.setUuid(cursor.getString(0));
            tempUser.setUsername(cursor.getString(1));
            tempUser.setPassword(cursor.getString(2));

            cursor.close();

            return tempUser;
        } catch (Exception e) {
            return null;
        } finally {
            cursor.close();
        }
    }
}
