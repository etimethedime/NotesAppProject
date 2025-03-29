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
            initialValues.put("date", memo.getDate());
            initialValues.put("body", memo.getBody());
            initialValues.put("priority", memo.getPriority());

            long result = database.insert("Memo", null, initialValues);
            didSucceed = result > 0;
        } catch (Exception e) {
            Log.e("MemoDBSource", "Error during memo insertion", e);
        }
        return didSucceed;
    }
    public boolean updateMemo(Memo memo) {
        boolean didSucceed = false;
        try {
            ContentValues updateValues = new ContentValues();
            updateValues.put("title", memo.getTitle());
            updateValues.put("date", memo.getDate());
            updateValues.put("body", memo.getBody());
            updateValues.put("priority", memo.getPriority());

            int rowsAffected = database.update("Memo", updateValues, "id = ?", new String[]{String.valueOf(memo.getId())});
            didSucceed = rowsAffected > 0;
        } catch (Exception e) {
            Log.e("MemoDBSource", "Error updating memo", e);
        }
        return didSucceed;
    }

    public ArrayList<Memo> getMemos(String sortField, String sortOrder) {
        ArrayList<Memo> memos = new ArrayList<>();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM Memo ORDER BY " + sortField + " " + sortOrder;
            cursor = database.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    Memo newMemo = new Memo();
                    newMemo.setId(cursor.getInt(0));
                    newMemo.setTitle(cursor.getString(1));
                    newMemo.setDate(cursor.getString(2));
                    newMemo.setBody(cursor.getString(3));
                    newMemo.setPriority(cursor.getInt(4));
                    memos.add(newMemo);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("MemoDBSource", "Error getting memos", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return memos;
    }

    public Memo getSpecificMemo(int memoId) {
        Memo memo = null;
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM Memo WHERE id = ?";
            cursor = database.rawQuery(query, new String[]{String.valueOf(memoId)});

            if (cursor.moveToFirst()) {
                memo = new Memo();
                memo.setId(cursor.getInt(0));
                memo.setTitle(cursor.getString(1));
                memo.setDate(cursor.getString(2));
                memo.setBody(cursor.getString(3));
                memo.setPriority(cursor.getInt(4));
            }
        } catch (Exception e) {
            Log.e("MemoDBSource", "Error getting specific memo", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return memo;
    }

    public int getLastMemoId() {
        int lastId = -1;
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("SELECT MAX(id) FROM Memo", null);
            if (cursor.moveToFirst()) {
                lastId = cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e("MemoDBSource", "Error getting last memo ID", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lastId;
    }

    public boolean deleteMemo(int memoId) {
        boolean didDelete = false;
        try {
            didDelete = database.delete("Memo", "id = ?", new String[]{String.valueOf(memoId)}) > 0;
        } catch (Exception e) {
            Log.e("MemoDBSource", "Error deleting memo", e);
        }
        return didDelete;
    }
}
