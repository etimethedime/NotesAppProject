package com.example.notesappproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesListActivity extends AppCompatActivity {

    private ArrayList<Memo> memos;
    MemoAdapter memoAdapter;

    RecyclerView notesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notes_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.notesMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initAddButton();
        initListButton();
        initSettingsButton();
        initDeleteSwitch();

        String sortBy = getSharedPreferences("MyMemoPreferences",
                Context.MODE_PRIVATE).getString("sortfield", "date");

        String sortPriority = getSharedPreferences("MyMemoPreferences",
                Context.MODE_PRIVATE).getString("sortpriority", "high");

        MemoDBSource ds = new MemoDBSource(this);


        try {
            ds.open();
            memos = ds.getMemos(sortBy,sortPriority);
            ds.close();

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            notesList = findViewById(R.id.rvNotes);
            notesList.setLayoutManager(layoutManager);

            memoAdapter = new MemoAdapter(memos, NotesListActivity.this);
            //memoAdapter.setOnItemClickListener(onItemClickListener);
            notesList.setAdapter(memoAdapter);
        } catch (Exception e) {
            Toast.makeText(this, "Error retrieving memos", Toast.LENGTH_LONG).show();
        }
    }

    public void initAddButton() {
        ImageButton imgButton = findViewById(R.id.addButton);
        imgButton.setOnClickListener(i -> {
            Intent intent = new Intent(NotesListActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    public void initListButton() {
        ImageButton imgButton = findViewById(R.id.listButton);
        imgButton.setEnabled(false);
    }

    public void initSettingsButton() {
        ImageButton imgButton = findViewById(R.id.settingsButton);
        imgButton.setOnClickListener(i -> {
            Intent intent = new Intent(NotesListActivity.this, SettingsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
    private void initDeleteSwitch() {
        Switch s = findViewById(R.id.switchDelete);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (CompoundButton compoundButton,boolean b){
                Boolean status = compoundButton.isChecked();
                //MemoAdapter.setDelete(status);;
                //MemoAdapter.notifyDataSetChanged();
            }
        });
    }
}
