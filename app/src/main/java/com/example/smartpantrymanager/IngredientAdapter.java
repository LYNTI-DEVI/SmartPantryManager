package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private final List<Ingredient> ingredientList;

    // Listen for Edit and Delete button clicks
    public interface OnIngredientClickListener {
        void onEditClick(Ingredient ingredient);
        void onDeleteClick(Ingredient ingredient);
    }

    private final OnIngredientClickListener listener;

    public IngredientAdapter(List<Ingredient> ingredientList,
                             OnIngredientClickListener listener) {
        this.ingredientList = ingredientList;
        this.listener = listener;
    }

    // making a layout for each ingredient
    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pantry_item, parent, false);

        return new IngredientViewHolder(view);
    }

    // displaying the ingredients information
    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {

        Ingredient ingredient = ingredientList.get(position);

        holder.tvIngredientName.setText(ingredient.getName());

        String quantityText = "Quantity: "
                + ingredient.getQuantity()
                + " "
                + ingredient.getUnit();

        holder.tvIngredientQuantity.setText(quantityText);

        // Handle Edit button click
        holder.btnEditIngredient.setOnClickListener(v ->
                listener.onEditClick(ingredient)
        );

        // Handle Delete button click
        holder.btnDeleteIngredient.setOnClickListener(v ->
                listener.onDeleteClick(ingredient)
        );
    }

    // gets the number of ingredients
    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    // connection for the layout views
    static class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView tvIngredientName;
        TextView tvIngredientQuantity;

        Button btnEditIngredient;
        Button btnDeleteIngredient;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);

            tvIngredientName = itemView.findViewById(R.id.tvIngredientName);
            tvIngredientQuantity = itemView.findViewById(R.id.tvIngredientQuantity);

            btnEditIngredient = itemView.findViewById(R.id.btnEditIngredient);
            btnDeleteIngredient = itemView.findViewById(R.id.btnDeleteIngredient);
        }
    }
}
