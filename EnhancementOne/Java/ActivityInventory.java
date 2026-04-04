package com.cs360.inventoryappdelarosa;
// LOGIC FOR SCREEN THAT DISPLAYS INVENTORY ITEMS

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import com.google.android.material.floatingactionbutton.FloatingActionButton; // import for FAB
import android.Manifest;

public class ActivityInventory extends AppCompatActivity {
    // instance of database helper
    DatabaseHelper databaseHelper;
    // array list for card items
    ArrayList<RecyclerView_list> recyclerViewlist;
    RecyclerView recyclerView;
    private FloatingActionButton fabButton;
    private TextView inventoryHeader;
    private Button logoutBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        logoutBtn = findViewById(R.id.logoutButton);
        // set on click listener to allow user to log out, if clicked navigates user to the log in screen
        logoutBtn.setOnClickListener(v -> {
            startActivity(new Intent(ActivityInventory.this, MainActivity.class)); // navigate to Login screen
            Toast.makeText(this, "Goodbye!", Toast.LENGTH_SHORT).show();
            finish(); // close add item screen
        });

        // set grid view through, display 1 column at a time
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        // test items
        recyclerViewlist = new ArrayList<>();
        recyclerViewlist.add(new RecyclerView_list(1, "test item", 1));
        recyclerViewlist.add(new RecyclerView_list(2, "testItem", 5));

        databaseHelper = new DatabaseHelper(this);
        ArrayList<RecyclerView_list> items;
        // call method to read all items.
        try {
            items = databaseHelper.getAllItems();
        }catch (Exception e) {
            Toast.makeText(this, "DB ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
            items = new ArrayList<>();
        }

        // assign items to recyclerview list if not null
        if (items != null) {
            recyclerViewlist = items;
        }
        else {
            recyclerViewlist = new ArrayList<>();
        }

        // set adapter
        RecyclerView_adapter recyclerView_adapter  = new RecyclerView_adapter(recyclerViewlist, this);
        recyclerView.setAdapter(recyclerView_adapter);

        // find button variables by id and initialize
        fabButton = findViewById(R.id.fab_button);

        // set on click listener to navigate to add item screen once the floatable action button is clicked
        fabButton.setOnClickListener(v -> {
            Intent intent = new Intent (ActivityInventory.this, ActivityAddItem.class); //navigate to add item screen
            startActivity(intent);
        });
        // TODO: onclick listener to call sorting methods based on users selection
        // sort A-Z method
        // sort low-high quantity
        // sort high-low quantity

        //  TODO: onclick listener to call search method to search database for item



    }
    // utilize on resume to refresh page once new item is added
    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<RecyclerView_list>items;
        try {
            items = databaseHelper.getAllItems();
        }catch (Exception e) {
            Toast.makeText(this, "DB ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
            items = new ArrayList<>();
        }
        // clear current list to prevent duplicates,
        recyclerViewlist.clear();
        recyclerViewlist.addAll(items);


        // notify the adapter of data change to update recyclerview to display new item to grid
        // ensure value is not null
        if (recyclerView.getAdapter() != null) {
            recyclerView.getAdapter().notifyDataSetChanged();

        }
    }
}