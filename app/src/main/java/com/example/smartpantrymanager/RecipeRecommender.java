package com.example.smartpantrymanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecipeRecommender {

    private DatabaseHelper databaseHelper;

    public RecipeRecommender(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    // looks at the ingredients to see what can be prepared
    public List<Recipe> getMatchingRecipes() {

        List<Recipe> matchingRecipes = new ArrayList<>();

        List<Recipe> allRecipes = databaseHelper.getAllRecipes();

        List<Ingredient> pantryIngredients =
                databaseHelper.getAllIngredients();

        for (Recipe recipe : allRecipes) {

            List<RecipeIngredient> requiredIngredients =
                    databaseHelper.getRecipeIngredients(recipe.getId());

            if (requiredIngredients.isEmpty()) {
                continue;
            }

            boolean canPrepareRecipe = true;

            for (RecipeIngredient required : requiredIngredients) {

                double availableQuantity = 0;

                for (Ingredient pantry : pantryIngredients) {

                    if (normalizeName(pantry.getName()).equals(
                            normalizeName(required.getIngredientName()))) {

                        double convertedQuantity = convertQuantity(
                                pantry.getQuantity(),
                                pantry.getUnit(),
                                required.getUnit()
                        );

                        if (convertedQuantity >= 0) {
                            availableQuantity += convertedQuantity;
                        }
                    }
                }

                if (availableQuantity < required.getQuantity()) {
                    canPrepareRecipe = false;
                    break;
                }
            }

            if (canPrepareRecipe) {
                matchingRecipes.add(recipe);
            }
        }

        return matchingRecipes;
    }

    // normalizes the ingredient names for comparison
    private String normalizeName(String name) {

        if (name == null) {
            return "";
        }

        String normalized = name.trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ");

        if (normalized.equals("egg")) {
            return "eggs";
        }

        if (normalized.equals("potato")) {
            return "potatoes";
        }

        if (normalized.equals("carrot")) {
            return "carrots";
        }

        if (normalized.equals("tomatoes")) {
            return "tomato";
        }

        if (normalized.equals("pepper")) {
            return "peppers";
        }

        if (normalized.equals("onions")) {
            return "onion";
        }

        if (normalized.equals("banana") ||
                normalized.equals("bananas")) {
            return "banana";
        }

        return normalized;
    }

    // convert compatible measurement units
    private double convertQuantity(double quantity,
                                   String pantryUnit,
                                   String requiredUnit) {

        if (pantryUnit == null || requiredUnit == null) {
            return -1;
        }

        String from = normalizeUnit(pantryUnit);
        String to = normalizeUnit(requiredUnit);

        if (from.equals(to)) {
            return quantity;
        }

        if (from.equals("kg") && to.equals("g")) {
            return quantity * 1000;
        }

        if (from.equals("g") && to.equals("kg")) {
            return quantity / 1000;
        }

        if (from.equals("l") && to.equals("ml")) {
            return quantity * 1000;
        }

        if (from.equals("ml") && to.equals("l")) {
            return quantity / 1000;
        }

        return -1;
    }

    // normalize measurement units
    private String normalizeUnit(String unit) {

        String normalized = unit.trim().toLowerCase(Locale.ROOT);

        if (normalized.equals("grams") ||
                normalized.equals("gram")) {
            return "g";
        }

        if (normalized.equals("kilograms") ||
                normalized.equals("kilogram")) {
            return "kg";
        }

        if (normalized.equals("millilitres") ||
                normalized.equals("milliliters") ||
                normalized.equals("millilitre") ||
                normalized.equals("milliliter")) {
            return "ml";
        }

        if (normalized.equals("litres") ||
                normalized.equals("liters") ||
                normalized.equals("litre") ||
                normalized.equals("liter")) {
            return "l";
        }

        if (normalized.equals("pieces") ||
                normalized.equals("piece") ||
                normalized.equals("pc")) {
            return "pcs";
        }

        return normalized;
    }
}