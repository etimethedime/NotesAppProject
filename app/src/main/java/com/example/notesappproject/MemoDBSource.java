package com.example.notesappproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

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

    public boolean insertMemo(Memo memo) {
        ContentValues values = new ContentValues();
        values.put("title", memo.getTitle());
        values.put("date", memo.getDate().getTimeInMillis());
        values.put("body", memo.getBody());
        values.put("priority", memo.getPriority());

        long result = database.insert("memos", null, values);
        return result != -1;
    }

    public boolean updateMemo(Memo memo) {
        ContentValues values = new ContentValues();
        values.put("title", memo.getTitle());
        values.put("date", memo.getDate().getTimeInMillis());
        values.put("body", memo.getBody());
        values.put("priority", memo.getPriority());

        int rowsAffected = database.update("memos", values, "id = ?", new String[]{String.valueOf(memo.getId())});
        return rowsAffected > 0;
    }

    public ArrayList<Memo> getMemos(String sortField, String sortOrder) {
        ArrayList<Memo> memos = new ArrayList<>();
        try {
            String query = "SELECT * FROM memos ORDER BY " + sortField + " " + sortOrder;
            Cursor cursor = database.rawQuery(query, null);

            Memo newMemo;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                newMemo = new Memo();
                newMemo.setId(cursor.getInt(0));
                newMemo.setTitle(cursor.getString(1));
                newMemo.setDate(cursor.getString(2));  // Assuming date is stored as a string or timestamp
                newMemo.setBody(cursor.getString(3));
                newMemo.setPriority(cursor.getInt(4));
                memos.add(newMemo);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("MemoDBSource", "Error getting memos", e);
        }
        return memos;
    }

    public Memo getSpecificMemo(long memoId) {
        Memo memo = new Memo();
        String query = "SELECT * FROM memos WHERE id = " + memoId;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            memo.setId(cursor.getInt(0));
            memo.setTitle(cursor.getString(1));
            memo.setDate(cursor.getString(2));  // Assuming date is stored as a string or timestamp
            memo.setBody(cursor.getString(3));
            memo.setPriority(cursor.getInt(4));
            cursor.close();
        }
        return memo;
    }

    public boolean deleteMemo(long memoId) {
        boolean didDelete = false;
        try {
            didDelete = database.delete("memos", "id = " + memoId, null) > 0;
        } catch (Exception e) {
            Log.e("MemoDBSource", "Error deleting memo", e);
        }
        return didDelete;
    }
}
}
