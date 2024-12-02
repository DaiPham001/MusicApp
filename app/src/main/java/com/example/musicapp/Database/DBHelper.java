package com.example.musicapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String NAME = "music.db";
    public static final int VERSION = 1;

    public DBHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng để lưu danh sách nhạc dưới dạng chuỗi JSON
        String tb_music_list = "CREATE TABLE music_list (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "music_json TEXT" +  // Cột chứa chuỗi JSON của danh sách nhạc
                ")";
        db.execSQL(tb_music_list);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS music_list");
            onCreate(db);
        }
    }
}
