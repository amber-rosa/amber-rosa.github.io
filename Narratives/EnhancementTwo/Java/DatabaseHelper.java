/*
This is the database helper class that creates a table to store user login/register data and a table to store inventory items
 * CRUD operations to add new item, update item by quantity, read item from database to the recyclerview, and delete
 * item and method to check if an item exists in the database
 * Assignment: CS499 Computer Science Capstone
 * Student: Amber De La Rosa
 * Recent code updates: 3/20/2026
 */

package com.cs360.inventoryappdelarosa;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    // add constant that defines database name as inventory record
    private static final String DATABASE_NAME = "Inventory_RECORD";
    // Create constants for user table, and columns for user data-id, email, password, to prevent magic numbers and safety
    private static final String UserLoginTable = "USER_DATA";
    private static final String userId = "ID";
    private static final String userEmail = "EMAIL";
    private static final String userPassword = "PASSWORD";

    // constants for inventory table and columns for inventory item data by id, name, and quantity, preventing magic numbers and for safety
    private static final String InventoryItemTable = "INVENTORY_DATA";
    private static final String itemId = "ID";
    private static final String itemName = "NAME";
    private static final String itemQuantity = "QUANTITY";

    // create helper object to open and manage database
    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    // create tables for user and items, with auto incremented primary key that is the id, with  their respective columns
    // user table:email and password inventory table: name and quantity
    public void onCreate(SQLiteDatabase db) {
        String createTableLogin = "CREATE TABLE IF NOT EXISTS " + UserLoginTable  + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,  EMAIL TEXT, PASSWORD TEXT)"; // user table
        String createTableInventory = "CREATE TABLE IF NOT EXISTS " + InventoryItemTable  + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,  NAME TEXT, QUANTITY INTEGER)"; // inventory table

        db.execSQL(createTableLogin);
        db.execSQL(createTableInventory);

    }

    // call onUpgrade when database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase myDb, int oldVersion, int newVersion) {
        // drop existing table
        myDb.execSQL("DROP TABLE IF EXISTS " + UserLoginTable );
        myDb.execSQL("DROP TABLE IF EXISTS " + InventoryItemTable );
        onCreate(myDb); // pass the database data

    }

    // method to register new user
    public boolean registerUser(String email, String password) {
        // create instance of database object to write to the database and ContentValues container to hold user information needed for insertion
        SQLiteDatabase myDb = this.getWritableDatabase();
        ContentValues userValues = new ContentValues();

        // add user email and password into database
        userValues.put(userEmail, email);
        userValues.put(userPassword, password);

        // verify the new user record was stored within the database, return result if found
        long result = myDb.insert(UserLoginTable , null, userValues);
        return result != -1;
    }

    // method to check if the user exists within the database via email database and password
    public boolean checkUser(String email, String password) {
        SQLiteDatabase myDb = this.getReadableDatabase(); // read from database
        // retrieve id from UserLoginTable
        String[] columns = {userId};

        //selection constraints for email and password, filter rows based on login credentials
        String selection = userEmail + "=?" + " and " + userPassword + "=?";

        //placeholders that replace the WHERE  clause (?) for userEmail and userPassword
        String[] selectionArgs = {email, password};

        // run query to iterate by row
        Cursor cursor = myDb.query(
                UserLoginTable ,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // check if user exists, utilize cursor to evaluate the rows and close cursor and database
        int count = cursor.getCount();
        cursor.close();
        myDb.close();

        // check if user exists within the row, if they exist the count will be greater than 0.
        return count > 0;
    }

    //  Create insertItem method to add new item to database
    public boolean insertItem(String name, Integer qty) {
        // get writeable database
        SQLiteDatabase myDb = this.getWritableDatabase();

        // create a container to hold input values of the item
        ContentValues itemValues = new ContentValues();
        itemValues.put(itemName, name);
        itemValues.put(itemQuantity, qty);

        // insert values of the item into database and verify by returning result if item was found
        long result = myDb.insert(InventoryItemTable , null, itemValues);
        myDb.close(); // close database
        return result != -1;
    }

    // create a method that will read all items in the list from the database that the recyclerview can access
    public ArrayList<RecyclerView_list> getAllItems() {
        SQLiteDatabase myDb = this.getReadableDatabase(); // open and read from database

        // use cursor as a pointer for the query Selecting item data from the inventory item table (InventoryItemTable)
        Cursor cursor = myDb.rawQuery("SELECT * FROM " + InventoryItemTable , null);

        // create an array list to store items
        ArrayList<RecyclerView_list> itemList = new ArrayList<>();

        // move cursor into the first position use OrThrow to remove lint warning of returning -1 this will allow an empty list to display if there is not any items
        if (cursor.moveToFirst()) {
            // loop through each item value and retrieve its  id, name, and quantity
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(itemId));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(itemName));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(itemQuantity));

                //add item to list
                itemList.add(new RecyclerView_list(id, name, quantity));

            } while (cursor.moveToNext()); // move cursor to next position while there are more non-empty rows
        }
            cursor.close(); // close cursor
            myDb.close(); // close database
            return itemList; // return array list
    }

    // method to update item quantity by the user id
    public boolean updateQuantity(int id, int newQuantity) {
        // create instance to write to the database
        SQLiteDatabase myDb = this.getWritableDatabase();

        // create container to store quantity value
        ContentValues quantityValues = new ContentValues();

        // set new quantity
        quantityValues.put(itemQuantity, newQuantity);

        // call update method to update database and pass values
        // update the quantity by the id utilizing the WHERE clause of a query to find quantity of the item by its id
        int result = myDb.update(InventoryItemTable , quantityValues, itemId + "=?", new String[] {String.valueOf(id)});

        myDb.close(); // close database
        return result > 0; // return updated item with its new quantity
    }

    // method to delete item from database by user id
    public void deleteItem(int id) {
        // open and write to database
        SQLiteDatabase myDb = this.getWritableDatabase();

        // call method to delete the comparable id
        myDb.delete(InventoryItemTable , itemId + "=?", new String[] {String.valueOf(id)});
        myDb.close(); // close database
    }

    // Create a method to check if item already exists in database: ENHANCEMENT ONE IMPLEMENTATION
    public boolean itemExists(String name) {
        // open and read from database
        SQLiteDatabase myDb = this.getReadableDatabase();
        // retrieve item by its id if an item exists within that row
        String[] columns = {itemId};

        // selection constraint for name to filter row based on input of item, select item where the name = itemName
        String selection = itemName + "=?";

        // placeholders to replace the WHERE clause of item name preventing SQL injection
        String[] selectionArgs = {name};

        // run query to iterate by row
        Cursor cursor = myDb.query(
                InventoryItemTable ,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        // utilize cursor to evaluate the rows, able to read the entire size of the table to indicate a beginning and end of search, close cursor and database
        int count = cursor.getCount();
        cursor.close();
        myDb.close();

        // check if item name exists within row if count is greater than 0
        return count > 0;

    }
    // TODO: add a  searchItem method to search for item within the database. ENHANCEMENT 3

}

