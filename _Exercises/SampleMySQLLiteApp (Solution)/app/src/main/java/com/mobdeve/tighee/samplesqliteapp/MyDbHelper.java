package com.mobdeve.tighee.samplesqliteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;


public class MyDbHelper extends SQLiteOpenHelper {
    // Our single instance of the class
    public static MyDbHelper instance = null;

    public MyDbHelper(Context context) {
        super(context, DbReferences.DATABASE_NAME, null, DbReferences.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DbReferences.CREATE_TABLE_STATEMENT);
    }

    // Called when a new version of the DB is present; hence, an "upgrade" to a newer version
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DbReferences.DROP_TABLE_STATEMENT);
        onCreate(sqLiteDatabase);
    }

    // Method that ensures that we're only getting one instance of the helper class.
    public static synchronized MyDbHelper getInstance(Context context) {
        if (instance == null)
            instance = new MyDbHelper(context.getApplicationContext());

        return instance;
    }

    // Method that returns an ArrayList of all stored contacts. This method was named with the term
    // "default" because I originally wanted to have different ways to retrieve the data and the
    // "default way I wanted to retrieve the contacts was to first sort the first names, then the
    // last names.
    public ArrayList<Contact> getAllContactsDefault() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor c = database.query(
                DbReferences.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                DbReferences.COLUMN_NAME_LAST_NAME + " ASC, " + DbReferences.COLUMN_NAME_FIRST_NAME + " ASC",
                null
        );
        ArrayList<Contact> contacts = new ArrayList<>();
        while(c.moveToNext()) {
            contacts.add(new Contact(
                    c.getString(c.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_LAST_NAME)),
                    c.getString(c.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_FIRST_NAME)),
                    c.getString(c.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_NUMBER)),
                    Uri.parse(c.getString(c.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_IMAGE_URI))),
                    c.getLong(c.getColumnIndexOrThrow(DbReferences._ID))
            ));
        }
        c.close();
        database.close();

        return contacts;
    }

    // The insert operation, which takes a contact object as a parameter. It also returns the ID of
    // the row so that the Contact can have that properly referenced within itself.
    public synchronized long insertContact(Contact c) {
        SQLiteDatabase database = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DbReferences.COLUMN_NAME_LAST_NAME, c.getLastName());
        values.put(DbReferences.COLUMN_NAME_FIRST_NAME, c.getFirstName());
        values.put(DbReferences.COLUMN_NAME_NUMBER, c.getNumber());
        values.put(DbReferences.COLUMN_NAME_IMAGE_URI, c.getImageUri().toString());

        // The actual insertion operation. As inserting returns the primary key value of the new
        // row, we can use this and return it to whomever is calling so they can be aware of what
        // ID the new contact was referenced with.
        long _id = database.insert(DbReferences.TABLE_NAME, null, values);

        database.close();

        return _id;
    }

    // Performs an UPDATE operation by comparing the old contact with the new contact. This method
    // tries to reduce the length of the update statement by only including attributes that have
    // been changed. If no changed are present, the update statement is simply not called.
    public void updateContact(Contact cOld, Contact cNew) {
        boolean withChanges = false;
        ContentValues values = new ContentValues();

        if(!cNew.getLastName().equals(cOld.getLastName())) {
            values.put(DbReferences.COLUMN_NAME_LAST_NAME, cNew.getLastName());
            withChanges = true;
        }
        if(!cNew.getFirstName().equals(cOld.getFirstName())) {
            values.put(DbReferences.COLUMN_NAME_FIRST_NAME, cNew.getFirstName());
            withChanges = true;
        }
        if(!cNew.getNumber().equals(cOld.getNumber())) {
            values.put(DbReferences.COLUMN_NAME_NUMBER, cNew.getNumber());
            withChanges = true;
        }
        if(!cNew.getImageUri().equals(cOld.getImageUri())) {
            values.put(DbReferences.COLUMN_NAME_IMAGE_URI, cNew.getImageUri().toString());
            withChanges = true;
        }

        if(withChanges) {
            SQLiteDatabase database = this.getWritableDatabase();
            database.update(
                    DbReferences.TABLE_NAME,
                    values,
                    DbReferences._ID + " = ?",
                    new String[]{String.valueOf(cNew.getId())});
            database.close();
        }
    }

    // The delete contact method that takes in a contact object and uses its ID to find and delete
    // the entry.
    public void deleteContact(Contact c) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(
                DbReferences.TABLE_NAME,
                DbReferences._ID + " = ?",
                new String[]{String.valueOf(c.getId())});
        database.close();
    }

    // Our class for holding DB references.
    private final class DbReferences {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "my_database.db";

        private static final String
                TABLE_NAME = "contacts",
                _ID = "id",
                COLUMN_NAME_FIRST_NAME = "first_name",
                COLUMN_NAME_LAST_NAME = "last_name",
                COLUMN_NAME_NUMBER = "number",
                COLUMN_NAME_IMAGE_URI = "image_uri";

        private static final String CREATE_TABLE_STATEMENT =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME_FIRST_NAME + " TEXT, " +
                COLUMN_NAME_LAST_NAME + " TEXT, " +
                COLUMN_NAME_NUMBER + " TEXT, " +
                COLUMN_NAME_IMAGE_URI + " TEXT)";

        private static final String DROP_TABLE_STATEMENT =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
