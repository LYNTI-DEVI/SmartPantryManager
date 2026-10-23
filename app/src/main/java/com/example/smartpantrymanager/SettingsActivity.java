package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsActivity extends AppCompatActivity {

    private Button btnBackToPantry;
    private RadioGroup radioGroupTheme;
    private RadioButton radioLightMode;
    private RadioButton radioDarkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // return to the pantry screen
        btnBackToPantry = findViewById(R.id.btnBackToPantry);
        btnBackToPantry.setOnClickListener(v -> finish());

        radioGroupTheme = findViewById(R.id.radioGroupTheme);
        radioLightMode = findViewById(R.id.radioLightMode);
        radioDarkMode = findViewById(R.id.radioDarkMode);

        SharedPreferences preferences =
                getSharedPreferences("SmartPantrySettings", MODE_PRIVATE);

        boolean isDarkMode = preferences.getBoolean("dark_mode", false);

        if (isDarkMode) {
            radioDarkMode.setChecked(true);
        } else {
            radioLightMode.setChecked(true);
        }

        radioGroupTheme.setOnCheckedChangeListener((group, checkedId) -> {

            boolean darkMode = checkedId == R.id.radioDarkMode;

            preferences.edit()
                    .putBoolean("dark_mode", darkMode)
                    .apply();

            if (darkMode) {
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_YES
                );
            } else {
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_NO
                );
            }
        });
    }
}