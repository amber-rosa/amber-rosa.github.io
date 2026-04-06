/*
* Class to handle sorting feature. A default spinner is set up to display a drop-down menu of sort by A-Z, low-high, high-low
methods for sorting will also be displayed here sortAlpha, sortLowHigh, sortHighLow
method to handle updates for sorting type, sortUpdate() with the use of switch to handle each case (case = sort option)
Assignment: CS499 Computer Science Capstone: Enhancement two, datasets and algorithms
Student: Amber De La Rosa
Recent code update: 3/26/2026
*/

package com.cs360.inventoryappdelarosa;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Comparator;

// class to handle sorting inventory items
public class InventorySort {
    // create a method to set up the spinner that will be called in the ActivityInventory class.
    // This sort feature will sort items from A-Z, quantity low-high, quantity high-low
    public static void spinnerSortItem(Context context, Spinner spinner) {
        // create an ArrayAdapter using string array of sorting names and default spinner layout (from sorting_options in strings.xml)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                context,
                R.array.sorting_option,
                android.R.layout.simple_spinner_item
        );

        // specify layout when the list of  sorting choices appear as a dropdown
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    // method for sorting update for different cases to sort the recyclerView_list either A-Z, quantity low-high, quantity high-low
    public static void sortUpdate(int position, ArrayList<RecyclerView_list> itemList) {
        // if sort by (<item 1> at index 0 is clicked (the Sort by item of the spinner), do nothing, or if itemList is null to prevent crashing if no object is present
        if (position == 0 || itemList == null) {
            return;
        }
        // create switch statement to process sort selection
        switch (position) {
            // case 1 sort item list alphabetically, A-Z
            case 1:
                sortAlpha(itemList);
                break;

            // case 2: sort quantity from low to high, low-high
            case 2:
                sortLowHigh(itemList);
                break;

            // case 3: sort quantity from high to low, high-low
            case 3:
                sortHighLow(itemList);
                break;
        }
    }
    // create method to sort alphabetically using collection sort to compare strings one by one and sort based on ordering principles
    public static void sortAlpha(ArrayList<RecyclerView_list> itemList) {
        itemList.sort(Comparator.comparing(RecyclerView_list::getName));
    }

    // create a method to sort quantity from low to high, a->b. Comparison is used to compare quantity values of a to b swapping lower quantity value to display first
    public static void sortLowHigh(ArrayList<RecyclerView_list> itemList) {
      itemList.sort(Comparator.comparingInt(RecyclerView_list::getQuantity));

    }
    // create a method to sort quantity from high to low, b->a. Comparison is used to compare quantity values of b to a swapping the higher quantity value to display first
    public static void sortHighLow(ArrayList<RecyclerView_list> itemList) {
        itemList.sort((a, b) -> Integer.compare(b.getQuantity(), a.getQuantity()));

    }
}