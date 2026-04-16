/*
This is the Activity inventory class where most user interactions occur when utilizing CRUD operations for inventory items
user can log out and return to the login screen. Add new items to the database by clicking add item. Update items by clicking the edit icon button.
Delete items by clicking the trash icon button.
User can sort items by name alphabetically, from quantity low-high, and from quantity high-low. Accomplished by utilizing a spinner feature that has a dropdown menu
once clicked
User can search item through the database, logic for the search view and on click listeners to search for item by name and to close the search view.
Assignment: CS499 Computer Science Capstone: Enhancement 2: algorithms and datasets. Utilizing sorting methods to enhance the user experience and organize
the user interface
Assignment: CS499 Computer Science Capstone: Enhancement 3: Database. Search through database for item by name
manageable ways.
Student: Amber De La Rosa
Recent code updates: 4/3/2026
 */
package com.cs360.inventoryappdelarosa;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Objects;
import com.google.android.material.floatingactionbutton.FloatingActionButton; // import for FAB
import androidx.appcompat.widget.SearchView;

public class ActivityInventory extends AppCompatActivity {
    // instance of database helper
    DatabaseHelper databaseHelper;
    // array list for card items
    ArrayList<RecyclerView_list> recyclerViewlist;
    RecyclerView recyclerView;
    FloatingActionButton fabButton;
    Button logoutBtn;
    // determine sort position state
    int sortPosition = 0;
    Spinner spinner;
    // access UI components search icon (magnifying glass), close (x) search and searchAction (searchView widget)
    ImageButton searchIcon, closeSearch;
    SearchView searchAction;

    // Create a method to handle toast messages to reduce redundant code
    public void showFeedback(String msg) {

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        // initialize views by there ids within the XML files for buttons, recycler view, spinner, and search icon & view
        logoutBtn = findViewById(R.id.logoutButton);
        recyclerView = findViewById(R.id.recyclerView);
        fabButton = findViewById(R.id.fab_button);
        spinner = findViewById(R.id.sorting_spinner);
        searchIcon = findViewById(R.id.searchIcon);
        searchAction = findViewById(R.id.searchAction);
        closeSearch = findViewById(R.id.closeSearch);

        // if user clicks the logout button return them to sign in screen with a "goodbye" toast message
        // set on click listener to allow user to log out, if clicked navigates user to the log in screen
        logoutBtn.setOnClickListener(v -> {
            startActivity(new Intent(ActivityInventory.this, MainActivity.class)); // navigate to Login screen
            showFeedback("Goodbye!");
            finish(); // close add item screen
        });

        // set grid view through, display 1 column at a time
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));


        // initialize the database helper to open the file and hold item values in the list
        databaseHelper = new DatabaseHelper(this);
        ArrayList<RecyclerView_list> items;

        // try to open database and read all items on the list, throw an exception and display error message if the database was not accessed
        try {
            items = databaseHelper.getAllItems();
        } catch (Exception e) {
            Toast.makeText(this, "DB ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
            items = new ArrayList<>();
        }

        // assign items to recyclerview list if not null
        recyclerViewlist = Objects.requireNonNullElseGet(items, ArrayList::new);

        // set adapter to display items list in a grid view
        RecyclerView_adapter recyclerView_adapter = new RecyclerView_adapter(recyclerViewlist, this, databaseHelper);
        recyclerView.setAdapter(recyclerView_adapter);

        // set on click listener to navigate to add item screen once the floatable action button is clicked
        fabButton.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityInventory.this, ActivityAddItem.class); //navigate to add item screen
            startActivity(intent);
        });

        // access the SortInventory class to call the spinner feature to display the default dropdown
        InventorySort.spinnerSortItem(this, spinner);

        // receive an event from the spinner when application opens
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // save current choice as the position, needed when refreshing page triggered by user event
                sortPosition = position;

                // call the applySortAndRefresh method that helps update the adapter and refresh page depending on sort
                applySortAndRefresh();
            }

            // Do nothing if the user is not interacting with the sort feature
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //  Enhancement 3: filter through database
        //  onclick listener to call search method to search database for item, clicking search icon, changes will be made to UI components once search view displays
        searchIcon.setOnClickListener(v -> {
                    // temporarily remove magnifying glass and spinner, make search view and closeSearch (x) visible
                    searchIcon.setVisibility(View.GONE);
                    spinner.setVisibility(View.GONE);
                    searchAction.setVisibility(View.VISIBLE);
                    closeSearch.setVisibility(View.VISIBLE);

                    // expand search view and show keyboard once search bar is touched
                    searchAction.setIconified(false);
                    searchAction.requestFocusFromTouch();
        });

        // set  onclick listener for closeSearch button resetting the list view back to default and hiding previous UI components
        closeSearch.setOnClickListener(vv -> {
            // clear search text and focus, and close search view
            searchAction.setQuery("", false);
            searchAction.clearFocus();
            searchAction.setIconified(true);

            // reset visibility on spinner and magnify image icon, and remove visibility from search view and closeSearch button
            spinner.setVisibility(View.VISIBLE);
            searchIcon.setVisibility(View.VISIBLE);
            searchAction.setVisibility(View.GONE);
            closeSearch.setVisibility(View.GONE);

            // reload default inventory list without the filteredList
            onResume();
        });

        // set searchView query text listener to handle input field for searching item
        searchAction.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // filter items from database instance to return only searched item, call the itemSearch() method from the DatabaseHelper class
            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<RecyclerView_list> filteredList = databaseHelper.itemSearch(newText);
                // clear current list and add the newText (searched item) to a filtered list to display
                recyclerViewlist.clear();
                recyclerViewlist.addAll(filteredList);

                // apply sorting method to keep items in order, though not necessarily needed at this stage of development
                applySortAndRefresh();

                return true;
            }

        });
    }

    @SuppressLint("NotifyDataSetChanged")

    // create a method to apply the sort and refresh the page to prevent having to refresh each sort individually
    public void applySortAndRefresh() {
        // call sortUpdate from InventorySort class, sort the item list based on sort selection. this method holds cases for a-z, lowHigh, and highLow methods
        InventorySort.sortUpdate(sortPosition, recyclerViewlist);

        // prevent null pointer exception and retrieve the adapter to notify and set the change
        if (recyclerView.getAdapter() != null) {
            recyclerView.getAdapter().notifyDataSetChanged();
        }

    }
    // utilize on resume to refresh page once new item is added
    @SuppressLint("NotifyDataSetChanged")

    // refresh data based on user interactions
    @Override
    protected void onResume() {
        super.onResume();
        // create new instance of the item list from recycler view
        ArrayList<RecyclerView_list>items;
        // try and open database to read from list, catch exception if action was not performed and display error message for debugging
        try {
            items = databaseHelper.getAllItems();
        }catch (Exception e) {
            Toast.makeText(this, "DB ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
            // initialize an empty list if database error occurs to prevent NullPointerException
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
        // call applyAndSortRefresh() method to notify adapter of sort changes and update recyclervie to display correctly on grid
        applySortAndRefresh();
    }
}