package com.cs360.inventoryappdelarosa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // declare input fields and buttons of sign in page, and database helper
    private EditText userEmail, userPassword;
    private DatabaseHelper myDB;
    private Button signUpButton, signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find variables by id and initialize
        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.userPassword);

        signUpButton = findViewById(R.id.signUpButton);
        signInButton = findViewById(R.id.signInButton);

        myDB = new DatabaseHelper(this);

        // call methods for inserting new user or login user
        insertUser();
        loginUser();

    }
    // create method to insert new user record into database
    // utilize onclick listener to call action to save user data
    private void insertUser() {
        signUpButton.setOnClickListener(v -> {
            String email = userEmail.getText().toString().trim();
            String password = userPassword.getText().toString().trim();

            //check if username exists
           if (myDB.checkUser(email, password)) {
               Toast.makeText(MainActivity.this, "Account already exists", Toast.LENGTH_SHORT).show();
           }
           else { // register new user
               boolean register = myDB.registerUser(email, password);
               if (register) {
                   Toast.makeText(MainActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                   startActivity(new Intent(MainActivity.this, ActivityInventory.class)); //navigate to inventory screen
                   finish(); // close log in screen
               }
               else {
                   Toast.makeText(MainActivity.this, "Registration error", Toast.LENGTH_SHORT).show();
               }
           }
        });
    }
        // create method to login current user from the database
        // utilize onclick listener to call action to access user data
        private void loginUser() {
            signInButton.setOnClickListener(v -> {
                String email = userEmail.getText().toString().trim();
                String password = userPassword.getText().toString().trim();

                boolean login = myDB.checkUser(email, password);

                if (login) {
                    Toast.makeText(MainActivity.this, "Login in successful", Toast.LENGTH_SHORT).show();
                    // navigate to activity inventory (inventory page)
                    startActivity(new Intent(MainActivity.this, ActivityInventory.class)); // navigate to Inventory screen
                    finish(); // close log in screen
                } else {
                    Toast.makeText(MainActivity.this, "Log in error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    /* References
    *  codingstuff070. (2020, September 19). Login and signUp app using SqliteDatabase (Android Studio 2020) [Video]. YouTube. https://youtu.be/bpRpQTykGfg?si=6wKBLOkb8qJN_naL
    * */