package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditIngredientActivity extends AppCompatActivity {

    private EditText etIngredientName;
    private EditText etQuantity;
    private Spinner spinnerUnit;
    private DatabaseHelper databaseHelper;

    private int ingredientId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_ingredient);


        etIngredientName = findViewById(R.id.etIngredientName);
        etQuantity = findViewById(R.id.etQuantity);
        spinnerUnit = findViewById(R.id.spinnerUnit);

        Button btnUpdateIngredient =
                findViewById(R.id.btnUpdateIngredient);

        databaseHelper = new DatabaseHelper(this);

        // Set the available units
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


        ingredientId = getIntent().getIntExtra("ingredient_id", -1);

        String name = getIntent().getStringExtra("ingredient_name");
        double quantity = getIntent().getDoubleExtra("ingredient_quantity", 0);
        String unit = getIntent().getStringExtra("ingredient_unit");

        if (ingredientId == -1 || name == null || unit == null) {
            Toast.makeText(this, "Ingredient not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        etIngredientName.setText(name);
        etQuantity.setText(String.valueOf(quantity));

        for (int i = 0; i < units.length; i++) {
            if (units[i].equals(unit)) {
                spinnerUnit.setSelection(i);
                break;
            }
        }


        btnUpdateIngredient.setOnClickListener(v -> updateIngredient());
    }

    private void updateIngredient() {

        String name = etIngredientName.getText().toString().trim();
        String quantityText = etQuantity.getText().toString().trim();
        String unit = spinnerUnit.getSelectedItem().toString();


        if (name.isEmpty()) {
            etIngredientName.setError("Please enter an ingredient name");
            etIngredientName.requestFocus();
            return;
        }

        // Validate the quantity
        if (quantityText.isEmpty()) {
            etQuantity.setError("Please enter a quantity");
            etQuantity.requestFocus();
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

        // validates the metric which was used
        if (spinnerUnit.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select a unit", Toast.LENGTH_SHORT).show();
            return;
        }

        // saves the updated version
        boolean updated = databaseHelper.updateIngredient(
                ingredientId,
                name,
                quantity,
                unit
        );

        if (updated) {
            Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update", Toast.LENGTH_SHORT).show();
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
