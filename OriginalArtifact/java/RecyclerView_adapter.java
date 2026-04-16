package com.cs360.inventoryappdelarosa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerView_adapter extends RecyclerView.Adapter<RecyclerView_adapter.ViewHolder> {

    private ArrayList<RecyclerView_list> recyclerViewlist;
    private Context context;

    public RecyclerView_adapter(ArrayList<RecyclerView_list> recyclerViewlist, Context context) {
        this.recyclerViewlist = recyclerViewlist;
        this.context = context;

    }

    @NonNull
    @Override
    public RecyclerView_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView_adapter.ViewHolder holder, int position) {
        holder.textViewCard.setText(recyclerViewlist.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return recyclerViewlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCard;
        ImageButton delete_button;
        ImageButton edit_button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCard = itemView.findViewById(R.id.textViewCard);
            delete_button = itemView.findViewById(R.id.delete_button);
            edit_button = itemView.findViewById(R.id.edit_button);

        }
    }
}
