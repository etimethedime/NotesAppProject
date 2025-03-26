package com.example.notesappproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingsActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.settings);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settingsMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initAddButton();
        initListButton();
        initSettingsButton();
        initSettings();
        initSortByClick();
        initSortPriorityClick();

    }


    public void initAddButton() {
        ImageButton imgButton = findViewById(R.id.addButton);
        imgButton.setOnClickListener(i -> {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    public void initListButton() {
        ImageButton imgButton = findViewById(R.id.listButton);
        imgButton.setOnClickListener(i -> {
            Intent intent = new Intent(SettingsActivity.this, NotesListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    public void initSettingsButton() {
        ImageButton imgButton = findViewById(R.id.settingsButton);
        imgButton.setEnabled(false);
    }

    public void initSettings() {
        RadioButton rbPriority = findViewById(R.id.radioPriority);
        RadioButton rbSubject = findViewById(R.id.radioSubject);
        RadioButton rbDate = findViewById(R.id.radioDate);

        RadioButton rbHigh = findViewById(R.id.radioHighPriority);
        RadioButton rbMed = findViewById(R.id.radioMedPriority);
        RadioButton rbLow = findViewById(R.id.radioLowPriority);


        String sortBy = getSharedPreferences("MyMemoPreferences",
                Context.MODE_PRIVATE).getString("sortfield", "date");

        String sortPriority = getSharedPreferences("MyMemoPreferences",
                Context.MODE_PRIVATE).getString("sortpriority", "high");


        if (sortBy.equalsIgnoreCase("priority")) {
            rbPriority.setChecked(true);
        }
        else if (sortBy.equalsIgnoreCase("subject")) {
            rbSubject.setChecked(true);
        }
        else {
            rbDate.setChecked(true);
        }


        if (sortPriority.equalsIgnoreCase("high")) {
            rbHigh.setChecked(true);
        }
        else if (sortPriority.equalsIgnoreCase("med")) {
            rbMed.setChecked(true);
        }
        else {
            rbLow.setChecked(true);
        }

    }

    private void initSortByClick() {
        RadioGroup rgSortBy = findViewById(R.id.radioGroupSort);
        rgSortBy.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rbPriority = SettingsActivity.this.findViewById(R.id.radioPriority);
            RadioButton rbSubject = SettingsActivity.this.findViewById(R.id.radioSubject);
            if (rbPriority.isChecked()) {
                SettingsActivity.this.getSharedPreferences("MyContactListPreferences",
                        Context.MODE_PRIVATE).edit().putString("sortfield", "priority").apply();
            } else if (rbSubject.isChecked()) {
                SettingsActivity.this.getSharedPreferences("MyContactListPreferences",
                        Context.MODE_PRIVATE).edit().putString("sortfield", "subject").apply();
            } else {
                SettingsActivity.this.getSharedPreferences("MyContactListPreferences",
                        Context.MODE_PRIVATE).edit().putString("sortfield", "date").apply();
            }

        });
    }

    private void initSortPriorityClick() {
        RadioGroup rgSortBy = findViewById(R.id.radioGroupPriority);
        rgSortBy.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rbHigh = SettingsActivity.this.findViewById(R.id.radioHighPriority);
            RadioButton rbMed = SettingsActivity.this.findViewById(R.id.radioMedPriority);
            if (rbHigh.isChecked()) {
                SettingsActivity.this.getSharedPreferences("MyMemoPreferences",
                        Context.MODE_PRIVATE).edit().putString("sortpriority", "high").apply();
            } else if (rbMed.isChecked()) {
                SettingsActivity.this.getSharedPreferences("MyMemoPreferences",
                        Context.MODE_PRIVATE).edit().putString("sortpriority", "med").apply();
            } else {
                SettingsActivity.this.getSharedPreferences("MyMemoPreferences",
                        Context.MODE_PRIVATE).edit().putString("sortpriority", "low").apply();
            }

        });
    }


}
