package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // database information
    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 2;

    // the table which has the ingredients
    public static final String TABLE_INGREDIENTS = "ingredients";

    // columns of the ingredients
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_UNIT = "unit";

    public static final String TABLE_RECIPES = "recipes";
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // database is created
    @Override
    public void onCreate(SQLiteDatabase db) {

        String createIngredientsTable =
                "CREATE TABLE " + TABLE_INGREDIENTS + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT NOT NULL, " +
                        COLUMN_QUANTITY + " REAL NOT NULL, " +
                        COLUMN_UNIT + " TEXT NOT NULL)";

        db.execSQL(createIngredientsTable);

        createRecipeTables(db);
        insertDefaultRecipes(db);
    }

    private void createRecipeTables(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS recipes (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL UNIQUE, " +
                        "instructions TEXT NOT NULL)"
        );

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS recipe_ingredients (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "recipe_id INTEGER NOT NULL, " +
                        "ingredient_name TEXT NOT NULL, " +
                        "quantity REAL NOT NULL, " +
                        "unit TEXT NOT NULL, " +
                        "FOREIGN KEY(recipe_id) REFERENCES recipes(id))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 2) {
            createRecipeTables(db);
            insertDefaultRecipes(db);
        }
    }

    private void insertDefaultRecipes(SQLiteDatabase db) {

        addRecipe(db, "Creamy Pap with Tomato Relish",
                "Boil water and gradually stir in maize meal. "
                        + "Cook until thick. Fry chopped onions and "
                        + "tomatoes to prepare the relish.",
                new String[][]{
                        {"Maize Meal", "200", "g"},
                        {"Water", "500", "ml"},
                        {"Salt", "5", "g"},
                        {"Tomato", "2", "pcs"},
                        {"Onion", "1", "pcs"}
                });

        addRecipe(db, "Spicy Chakalaka Rice",
                "Cook rice until tender. Fry chopped onion and "
                        + "carrots with curry powder. Add baked beans "
                        + "and combine with cooked rice.",
                new String[][]{
                        {"Rice", "200", "g"},
                        {"Baked Beans", "200", "g"},
                        {"Carrots", "2", "pcs"},
                        {"Onion", "1", "pcs"},
                        {"Curry Powder", "10", "g"}
                });

        addRecipe(db, "Cheesy Potato Bake",
                "Slice potatoes and arrange in a baking dish. "
                        + "Add milk, garlic and butter. Sprinkle "
                        + "cheese over the top and bake until tender.",
                new String[][]{
                        {"Potatoes", "500", "g"},
                        {"Cheese", "150", "g"},
                        {"Milk", "250", "ml"},
                        {"Butter", "30", "g"},
                        {"Garlic", "2", "pcs"}
                });

        addRecipe(db, "Durban-Style Chicken Curry",
                "Fry onion with curry powder. Add chicken, "
                        + "potatoes and tomatoes. Simmer until "
                        + "the chicken is fully cooked.",
                new String[][]{
                        {"Chicken", "500", "g"},
                        {"Potatoes", "300", "g"},
                        {"Onion", "1", "pcs"},
                        {"Tomato", "2", "pcs"},
                        {"Curry Powder", "20", "g"}
                });

        addRecipe(db, "Mini Kota Sandwich",
                "Cut open the bread and fill with polony, "
                        + "cheese, atchar and cooked chips.",
                new String[][]{
                        {"Bread", "2", "pcs"},
                        {"Polony", "100", "g"},
                        {"Cheese", "50", "g"},
                        {"Atchar", "30", "g"},
                        {"Chips", "150", "g"}
                });

        addRecipe(db, "Creamy Spinach Pasta",
                "Boil pasta. Cook spinach with garlic and milk. "
                        + "Add cheese and combine with the pasta.",
                new String[][]{
                        {"Pasta", "200", "g"},
                        {"Spinach", "150", "g"},
                        {"Milk", "200", "ml"},
                        {"Cheese", "100", "g"},
                        {"Garlic", "2", "pcs"}
                });

        addRecipe(db, "Boerewors and Onion Skillet",
                "Cook boerewors thoroughly in a pan. "
                        + "Add sliced onion, tomato and peppers. "
                        + "Cook until the vegetables soften.",
                new String[][]{
                        {"Boerewors", "400", "g"},
                        {"Onion", "1", "pcs"},
                        {"Tomato", "2", "pcs"},
                        {"Peppers", "1", "pcs"}
                });

        addRecipe(db, "Golden Sweetcorn Fritters",
                "Mix sweetcorn, flour, eggs, milk and salt. "
                        + "Spoon the mixture into a non-stick pan "
                        + "and cook on both sides until golden.",
                new String[][]{
                        {"Sweetcorn", "200", "g"},
                        {"Flour", "150", "g"},
                        {"Eggs", "2", "pcs"},
                        {"Milk", "100", "ml"},
                        {"Salt", "5", "g"}
                });

        addRecipe(db, "Peri-Peri Chicken Wrap",
                "Cook chicken thoroughly with peri-peri sauce. "
                        + "Place chicken, lettuce and mayonnaise "
                        + "inside a tortilla and wrap.",
                new String[][]{
                        {"Chicken", "200", "g"},
                        {"Tortilla", "2", "pcs"},
                        {"Lettuce", "50", "g"},
                        {"Mayonnaise", "30", "g"},
                        {"Peri-Peri Sauce", "30", "ml"}
                });

        addRecipe(db, "Loaded Breakfast Potatoes",
                "Cook diced potatoes until tender. "
                        + "Fry with onion and peppers. "
                        + "Add cooked eggs and sprinkle with cheese.",
                new String[][]{
                        {"Potatoes", "300", "g"},
                        {"Eggs", "2", "pcs"},
                        {"Cheese", "80", "g"},
                        {"Onion", "1", "pcs"},
                        {"Peppers", "1", "pcs"}
                });

        addRecipe(db, "Cape Malay Spiced Rice",
                "Cook rice with turmeric and cinnamon. "
                        + "Add raisins and butter. "
                        + "Stir gently before serving.",
                new String[][]{
                        {"Rice", "200", "g"},
                        {"Turmeric", "5", "g"},
                        {"Cinnamon", "5", "g"},
                        {"Raisins", "50", "g"},
                        {"Butter", "20", "g"}
                });

        addRecipe(db, "Creamy Mushroom Toast",
                "Cook mushrooms with butter and garlic. "
                        + "Add milk and simmer until creamy. "
                        + "Serve over toasted bread.",
                new String[][]{
                        {"Bread", "2", "pcs"},
                        {"Mushrooms", "150", "g"},
                        {"Milk", "100", "ml"},
                        {"Butter", "20", "g"},
                        {"Garlic", "2", "pcs"}
                });

        addRecipe(db, "Spicy Bean and Cheese Quesadilla",
                "Fill a tortilla with beans, cheese, "
                        + "chopped onion and chilli. "
                        + "Fold and toast until the cheese melts.",
                new String[][]{
                        {"Tortilla", "2", "pcs"},
                        {"Beans", "150", "g"},
                        {"Cheese", "100", "g"},
                        {"Onion", "1", "pcs"},
                        {"Chilli", "1", "pcs"}
                });

        addRecipe(db, "Tomato and Basil Shakshuka",
                "Cook tomatoes, onion and garlic in a pan. "
                        + "Add basil. Crack eggs into the sauce "
                        + "and cook until the eggs are set.",
                new String[][]{
                        {"Eggs", "3", "pcs"},
                        {"Tomato", "3", "pcs"},
                        {"Onion", "1", "pcs"},
                        {"Garlic", "2", "pcs"},
                        {"Basil", "10", "g"}
                });

        addRecipe(db, "Honey Garlic Chicken Bites",
                "Cook chicken pieces thoroughly. "
                        + "Mix honey, garlic and soy sauce. "
                        + "Add the sauce and simmer briefly.",
                new String[][]{
                        {"Chicken", "300", "g"},
                        {"Honey", "40", "g"},
                        {"Garlic", "3", "pcs"},
                        {"Soy Sauce", "30", "ml"},
                        {"Oil", "20", "ml"}
                });

        addRecipe(db, "Butternut and Carrot Soup",
                "Boil butternut, carrots and onion until soft. "
                        + "Blend until smooth. Add milk and garlic "
                        + "and simmer before serving.",
                new String[][]{
                        {"Butternut", "400", "g"},
                        {"Carrots", "200", "g"},
                        {"Onion", "1", "pcs"},
                        {"Milk", "200", "ml"},
                        {"Garlic", "2", "pcs"}
                });

        addRecipe(db, "Savoury Mince Vetkoek",
                "Mix flour, yeast and water to make dough. "
                        + "Allow it to rise. Fry portions of dough "
                        + "in oil until cooked through. "
                        + "Cook mince with onion and tomato, "
                        + "then fill the vetkoek.",
                new String[][]{
                        {"Flour", "300", "g"},
                        {"Yeast", "10", "g"},
                        {"Water", "200", "ml"},
                        {"Mince", "300", "g"},
                        {"Onion", "1", "pcs"},
                        {"Tomato", "2", "pcs"},
                        {"Oil", "300", "ml"}
                });

        addRecipe(db, "Creamy Tuna Pasta",
                "Boil pasta. Cook chopped onion, "
                        + "then add tuna and milk. "
                        + "Stir in cheese and combine with pasta.",
                new String[][]{
                        {"Pasta", "200", "g"},
                        {"Tuna", "150", "g"},
                        {"Milk", "150", "ml"},
                        {"Cheese", "80", "g"},
                        {"Onion", "1", "pcs"}
                });

        addRecipe(db, "Peanut Butter Banana Pancakes",
                "Mash banana and mix with eggs, flour and milk. "
                        + "Cook small pancakes in a non-stick pan. "
                        + "Serve with peanut butter.",
                new String[][]{
                        {"Banana", "2", "pcs"},
                        {"Peanut Butter", "50", "g"},
                        {"Eggs", "2", "pcs"},
                        {"Flour", "150", "g"},
                        {"Milk", "150", "ml"}
                });

        addRecipe(db, "One-Pot Vegetable Biryani",
                "Cook onion with curry powder. "
                        + "Add rice, potatoes, peas and carrots. "
                        + "Add sufficient water and simmer "
                        + "until the rice and vegetables are cooked.",
                new String[][]{
                        {"Rice", "200", "g"},
                        {"Potatoes", "200", "g"},
                        {"Peas", "100", "g"},
                        {"Carrots", "100", "g"},
                        {"Curry Powder", "15", "g"},
                        {"Onion", "1", "pcs"},
                        {"Water", "500", "ml"}
                });
    }

    private void addRecipe(SQLiteDatabase db,
                           String name,
                           String instructions,
                           String[][] ingredients) {

        ContentValues recipeValues = new ContentValues();

        recipeValues.put("name", name);
        recipeValues.put("instructions", instructions);

        long recipeId = db.insertWithOnConflict(
                TABLE_RECIPES,
                null,
                recipeValues,
                SQLiteDatabase.CONFLICT_IGNORE
        );

        if (recipeId == -1) {
            return;
        }

        for (String[] ingredient : ingredients) {

            ContentValues values = new ContentValues();

            values.put("recipe_id", recipeId);
            values.put("ingredient_name", ingredient[0]);
            values.put("quantity",
                    Double.parseDouble(ingredient[1]));
            values.put("unit", ingredient[2]);

            db.insert(
                    TABLE_RECIPE_INGREDIENTS,
                    null,
                    values
            );
        }
    }

    // gets the ingredients which have been saved
    public List<Ingredient> getAllIngredients() {

        List<Ingredient> ingredientList = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.query(
                TABLE_INGREDIENTS,
                null,
                null,
                null,
                null,
                null,
                COLUMN_ID + " DESC"
        );

        try {
            int idIndex = cursor.getColumnIndexOrThrow(COLUMN_ID);
            int nameIndex = cursor.getColumnIndexOrThrow(COLUMN_NAME);
            int quantityIndex = cursor.getColumnIndexOrThrow(COLUMN_QUANTITY);
            int unitIndex = cursor.getColumnIndexOrThrow(COLUMN_UNIT);

            while (cursor.moveToNext()) {

                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                double quantity = cursor.getDouble(quantityIndex);
                String unit = cursor.getString(unitIndex);

                Ingredient ingredient = new Ingredient(
                        id,
                        name,
                        quantity,
                        unit
                );

                ingredientList.add(ingredient);
            }

        } finally {
            cursor.close();
        }

        return ingredientList;
    }

    // update an ingredient which has been saved
    public boolean updateIngredient(int id, String name, double quantity, String unit) {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_UNIT, unit);

        int rowsUpdated = database.update(
                TABLE_INGREDIENTS,
                values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        return rowsUpdated > 0;
    }

    // delete an ingredient from the database
    public boolean deleteIngredient(int id) {

        SQLiteDatabase database = this.getWritableDatabase();

        int rowsDeleted = database.delete(
                TABLE_INGREDIENTS,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        return rowsDeleted > 0;
    }

    // retrieve all recipes from the database
    public List<Recipe> getAllRecipes() {

        List<Recipe> recipeList = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.query(
                TABLE_RECIPES,
                null,
                null,
                null,
                null,
                null,
                "id ASC"
        );

        try {
            int idIndex = cursor.getColumnIndexOrThrow("id");
            int nameIndex = cursor.getColumnIndexOrThrow("name");
            int instructionsIndex = cursor.getColumnIndexOrThrow("instructions");

            while (cursor.moveToNext()) {

                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String instructions = cursor.getString(instructionsIndex);

                Recipe recipe = new Recipe(id, name, instructions);

                recipeList.add(recipe);
            }

        } finally {
            cursor.close();
        }

        return recipeList;
    }

    // retrieve the ingredients required for a recipe
    public List<RecipeIngredient> getRecipeIngredients(int recipeId) {

        List<RecipeIngredient> ingredientList = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.query(
                TABLE_RECIPE_INGREDIENTS,
                null,
                "recipe_id = ?",
                new String[]{String.valueOf(recipeId)},
                null,
                null,
                "id ASC"
        );

        try {
            int idIndex = cursor.getColumnIndexOrThrow("id");
            int recipeIdIndex = cursor.getColumnIndexOrThrow("recipe_id");
            int nameIndex = cursor.getColumnIndexOrThrow("ingredient_name");
            int quantityIndex = cursor.getColumnIndexOrThrow("quantity");
            int unitIndex = cursor.getColumnIndexOrThrow("unit");

            while (cursor.moveToNext()) {

                int id = cursor.getInt(idIndex);
                int savedRecipeId = cursor.getInt(recipeIdIndex);
                String name = cursor.getString(nameIndex);
                double quantity = cursor.getDouble(quantityIndex);
                String unit = cursor.getString(unitIndex);

                RecipeIngredient ingredient = new RecipeIngredient(
                        id,
                        savedRecipeId,
                        name,
                        quantity,
                        unit
                );

                ingredientList.add(ingredient);
            }

        } finally {
            cursor.close();
        }

        return ingredientList;
    }
}