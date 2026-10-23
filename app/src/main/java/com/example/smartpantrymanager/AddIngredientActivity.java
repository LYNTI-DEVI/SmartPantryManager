package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddIngredientActivity extends AppCompatActivity {

    private EditText etIngredientName;
    private EditText etQuantity;
    private Spinner spinnerUnit;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // showing the add
        setContentView(R.layout.activity_add_ingredient);

        // connection of the inputs and buttons
        etIngredientName = findViewById(R.id.etIngredientName);
        etQuantity = findViewById(R.id.etQuantity);
        spinnerUnit = findViewById(R.id.spinnerUnit);

        Button btnSaveIngredient =
                findViewById(R.id.btnSaveIngredient);

        Button btnBackToPantry =
                findViewById(R.id.btnBackToPantry);

        // database connection
        databaseHelper = new DatabaseHelper(this);

        // the unit options
        String[] units = {
                "Select Unit",
                "kg",
                "g",
                "L",
                "ml",
                "pieces",
                "packets",
                "cans"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                units
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerUnit.setAdapter(adapter);

        // this saves the inputs which are entered
        btnSaveIngredient.setOnClickListener(v -> saveIngredient());

        btnBackToPantry.setOnClickListener(v -> finish());
    }

    private void saveIngredient() {

        // user input
        String name = etIngredientName.getText().toString().trim();
        String quantityText = etQuantity.getText().toString().trim();
        String unit = spinnerUnit.getSelectedItem().toString();

        // makes sure that the input fields are not left empty
        if (name.isEmpty()) {
            etIngredientName.setError("Please enter an ingredient name");
            etIngredientName.requestFocus();
            return;
        }

        if (quantityText.isEmpty()) {
            etQuantity.setError("Please enter a quantity");
            etQuantity.requestFocus();
            return;
        }

        if (spinnerUnit.getSelectedItemPosition() == 0) {
            Toast.makeText(
                    this,
                    "Please select a unit",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        double quantity;

        try {
            quantity = Double.parseDouble(quantityText);

            if (!Double.isFinite(quantity) || quantity <= 0) {
                etQuantity.setError("Quantity must be greater than zero");
                etQuantity.requestFocus();
                return;
            }

        } catch (NumberFormatException e) {
            etQuantity.setError("Please enter a valid quantity");
            etQuantity.requestFocus();
            return;
        }

        // saves the ingredients to the SQLite database
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_QUANTITY, quantity);
        values.put(DatabaseHelper.COLUMN_UNIT, unit);

        long result = database.insert(
                DatabaseHelper.TABLE_INGREDIENTS,
                null,
                values
        );

        if (result != -1) {
            Toast.makeText(
                    this,
                    "Ingredient saved successfully!",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        } else {
            Toast.makeText(
                    this,
                    "Failed to save ingredient",
                    Toast.LENGTH_SHORT
            ).show();
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
