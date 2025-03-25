package com.example.notesappproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class MemoDBSource {
    private MemoDBHelper dbHelper;
    private SQLiteDatabase database;

    public MemoDBSource(Context context) {
        dbHelper = new MemoDBHelper(context);
    }
    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

}
