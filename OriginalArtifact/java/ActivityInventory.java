package com.cs360.inventoryappdelarosa;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ActivityInventory extends AppCompatActivity {
    // array list for card items
    ArrayList<RecyclerView_list> recyclerViewlist;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inventory);

        // set grid view through, display 2 columns
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        recyclerViewlist = new ArrayList<>();
        // TODO: add items to list.this will be from SQLite database

        // test RecyclerView by initializing list items
        recyclerViewlist = new ArrayList<>();
        recyclerViewlist.add(new RecyclerView_list("Test Item 1"));
        recyclerViewlist.add(new RecyclerView_list("Test Item 2"));

        // set adapter
        RecyclerView_adapter recyclerView_adapter  = new RecyclerView_adapter(recyclerViewlist, this);
        recyclerView.setAdapter(recyclerView_adapter);



    }
}