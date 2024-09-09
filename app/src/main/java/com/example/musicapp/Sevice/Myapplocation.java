package com.example.musicapp.Sevice;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Myapplocation extends Application {
    public static final String channel_id="Music Player Channel";

    @Override
    public void onCreate() {
        super.onCreate();
        createChannelNotification();
    }

    private void createChannelNotification() {
        // check API 26 trở lên
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channel_id, "Music Player Channel"
                    , NotificationManager.IMPORTANCE_HIGH);// độ uy tiên hiển thị notification
            channel.setSound(null,null); //tắt tiếng thông báo của notification
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null){
                manager.createNotificationChannel(channel);
            }
        }
    }
}
