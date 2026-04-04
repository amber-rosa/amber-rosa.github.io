package com.cs360.inventoryappdelarosa;

// -- card view logic for UI

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
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

    private final ArrayList<RecyclerView_list> recyclerViewlist;
    private final Context context;
    private final DatabaseHelper myDb; // access database

    public RecyclerView_adapter(ArrayList<RecyclerView_list> recyclerViewlist, Context context) {
        this.recyclerViewlist = recyclerViewlist;
        this.context = context;
        this.myDb = new DatabaseHelper(context); // initialize database
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecyclerView_list item = recyclerViewlist.get(position);
        holder.textViewCard.setText(item.getName());
        holder.quantityValue.setText(String.valueOf(item.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return recyclerViewlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView; // bring in cardview
        TextView textViewCard, quantityValue;
        ImageButton delete_button, edit_button;

        // create method to hold toast messages to reduce redundant code
        private void showFeedback(String msg) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            textViewCard = itemView.findViewById(R.id.textViewCard);
            quantityValue = itemView.findViewById(R.id.quantityValue);
            delete_button = itemView.findViewById(R.id.delete_button);
            edit_button = itemView.findViewById(R.id.edit_button);


            // onclick listener to remove item
            delete_button.setOnClickListener(v -> {
                // access position of item on grid view, these will be needed to remove from grid view
                int position = getBindingAdapterPosition();
                // if not in valid position
                if (position != RecyclerView.NO_POSITION) {
                    RecyclerView_list item = recyclerViewlist.get(position); // set current position

                    // delete item from database by id
                    myDb.deleteItem(item.getId());

                    //remove item from adapter and recycler view, and provide feedback
                    recyclerViewlist.remove(position); // remove from adapter list
                    notifyItemRemoved(position); // notify removal to recycler view
                    showFeedback("Item deleted.");
                }
            });
            // onclick listener to update Quantity using a dialog view
            edit_button.setOnClickListener(v -> {
                // access position of item on grid view, these will be needed to remove from grid view
                int position = getBindingAdapterPosition();
                // if not in valid position
                if (position != RecyclerView.NO_POSITION) {
                    RecyclerView_list item = recyclerViewlist.get(position); // set current position

                    // inflate update layout
                    View dialogView = LayoutInflater.from(context).inflate(R.layout.activity_update_quantity, null);

                    EditText quantityInput = dialogView.findViewById(R.id.updateQuantity);
                    Button cancelBtn = dialogView.findViewById(R.id.buttonCancel);
                    Button updateBtn = dialogView.findViewById(R.id.buttonUpdate);

                    // show current quantity
                    quantityInput.setText(String.valueOf(item.getQuantity()));

                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setView(dialogView)
                            .create();

                    // if cancel button is clicked close dialog view
                    cancelBtn.setOnClickListener(vv -> dialog.dismiss());

                    // if user clicks update button, set on click listener update adapter and database
                    updateBtn.setOnClickListener(vv -> {
                        String newQuantity = quantityInput.getText().toString().trim();

                        // don't allow fields to be left blank
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
                        // ensure user is not entering a negative value
                        if (newQty < 0) {
                            showFeedback("Cannot be a negative value.");
                            return;
                        }

                        // update Quantity from database by id
                        boolean updated = myDb.updateQuantity(item.getId(), newQty);

                        //if item updated notify adapter, update the local quantity object, and provide feedback,
                        if (updated) {
                            item.setQuantity(newQty);
                            notifyItemChanged(position); // notify update to recycler view
                            showFeedback("Item Updated.");

                            // if new quantity is zero notify user
                            if (newQty == 0) {
                                Toast.makeText(context, item.getName() + " is out of stock.", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss(); // close dialog window
                        } else {
                            showFeedback("Update Failed.");
                        }
                    });
                    dialog.show(); // display window
                }
            });
        }
    }
}