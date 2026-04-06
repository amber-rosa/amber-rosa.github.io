/*
Class that allows user to add a new inventory item to the database
* there are two input fields one to add item by name and another to add item quantity
* there is a submit button that adds the new item to the database and once clicked navigates the user back to the inventory screen
* the cancel button allows the user to go back to inventory screen without adding a new item
* Assignment: CS499 Computer Science Capstone
* Student: Amber De La Rosa
* Last edit: 3/20/2026
*/

package com.cs360.inventoryappdelarosa;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.lang.NumberFormatException;


public class ActivityAddItem extends AppCompatActivity {
    //declare input fields and buttons of add new item screen, and database helper
    private EditText name, quantity;
    private Button cancel, submit;
    private DatabaseHelper myDb;

    // set onClick listener for submit new item
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        //find variables by id and initialize of input fields
        name = findViewById(R.id.addItemName);
        quantity = findViewById(R.id.addItemQuantity);

        //find variables by id and initialize of buttons
        cancel = findViewById(R.id.cancelButton);
        submit = findViewById(R.id.submitButton);

        myDb = new DatabaseHelper(this);

        // call method for inserting new item
        insertItem();
        // call method for checking if existing item exist
    }

    // a method to reuse toast messages
    public void showFeedback(String msg) {

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    // create method to insert new item record into database
    // utilize onclick listener to call action to save item data
    private void insertItem() {
        submit.setOnClickListener(v -> {
            String itemName = name.getText().toString().trim();
            String itemQuantity = quantity.getText().toString().trim(); // convert string into integer.

            // don't allow fields to be left blank
            if (itemName.isEmpty() || itemQuantity.isEmpty()) {
                showFeedback("Please complete entire form.");
                return;
            }
            // create variable to convert string quantity into an integer
            int intQuantity;
            // error handling catch exception if user places string verses integer for quantity field
            try {
                intQuantity = Integer.parseInt(itemQuantity);
            }
            catch  (NumberFormatException e) {
                showFeedback("Quantity must be a number");
                return;

            }
            // ensure user is not entering a negative value
            if (intQuantity < 0) {
                showFeedback("Cannot be a negative value.");
                return;
            }
            // convert all letters to lower case to prevent duplicates due to case sensitivity and during sorting. Preventing future bugs.
            String lowerCaseName = itemName.toLowerCase();

            // call itemExists() method to check if item already exists. ENHANCEMENT ONE
            boolean newItem = myDb.itemExists(lowerCaseName);

            // if item exists provide feedback, else insert item into database
            if (newItem) {
                showFeedback("Item already exists.");
            }
            else {
                // insert new item by name and quantity into database

               boolean insertSuccess =  myDb.insertItem(lowerCaseName, intQuantity);
               // if insert was successful provide "item added" feedback
               if (insertSuccess) {
                    showFeedback("Item added.");
                   // close add item screen
                    finish();
                }
               // else provide feedback of error
               else {
                   showFeedback("Error");
               }
            }

        });
        // set onclick listener in the event of a user clicking the cancel button
        cancel.setOnClickListener(v -> {
            finish(); // close add item screen
        });
    }
}