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
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put("title", memo.getTitle());
            initialValues.put("date", memo.getDate());  // Store date as string
            initialValues.put("body", memo.getBody());
            initialValues.put("priority", memo.getPriority());

            long result = database.insert("memos", null, initialValues);

            if (result > 0) {
                didSucceed = true;
            } else {
                Log.e("MemoDBSource", "Error inserting memo into database");
            }

        } catch (Exception e) {
            Log.e("MemoDBSource", "Error during memo insertion", e);
        }
        return didSucceed;
    }

    public boolean updateMemo(Memo memo) {
        boolean didSucceed = false;
        try{
            ContentValues updateValues = new ContentValues();
            updateValues.put("title", memo.getTitle());
            updateValues.put("date", memo.getDate());  // Store date as string
            updateValues.put("body", memo.getBody());
            updateValues.put("priority", memo.getPriority());

            int rowsAffected = database.update("memos", updateValues, "id = ?", new String[]{String.valueOf(memo.getId())});

            if (rowsAffected > 0) {
                didSucceed = true;
            } else {
                Log.e("MemoDBSource", "Error updating memo in database");
            }
        } catch (Exception e) {
            Log.e("MemoDBSource", "Error updating memo", e);
        }
        return didSucceed;
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
                newMemo.setDate(cursor.getString(2));  // Parse date string into Calendar
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
            memo.setDate(cursor.getString(2));  // Parse date string into Calendar
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
