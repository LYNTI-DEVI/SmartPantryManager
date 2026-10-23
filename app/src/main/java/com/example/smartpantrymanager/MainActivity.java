package com.example.smartpantrymanager;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerViewPantry;
    private IngredientAdapter ingredientAdapter;
    private List<Ingredient> ingredientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // displays the screen
        setContentView(R.layout.activity_main);

        // database connection
        databaseHelper = new DatabaseHelper(this);

        // create/open the database
        try {
            SQLiteDatabase database =
                    databaseHelper.getWritableDatabase();

            Log.d("SmartPantryDB",
                    "SQLite has connected");

        } catch (Exception e) {

            Log.e("SmartPantryDB",
                    "Database failed to connect: " + e.getMessage());
        }

        // setting up the RecyclerView
        recyclerViewPantry = findViewById(R.id.recyclerViewPantry);

        recyclerViewPantry.setLayoutManager(
                new LinearLayoutManager(this)
        );

        ingredientList = new ArrayList<>();

        ingredientAdapter = new IngredientAdapter(
                ingredientList,
                new IngredientAdapter.OnIngredientClickListener() {

                    @Override
                    public void onEditClick(Ingredient ingredient) {

                        Intent intent = new Intent(
                                MainActivity.this,
                                EditIngredientActivity.class
                        );

                        intent.putExtra("ingredient_id", ingredient.getId());
                        intent.putExtra("ingredient_name", ingredient.getName());
                        intent.putExtra("ingredient_quantity", ingredient.getQuantity());
                        intent.putExtra("ingredient_unit", ingredient.getUnit());

                        startActivity(intent);
                    }

                    @Override
                    public void onDeleteClick(Ingredient ingredient) {

                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Delete Ingredient")
                                .setMessage("Are you sure you want to delete "
                                        + ingredient.getName() + "?")
                                .setPositiveButton("Delete", (dialog, which) -> {

                                    boolean deleted = databaseHelper.deleteIngredient(
                                            ingredient.getId()
                                    );

                                    if (deleted) {
                                        Toast.makeText(
                                                MainActivity.this,
                                                "Ingredient deleted successfully",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                        loadIngredients();

                                    } else {
                                        Toast.makeText(
                                                MainActivity.this,
                                                "Failed to delete ingredient",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                    }
                                })
                                .setNegativeButton("Cancel", (dialog, which) ->
                                        dialog.dismiss()
                                )
                                .show();
                    }
                }
        );

        recyclerViewPantry.setAdapter(ingredientAdapter);

        // connect the Add Ingredient button
        Button btnAddIngredient =
                findViewById(R.id.btnAddIngredient);

        btnAddIngredient.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    AddIngredientActivity.class
            );

            startActivity(intent);
        });

        // connect the Recommended Recipes button
        Button btnRecommendedRecipes =
                findViewById(R.id.btnRecommendedRecipes);

        btnRecommendedRecipes.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    RecommendedRecipesActivity.class
            );

            startActivity(intent);
        });

        // connect the Settings button
        TextView btnSettings = findViewById(R.id.btnSettings);

        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    SettingsActivity.class
            );

            startActivity(intent);
        });
    }

    // load the saved ingredients
    private void loadIngredients() {

        ingredientList.clear();

        ingredientList.addAll(databaseHelper.getAllIngredients());

        ingredientAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // this is used to refresh
        loadIngredients();
    }

    @Override
    protected void onDestroy() {
        if (databaseHelper != null) {
            databaseHelper.close();
        }

        super.onDestroy();
    }
}