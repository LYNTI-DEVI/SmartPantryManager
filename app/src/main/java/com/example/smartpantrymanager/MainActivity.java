package com.example.smartpantrymanager;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

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
    }

    @Override
    protected void onDestroy() {
        if (databaseHelper != null) {
            databaseHelper.close();
        }

        super.onDestroy();
    }
}