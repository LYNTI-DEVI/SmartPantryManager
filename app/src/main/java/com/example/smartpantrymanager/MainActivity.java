package com.example.smartpantrymanager;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

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
    }

    @Override
    protected void onDestroy() {
        if (databaseHelper != null) {
            databaseHelper.close();
        }

        super.onDestroy();
    }
}