package com.example.notesappproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    private MemoAdapter memoAdapter;
    private RecyclerView notesList;

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            int notesId = memos.get(position).getId();
            Intent intent = new Intent(NotesListActivity.this, MainActivity.class);
            intent.putExtra("memoID", notesId);
            startActivity(intent);
        }
    };

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
        notesList = findViewById(R.id.rvNotes);
        notesList.setLayoutManager(new LinearLayoutManager(this));

        String keyword = getIntent().getStringExtra("SEARCH_KEYWORD");
        Log.d("KeywordCheck", "Keyword received: " + keyword);
        if (keyword != null && !keyword.isEmpty()) {
            displayResults(keyword);
        } else {
            loadMemos();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String keyword = getIntent().getStringExtra("SEARCH_KEYWORD");
        if (keyword != null && !keyword.isEmpty()) {
            displayResults(keyword);
        } else {
            loadMemos();
        }
    }

    private void displayResults(String keyword) {
        MemoDBSource ds = new MemoDBSource(this);
        try {
            ds.open();
            memos = ds.searchMemos(keyword);
            ds.close();

            Log.d("SearchResults", "Number of results found: " + memos.size());

            if (memos.isEmpty()) {
                Log.d("SearchResults", "No results found.");
            }

            updateRecyclerView();
        } catch (Exception e) {
            Toast.makeText(this, "Error searching memos", Toast.LENGTH_LONG).show();
        }
    }


    private void loadMemos() {
        String sortBy = getSharedPreferences("MyMemoPreferences", MODE_PRIVATE)
                .getString("sortfield", "date");
        String sortOrder = getSharedPreferences("MyMemoPreferences", MODE_PRIVATE)
                .getString("sortorder", "ASC");

        MemoDBSource ds = new MemoDBSource(this);
        try {
            ds.open();
            memos = ds.getMemos(sortBy, sortOrder);
            ds.close();
            updateRecyclerView();
        } catch (Exception e) {
            Toast.makeText(this, "Error retrieving memos", Toast.LENGTH_LONG).show();
        }
    }

    private void updateRecyclerView() {
        if (memoAdapter == null) {
            memoAdapter = new MemoAdapter(memos, this);
            memoAdapter.setOnItemClickListener(onItemClickListener);
            notesList.setAdapter(memoAdapter);
        } else {
            memoAdapter.setMemos(memos);
            memoAdapter.notifyDataSetChanged();
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
        s.setOnCheckedChangeListener((compoundButton, b) -> {
            memoAdapter.setDelete(b);
            memoAdapter.notifyDataSetChanged();
        });
    }
}
