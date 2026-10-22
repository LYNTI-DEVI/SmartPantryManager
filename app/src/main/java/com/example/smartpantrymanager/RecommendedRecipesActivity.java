package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecommendedRecipesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewRecipes;
    private TextView tvNoRecipes;
    private DatabaseHelper databaseHelper;
    private RecipeRecommender recipeRecommender;
    private Button btnBackToPantry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_recipes);

        recyclerViewRecipes = findViewById(R.id.recyclerViewRecipes);
        tvNoRecipes = findViewById(R.id.tvNoRecipes);
        btnBackToPantry = findViewById(R.id.btnBackToPantry);

        // return to the pantry screen
        btnBackToPantry.setOnClickListener(v -> finish());

        databaseHelper = new DatabaseHelper(this);
        recipeRecommender = new RecipeRecommender(databaseHelper);

        recyclerViewRecipes.setLayoutManager(new LinearLayoutManager(this));

        displayRecommendedRecipes();
    }

    // display recipes that match the available pantry ingredients
    private void displayRecommendedRecipes() {

        List<Recipe> recommendedRecipes = recipeRecommender.getMatchingRecipes();

        if (recommendedRecipes.isEmpty()) {

            tvNoRecipes.setVisibility(View.VISIBLE);
            recyclerViewRecipes.setVisibility(View.GONE);

        } else {

            tvNoRecipes.setVisibility(View.GONE);
            recyclerViewRecipes.setVisibility(View.VISIBLE);

            RecipeAdapter adapter = new RecipeAdapter(recommendedRecipes, recipe -> {

                Intent intent = new Intent(
                        RecommendedRecipesActivity.this,
                        RecipeDetailsActivity.class
                );

                intent.putExtra("recipe_id", recipe.getId());
                intent.putExtra("recipe_name", recipe.getName());
                intent.putExtra("recipe_instructions", recipe.getInstructions());

                startActivity(intent);
            });

            recyclerViewRecipes.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (recipeRecommender != null) {
            displayRecommendedRecipes();
        }
    }
}