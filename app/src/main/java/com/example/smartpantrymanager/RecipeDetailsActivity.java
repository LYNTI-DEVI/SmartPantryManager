package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailsActivity extends AppCompatActivity {

    private TextView tvRecipeDetailsName;
    private TextView tvRecipeIngredients;
    private TextView tvRecipeInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        tvRecipeDetailsName = findViewById(R.id.tvRecipeDetailsName);
        tvRecipeIngredients = findViewById(R.id.tvRecipeIngredients);
        tvRecipeInstructions = findViewById(R.id.tvRecipeInstructions);

        String recipeName = getIntent().getStringExtra("recipe_name");
        String recipeInstructions = getIntent().getStringExtra("recipe_instructions");
        int recipeId = getIntent().getIntExtra("recipe_id", -1);

        tvRecipeDetailsName.setText(recipeName);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        StringBuilder ingredientsText = new StringBuilder();
        for (RecipeIngredient ingredient :
                databaseHelper.getRecipeIngredients(recipeId)) {

            ingredientsText.append("• ")
                    .append(ingredient.getIngredientName())
                    .append(" - ")
                    .append(ingredient.getQuantity())
                    .append(" ")
                    .append(ingredient.getUnit())
                    .append("\n");
        }

        if (ingredientsText.length() == 0) {
            ingredientsText.append("No ingredients listed.");
        }

        tvRecipeIngredients.setText(ingredientsText.toString());
        tvRecipeInstructions.setText(recipeInstructions);
    }
}