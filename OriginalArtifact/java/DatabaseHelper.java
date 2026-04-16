package com.cs360.inventoryappdelarosa;
// here is the logic for the SQLite database for tables: users and items
// CRUD logic here as well

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Variables for user table, table name:USER_DATA, Columns for id, email, and password
    private static final String DATABASE_NAME = "Inventory_RECORD";
    private static final String TABLE_NAME = "USER_DATA";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "EMAIL";
    private static final String COL_3= "PASSWORD";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    // create table for users, with auto incremented primary key that is the id, with email and password
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,  EMAIL TEXT, PASSWORD TEXT)";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If user exists prevent creation of a new table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db); // pass the database data

    }

    // method to register user
    public boolean registerUser(String email , String password) {
        // create instance of db
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues  values = new ContentValues();

        values.put(COL_2 , email);
        values.put(COL_3 , password);

        long result = db.insert(TABLE_NAME , null  , values);
        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    // build/ bind the user name and login
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        // return id
        String [] columns = { COL_1 };
        //selection constraints for email and password
        String selection =  COL_2 + "=?" + " and " + COL_3 + "=?";
        //placeholders
        String [] selectionArgs = { email, password };

        Cursor cursor = db.query(
                TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // check if cursor has data and close cursor and db
        int count = cursor.getCount();
        db.close();
        cursor.close();


        if (count > 0) {
            return true;
        }
        else {
            return false;
        }
    }
}

