package com.cs360.inventoryappdelarosa;

/* The adapter class bridges the data source and the UI elements of the cardview into a
dynamic grid layout from a recyclerview. It holds the logic for the onclick listeners delete and update
buttons located on the Inventory screen as well as holding the logic for the dialog view to update the quantity
of a given item. the sorting also
Assignment: CS499 Computer Science Capstone: Enhancement 2 implementation
Student: Amber De La Rosa
Recent code updates: 3/26/2026
*/

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RecyclerView_adapter extends RecyclerView.Adapter<RecyclerView_adapter.ViewHolder> {
    // fields to hold the data needed within the UI to display list items, dialog, toast messages, sorting, and changes made to database
    private final ArrayList<RecyclerView_list> recyclerViewlist;
    private final Context context;
    private final DatabaseHelper myDb; // access database

    // constructor to receive list of items, activity context, and the database helper
    public RecyclerView_adapter(ArrayList<RecyclerView_list> recyclerViewlist, Context context, DatabaseHelper db) {
        this.recyclerViewlist = recyclerViewlist;
        this.context = context;
        this.myDb = db; // initialize database
    }

    // set default layout of the gridview, inflate from the xml file for each item
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_card, parent, false);
        return new ViewHolder(view);
    }

    // Bind the data to display each item on its on view card each containing a name and quantity
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecyclerView_list item = recyclerViewlist.get(position);
        holder.textViewCard.setText(item.getName());
        holder.quantityValue.setText(String.valueOf(item.getQuantity()));
    }

    // get the amount of items within the list by returning its size
    @Override
    public int getItemCount() {

        return recyclerViewlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         // define variables for the UI elements
        CardView cardView;
        TextView textViewCard, quantityValue;
        ImageButton delete_button, edit_button;

        // create method to hold toast messages to reduce redundant code
        private void showFeedback(String msg) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }

        // create inner ViewHolder class that will handle edit and delete onclick listeners
        @SuppressLint("NotifyDataSetChanged")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initilialize UI elements by there id
            cardView = itemView.findViewById(R.id.cardView);
            textViewCard = itemView.findViewById(R.id.textViewCard);
            quantityValue = itemView.findViewById(R.id.quantityValue);
            delete_button = itemView.findViewById(R.id.delete_button);
            edit_button = itemView.findViewById(R.id.edit_button);


            // onclick listener to delete item
            delete_button.setOnClickListener(v -> {
                // access position of item on grid view, these will be needed to remove from grid view
                int position = getBindingAdapterPosition();
                // if not in valid position
                if (position != RecyclerView.NO_POSITION) {
                    // set current position
                    RecyclerView_list item = recyclerViewlist.get(position);

                    // delete item from database by id
                    myDb.deleteItem(item.getId());

                    //remove item from adapter and recycler view, and provide feedback
                    recyclerViewlist.remove(position);
                    // notify removal to recycler view
                    notifyItemRemoved(position);
                    // provide feedback to user
                    showFeedback("Item deleted.");
                }
            });
            // onclick listener to update Quantity using a dialog view
            edit_button.setOnClickListener(v -> {
                // access position of item on grid view, these will be needed to remove from grid view
                int position = getBindingAdapterPosition();
                // if not in valid position
                if (position != RecyclerView.NO_POSITION) {
                    // set current position
                    RecyclerView_list item = recyclerViewlist.get(position);

                    // inflate update layout to display a dialog view
                    View dialogView = LayoutInflater.from(context).inflate(R.layout.activity_update_quantity, null);

                    // input field for updating quantity with a cancel and an update button
                    EditText quantityInput = dialogView.findViewById(R.id.updateQuantity);
                    Button cancelBtn = dialogView.findViewById(R.id.buttonCancel);
                    Button updateBtn = dialogView.findViewById(R.id.buttonUpdate);

                    // show current quantity
                    quantityInput.setText(String.valueOf(item.getQuantity()));

                    // create and set the view
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setView(dialogView)
                            .create();

                    // if cancel button is clicked close dialog view
                    cancelBtn.setOnClickListener(vv -> dialog.dismiss());

                    // if user clicks update button, set on click listener to update adapter and database
                    updateBtn.setOnClickListener(vv -> {
                        String newQuantity = quantityInput.getText().toString().trim();

                        // don't allow quantity input field to be left blank
                        if (newQuantity.isEmpty()) {
                            showFeedback("Please enter quantity.");
                            return;
                        }
                        // create variable to convert string quantity into an integer
                        int newQty;
                        // error handling catch exception if user places string verses integer for quantity field
                        try {
                            newQty = Integer.parseInt(newQuantity);
                        } catch (NumberFormatException e) {
                            showFeedback("Quantity must be a number");
                            return;

                        }
                        // create a condition to ensure user is not entering a negative value
                        if (newQty < 0) {
                            showFeedback("Cannot be a negative value.");
                            return;
                        }

                        // update Quantity from database by id
                        boolean updated = myDb.updateQuantity(item.getId(), newQty);

                        //if item updated notify adapter, update the local quantity object, and provide feedback,
                        if (updated) {
                            item.setQuantity(newQty);
                            notifyItemChanged(position);

                            // force the list to refresh based on sort option if instance of ActivityInventory where the item quantity needed to be sorted
                            // applySortAndRefresh() method data based on chosen sort option. Part of enchancement two
                            if (context instanceof ActivityInventory){
                                ((ActivityInventory) context).applySortAndRefresh();
                            }
                            showFeedback("Item Updated.");

                            // if new quantity is zero notify user
                            if (newQty == 0) {
                                Toast.makeText(context, item.getName() + " are out of stock.", Toast.LENGTH_SHORT).show();
                            }
                            // close dialog window
                            dialog.dismiss();
                        }
                        // else, quantity failed to update, notify the user
                        else {
                            showFeedback("Update Failed.");
                        }
                    });
                    // display window (dialog view)
                    dialog.show();
                }
            });
        }
    }
}