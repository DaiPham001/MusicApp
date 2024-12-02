package com.example.musicapp.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.musicapp.Model.Music;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Music_Dao {
    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    public Music_Dao(Context context) {
        this.dbHelper = new DBHelper(context);
    }

    // Lưu danh sách nhạc dưới dạng chuỗi JSON vào cơ sở dữ liệu
    public boolean saveMusicList(ArrayList<Music> musicList) {
         sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        Gson gson = new Gson();
        String jsonMusicList = gson.toJson(musicList); // Chuyển danh sách nhạc thành chuỗi JSON

        values.put("music_json", jsonMusicList);  // Lưu chuỗi JSON vào cơ sở dữ liệu

        try {
            long check = sqLiteDatabase.insert("music_list", null, values);
            return check != -1; // Kiểm tra nếu việc lưu thành công
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        } finally {
            sqLiteDatabase.close();
        }
    }

    public ArrayList<Music> getSavedMusicList() {
        ArrayList<Music> musicList = new ArrayList<>();
         sqLiteDatabase = dbHelper.getReadableDatabase();

        // Câu truy vấn để lấy danh sách nhạc đã lưu
        String selectQuery = "SELECT * FROM music_list";
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

        // Lặp qua các hàng trong cursor và lấy chuỗi JSON
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String musicListJson = cursor.getString(cursor.getColumnIndex("music_json"));

            // Chuyển đổi chuỗi JSON thành ArrayList<Music>
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Music>>() {}.getType();
            musicList = gson.fromJson(musicListJson, type);
        }

        // Đóng cursor và database
        cursor.close();
        sqLiteDatabase.close();

        return musicList;
    }

    // Hàm để xóa toàn bộ dữ liệu trong bảng music_list
    public void deleteAllMusicList() {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        try {
            sqLiteDatabase.execSQL("DELETE FROM music_list");  // Xóa toàn bộ dữ liệu trong bảng
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.close();  // Đóng kết nối với cơ sở dữ liệu sau khi thực hiện
        }
    }
}
