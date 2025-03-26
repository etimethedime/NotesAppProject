package com.example.notesappproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Memo currentMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initAddButton();
        initListButton();
        initSettingsButton();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            initMemo(extras.getInt("memoID"));
        } else {
            currentMemo = new Memo();
        }
        initSaveButton();
    }

    public void initAddButton() {
        ImageButton imgButton = findViewById(R.id.addButton);
        imgButton.setEnabled(false);
    }

    public void initListButton() {
        ImageButton imgButton = findViewById(R.id.listButton);
        imgButton.setOnClickListener(i -> {
            Intent intent = new Intent(MainActivity.this, NotesListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    public void initSettingsButton() {
        ImageButton imgButton = findViewById(R.id.settingsButton);
        imgButton.setOnClickListener(i -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void initMemo(int id) {
        MemoDBSource dbSource = new MemoDBSource(this);
        try {
            dbSource.open();
            currentMemo = dbSource.getSpecificMemo(id);
            dbSource.close();
        } catch (Exception e) {
            Log.w(this.getLocalClassName(), "Error getting Memo from database");
        }
        final EditText titleText = findViewById(R.id.TitleEditText);
        final EditText dateText = findViewById(R.id.DateEditText);
        final EditText bodyText = findViewById(R.id.BodyEditText);
        final RadioGroup priorityGroup = findViewById(R.id.priorityRadioGroup);
        titleText.setText(currentMemo.getTitle());
        dateText.setText(currentMemo.getDate());
        bodyText.setText(currentMemo.getBody());
        switch (currentMemo.getPriority()) {
            case 1:
                priorityGroup.check(R.id.HighRadioButton);
                break;
            case 2:
                priorityGroup.check(R.id.MediumRadioButton);
                break;
            case 3:
                priorityGroup.check(R.id.LowRadioButton);
                break;
        }

    }

    private void initSaveButton() {
        Button saveButton = findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(v -> {

            Log.d("Saved Memo.", "Title " + currentMemo.getTitle() +
                    ", Date Published: " + currentMemo.getDate() +
                    ", Body: " + currentMemo.getBody() +
                    ", Priority: " + currentMemo.getPriority());

            Boolean wasSuccessful;
            MemoDBSource ds = new MemoDBSource(this);
            ds.open();
            if (currentMemo.getId() == -1) {
                wasSuccessful = ds.insertMemo(currentMemo);
                if (wasSuccessful) {
                    int newId = ds.getLastMemoId();
                    currentMemo.setId(newId);
                    Log.d("initSaveMemo", "Memo inserted successfully with ID: " + newId);
                } else {
                    Log.d("initSaveMemo", "Memo insertion failed.");
                }
            } else {
                wasSuccessful = ds.updateMemo(currentMemo);
                if (wasSuccessful) {
                    Log.d("initSaveMemo", "Memo updated successfully.");
                } else {
                    Log.d("initSaveMemo", "Memo update failed.");
                }
            }

            ds.close();
        });

    }

    private void initTextChangedEvents() {
        final EditText bodyText = findViewById(R.id.BodyEditText);
        final EditText titleText = findViewById(R.id.TitleEditText);
    }
}





