package com.example.musicapp.Activity;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import com.example.musicapp.Model.FilesMP3;

import java.util.ArrayList;

public class Myapplitication extends Application {
    public static final String CHANNEL_ID = "dai";
    private ArrayList<FilesMP3> list;
    @Override
    public void onCreate() {
        super.onCreate();
        createChannelNotification();
    }

    private void createChannelNotification() {
        // check API 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // set âm thanh
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "dai"
                    , NotificationManager.IMPORTANCE_HIGH);// độ uy tiên hiển thị của notification
            // channel.setSound(null, );// tắt tiếng thông báo của notification
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
            list = getAllAudio(this);
        }
    }



    public static ArrayList<FilesMP3> getAllAudio(Context context){
        ArrayList<FilesMP3> tempAudiolist = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
        };
        Cursor cursor = context.getContentResolver().query(uri,projection,null,null,null);
        if (cursor != null){
            while (cursor.moveToNext()){
                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String artis = cursor.getString(4);

                FilesMP3 musicFiles = new FilesMP3(path,title,artis,album,duration);
                //Log.e("path","album"+album);
                tempAudiolist.add(musicFiles);
            }
            cursor.close();
        }
        return tempAudiolist;
    }
}
