package com.example.crudsqllite.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Db extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    static final String DATABASE_NAME = "digitaltalent.db";
    public static final String TABLE_SQLite = "sqlite";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_image = "image";

    public Db(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + TABLE_SQLite + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY autoincrement, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_ADDRESS + " TEXT NOT NULL," +
                COLUMN_image + " TEXT " +
                " )";
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SQLite);
        onCreate(db);
    }

    public ArrayList<HashMap<String, String>> getAlData() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM " + TABLE_SQLite;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(COLUMN_ID, cursor.getString(0));
                map.put(COLUMN_NAME, cursor.getString(1));
                map.put(COLUMN_ADDRESS, cursor.getString(2));
                map.put(COLUMN_image, cursor.getString(3));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        Log.e("select sqlite", "" + wordList);

//        database.close();
        return wordList;
    }

    public void insert( String name, String address,String phat) {
        SQLiteDatabase database = this.getWritableDatabase();
        String queryValues = "INSERT INTO " + TABLE_SQLite + " (name, address, image) " +
                "VALUES ('" + name + "', '" + address + "','" + phat + "')";
        Log.e("insert sqlite ", "" + queryValues);
        database.execSQL(queryValues);
//        database.close();
    }

    public void update(int id, String name, String address,String phat) {
        SQLiteDatabase database = this.getWritableDatabase();

        String updateQuery = "UPDATE " + TABLE_SQLite + " SET "
                + COLUMN_NAME + "='" + name + "', "
                + COLUMN_ADDRESS + "='" + address + "',"
                + COLUMN_image + "='" + phat + "' "
                + " WHERE " + COLUMN_ID + "=" + "'" + id + "'";
        Log.e("update sqlite ", updateQuery);
        database.execSQL(updateQuery);
//        database.close();
    }

    public void delete(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor data=database.rawQuery("SELECT * FROM " + TABLE_SQLite+" WHERE " + COLUMN_ID + "=" + "'" + id + "'",null);
        data.moveToFirst();
        String img= data.getString((int)data.getColumnIndex(COLUMN_image));

        File imgFile=new File(img);
        System.out.println(imgFile.getAbsolutePath());
        imgFile.delete();
        String updateQuery = "DELETE FROM " + TABLE_SQLite + " WHERE " + COLUMN_ID + "=" + "'" + id + "'";

        Log.e("update sqlite ", updateQuery);
        database.execSQL(updateQuery);
//        database.close();
    }
}
