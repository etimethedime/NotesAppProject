package com.example.notesappproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
        initSortSearch();
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
        String sortBy = getSharedPreferences("MyMemoPreferences", Context.MODE_PRIVATE)
                .getString("sortfield", "date");

        String sortOrder = getSharedPreferences("MyMemoPreferences", Context.MODE_PRIVATE)
                .getString("sortorder", "ASC");


        RadioButton rbPriority = findViewById(R.id.radioPriority);
        RadioButton rbSubject = findViewById(R.id.radioSubject);
        RadioButton rbDate = findViewById(R.id.radioDate);

        if (sortBy.equalsIgnoreCase("priority")) {
            rbPriority.setChecked(true);
        } else if (sortBy.equalsIgnoreCase("subject")) {
            rbSubject.setChecked(true);
        } else {
            rbDate.setChecked(true);
        }
        RadioButton rbASC = findViewById(R.id.radioASC);
        RadioButton rbDSC = findViewById(R.id.radioDSC);

        if (sortOrder.equalsIgnoreCase("ASC")) {
            rbASC.setChecked(true);
        } else {
            rbDSC.setChecked(true);
        }
    }

    private void initSortByClick() {
        RadioGroup rgSortBy = findViewById(R.id.radioGroupSort);
        rgSortBy.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rbPriority = SettingsActivity.this.findViewById(R.id.radioPriority);
            RadioButton rbSubject = SettingsActivity.this.findViewById(R.id.radioSubject);
            if (rbPriority.isChecked()) {
                SettingsActivity.this.getSharedPreferences("MyMemoPreferences",
                        Context.MODE_PRIVATE).edit().putString("sortfield", "priority").apply();
            } else if (rbSubject.isChecked()) {
                SettingsActivity.this.getSharedPreferences("MyMemoPreferences",
                        Context.MODE_PRIVATE).edit().putString("sortfield", "subject").apply();
            } else {
                SettingsActivity.this.getSharedPreferences("MyMemoPreferences",
                        Context.MODE_PRIVATE).edit().putString("sortfield", "date").apply();
            }

        });
    }

    private void initSortPriorityClick() {
        RadioGroup rgSortBy = findViewById(R.id.radioGroupPriority);
        rgSortBy.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rbASC = SettingsActivity.this.findViewById(R.id.radioASC);
            RadioButton rbDSC = SettingsActivity.this.findViewById(R.id.radioDSC);

            if (rbASC.isChecked()) {
                SettingsActivity.this.getSharedPreferences("MyMemoPreferences",
                        Context.MODE_PRIVATE).edit().putString("sortorder", "ASC").apply();
            } else if (rbDSC.isChecked()) {
                SettingsActivity.this.getSharedPreferences("MyMemoPreferences",
                        Context.MODE_PRIVATE).edit().putString("sortorder", "DESC").apply();
            }
        });
    }

    private void initSortSearch() {
        Button search = findViewById(R.id.buttonSearch);
        EditText searchBar = findViewById(R.id.editSubjectFilter);
        TextView errorMessage = findViewById(R.id.errorView);
        String message = "No Memo Found! Try again.";


        search.setOnClickListener( s -> {
            String keyword = searchBar.getText().toString().trim();
            if (!keyword.isEmpty()) {
                Intent intent = new Intent(SettingsActivity.this, NotesListActivity.class);
                intent.putExtra("SEARCH_KEYWORD", keyword);
                startActivity(intent);
            } else {
                Toast.makeText(SettingsActivity.this, "Enter a keyword!", Toast.LENGTH_SHORT).show();
                errorMessage.setText(message);
            }
        });
    }

}
