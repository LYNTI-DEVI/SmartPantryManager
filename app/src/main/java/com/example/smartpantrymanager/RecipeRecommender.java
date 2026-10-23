package com.example.smartpantrymanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

        Map<String, Double> pantryQuantities = new HashMap<>();
        Map<String, String> pantryUnits = new HashMap<>();

        for (Ingredient pantry : pantryIngredients) {

            String name = normalizeName(pantry.getName());

            pantryQuantities.put(
                    name,
                    pantryQuantities.getOrDefault(name, 0.0)
                            + pantry.getQuantity()
            );

            pantryUnits.put(name, pantry.getUnit());
        }

        Map<Integer, Double> recipeScores = new HashMap<>();

        for (Recipe recipe : allRecipes) {

            List<RecipeIngredient> requiredIngredients =
                    databaseHelper.getRecipeIngredients(recipe.getId());

            if (requiredIngredients.isEmpty()) {
                continue;
            }

            int matchedIngredients = 0;
            boolean allIngredientsAvailable = true;

            for (RecipeIngredient required : requiredIngredients) {

                String requiredName =
                        normalizeName(required.getIngredientName());

                double availableQuantity = 0;

                for (Ingredient pantry : pantryIngredients) {

                    if (normalizeName(pantry.getName()).equals(requiredName)) {

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

                if (availableQuantity >= required.getQuantity()) {
                    matchedIngredients++;
                } else {
                    allIngredientsAvailable = false;
                    break;
                }
            }

            if (allIngredientsAvailable) {

                double score = (double) matchedIngredients
                        / requiredIngredients.size();

                recipeScores.put(recipe.getId(), score);
                matchingRecipes.add(recipe);
            }
        }

        Collections.sort(matchingRecipes, new Comparator<Recipe>() {
            @Override
            public int compare(Recipe first, Recipe second) {

                double firstScore =
                        recipeScores.getOrDefault(first.getId(), 0.0);

                double secondScore =
                        recipeScores.getOrDefault(second.getId(), 0.0);

                return Double.compare(secondScore, firstScore);
            }
        });

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

        if (normalized.equals("egg") || normalized.equals("eggs")) {
            return "eggs";
        }

        if (normalized.equals("potato") || normalized.equals("potatoes")) {
            return "potatoes";
        }

        if (normalized.equals("carrot") || normalized.equals("carrots")) {
            return "carrots";
        }

        if (normalized.equals("tomato") || normalized.equals("tomatoes")) {
            return "tomatoes";
        }

        if (normalized.equals("onion") || normalized.equals("onions")) {
            return "onions";
        }

        if (normalized.equals("pepper") || normalized.equals("peppers")) {
            return "peppers";
        }

        if (normalized.equals("banana") || normalized.equals("bananas")) {
            return "bananas";
        }

        if (normalized.equals("maize meal") || normalized.equals("maizemeal")) {
            return "maize meal";
        }

        if (normalized.equals("baked beans")) {
            return "baked beans";
        }

        return normalized;
    }

    // convert compatible measurement units
    private double convertQuantity(
            double quantity,
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

        if (normalized.equals("grams")
                || normalized.equals("gram")) {
            return "g";
        }

        if (normalized.equals("kilograms")
                || normalized.equals("kilogram")) {
            return "kg";
        }

        if (normalized.equals("millilitres")
                || normalized.equals("milliliters")
                || normalized.equals("millilitre")
                || normalized.equals("milliliter")) {
            return "ml";
        }

        if (normalized.equals("litres")
                || normalized.equals("liters")
                || normalized.equals("litre")
                || normalized.equals("liter")) {
            return "l";
        }

        if (normalized.equals("pieces")
                || normalized.equals("piece")
                || normalized.equals("pc")
                || normalized.equals("pcs")) {
            return "pcs";
        }

        return normalized;
    }
}