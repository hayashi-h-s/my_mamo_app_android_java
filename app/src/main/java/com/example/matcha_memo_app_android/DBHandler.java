package com.example.matcha_memo_app_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.matcha_memo_app_android.DTO.Memo;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    public static final String DB_NAME = "MemoList";
    public static final int DB_VERSION = 1;
    public static final String TABLE_MEMO = "memo";
    public static final String COL_ID = "id";
    public static final String COL_CREATED_AT = "createdAt";
    public static final String COL_NAME = "name";

    public DBHandler(Context content) {
        super(content, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createToDoTable = "CREATE TABLE " + TABLE_MEMO + " (" +
                COL_ID + " integer PRIMARY KEY AUTOINCREMENT," +
                COL_CREATED_AT + " datetime DEFAULT CURRENT_TIMESTAMP," +
                COL_NAME + " varchar)";
        db.execSQL(createToDoTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addMemo(Memo memo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, memo.getName());
        long result = db.insert(TABLE_MEMO, null, cv);
        return result != -1;
    }

    public void deleteToDo(Long memoId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_MEMO, COL_ID + "=?", new String[]{String.valueOf(memoId)});
    }

    public ArrayList<Memo> getMemos() {
        ArrayList<Memo> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor queryResult = db.rawQuery("SELECT * from " + TABLE_MEMO, null);
        if (queryResult.moveToFirst()) {
            do {
                Memo memo = new Memo();
                memo.setId(queryResult.getLong(queryResult.getColumnIndex(COL_ID)));
                memo.setName(queryResult.getString(queryResult.getColumnIndex(COL_NAME)));
                result.add(memo);
            } while (queryResult.moveToNext());
        }
        queryResult.close();
        return result;
    }

    public ArrayList<Memo> getCreateTimes() {
        ArrayList<Memo> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor queryResult = db.rawQuery("SELECT * from " + TABLE_MEMO, null);
        if (queryResult.moveToFirst()) {
            do {
                Memo memo = new Memo();
                memo.setId(queryResult.getLong(queryResult.getColumnIndex(COL_ID)));
                memo.setCreatedAt(queryResult.getString(queryResult.getColumnIndex(COL_CREATED_AT)));
                result.add(memo);
            } while (queryResult.moveToNext());
        }
        queryResult.close();
        return result;
    }
}
