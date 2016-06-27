package com.example.kacper.zaliczenie.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.kacper.zaliczenie.Models.Person;

import java.util.Vector;

import static com.example.kacper.zaliczenie.Helpers.DbConstants.*;

/**
 * Created by Kacper on 18-Jun-16.
 */
public class FavouritesDatabase {
    private DatabaseHelper databaseHelper;

    public FavouritesDatabase(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    public boolean addPerson(Person person) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        boolean result = false;

        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, person.getUuid());
            values.put(COLUMN_USERID, person.getUserId());
            values.put(COLUMN_PHOTOURL, person.getPhotoUrl());
            values.put(COLUMN_NAME, person.getName());
            values.put(COLUMN_LOCATION, person.getLocation());
            values.put(COLUMN_FACEBOOK, person.getFacebook());
            values.put(COLUMN_TWITTER, person.getTwitter());
            values.put(COLUMN_GOOGLE, person.getGoogle());
            values.put(COLUMN_GITHUB, person.getGithub());
            values.put(COLUMN_WEBSITE, person.getWebsite());

            db.insert(TABLE2_NAME, null, values);
            result = true;
        } catch (Exception e) {
            result = false;
        } finally {
            db.close();
            return result;
        }
    }

    public boolean chceckIfPersonExists(Person person) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        try {
            String[] projection = {COLUMN_ID, COLUMN_USERID, COLUMN_PHOTOURL, COLUMN_NAME, COLUMN_LOCATION, COLUMN_FACEBOOK, COLUMN_TWITTER, COLUMN_GOOGLE, COLUMN_GITHUB, COLUMN_WEBSITE};

            Cursor cursor = db.query(TABLE2_NAME,  // The table to query
                    projection,                   // The columns to return
                    COLUMN_NAME + "=? and " + COLUMN_FACEBOOK + "=? and " + COLUMN_USERID + "=?",                         // The columns for the WHERE clause
                    new String[]{person.getName(), person.getFacebook(), person.getUserId()},                         // The values for the WHERE clause
                    null,                         // don't group the rows
                    null,                         // don't filter by row groups
                    null                     // The sort order
            );

            if (getPersonFromCursor(cursor) != null)
                return true;
            else
                return false;
        } catch (Exception e) {
            return false;
        } finally {
            db.close();
        }

    }

    public boolean removePerson(String uuid) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        boolean result = false;

        try {
            db.delete(TABLE2_NAME, COLUMN_ID + "=?", new String[]{uuid});
            result = true;
        } catch (Exception e) {
            result = false;
        } finally {
            db.close();
            return result;
        }

    }

    public Vector<Person> getPersons(String userId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Vector<Person> persons = new Vector<Person>();

        try {
            String[] projection = {COLUMN_ID};

            Cursor cursor = db.query(TABLE2_NAME,  // The table to query
                    projection,                   // The columns to return
                    COLUMN_USERID + "=?",                         // The columns for the WHERE clause
                    new String[]{userId},                         // The values for the WHERE clause
                    null,                         // don't group the rows
                    null,                         // don't filter by row groups
                    null                     // The sort order
            );

            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {
                persons.add(getPerson(cursor.getString(0)));
                cursor.moveToNext();
            }

            return persons;
        } catch (Exception e) {
            return null;
        } finally {
            db.close();
        }

    }

    public Person getPerson(String uuid) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        try {
            String[] projection = {COLUMN_ID, COLUMN_USERID, COLUMN_PHOTOURL, COLUMN_NAME, COLUMN_LOCATION, COLUMN_FACEBOOK, COLUMN_TWITTER, COLUMN_GOOGLE, COLUMN_GITHUB, COLUMN_WEBSITE};

            Cursor cursor = db.query(TABLE2_NAME,  // The table to query
                    projection,                   // The columns to return
                    COLUMN_ID + "=?",                         // The columns for the WHERE clause
                    new String[]{uuid},                         // The values for the WHERE clause
                    null,                         // don't group the rows
                    null,                         // don't filter by row groups
                    null                     // The sort order
            );

            return getPersonFromCursor(cursor);
        } catch (Exception e) {
            return null;
        } finally {
            db.close();
        }
    }

    private Person getPersonFromCursor(Cursor cursor) {
        try {

            cursor.moveToFirst();

            Person tempPerson = new Person();

            tempPerson.setUuid(cursor.getString(0));
            tempPerson.setUserId(cursor.getString(1));
            tempPerson.setPhotoUrl(cursor.getString(2));
            tempPerson.setName(cursor.getString(3));
            tempPerson.setLocation(cursor.getString(4));
            tempPerson.setFacebook(cursor.getString(5));
            tempPerson.setTwitter(cursor.getString(6));
            tempPerson.setGoogle(cursor.getString(7));
            tempPerson.setGithub(cursor.getString(8));
            tempPerson.setWebsite(cursor.getString(9));

            cursor.close();

            return tempPerson;
        } catch (Exception e) {
            return null;
        } finally {
            cursor.close();
        }
    }
}
