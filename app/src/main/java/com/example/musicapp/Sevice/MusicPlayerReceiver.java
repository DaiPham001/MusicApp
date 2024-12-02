package com.example.musicapp.Sevice;

import static com.example.musicapp.Sevice.MusicPlayerService.ACTION_STOP_AND_DELETE;
import static com.example.musicapp.Sevice.MusicPlayerService.NOTIFICATION_ID;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.musicapp.Database.Music_Dao;
import com.example.musicapp.Model.Music;

import java.util.ArrayList;

public class MusicPlayerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            Intent serviceIntent = new Intent(context, MusicPlayerService.class);
            if (action.equals(MusicPlayerService.ACTION_PLAY)) {
                context.startService(serviceIntent.setAction(MusicPlayerService.ACTION_PLAY));
            } else if (action.equals(MusicPlayerService.ACTION_PAUSE)) {
                context.startService(serviceIntent.setAction(MusicPlayerService.ACTION_PAUSE));
            } else if (action.equals(MusicPlayerService.ACTION_CLEAR)) {
                Log.e("clear", "ok");
                context.stopService(serviceIntent);
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager != null) {
                    notificationManager.cancel(NOTIFICATION_ID);
                }
            } else if (ACTION_STOP_AND_DELETE.equals(action)) {
                // Dừng phát nhạc (nếu bạn đang dùng MediaPlayer hoặc ExoPlayer)
                MusicPlayerService musicService = MusicPlayerService.getInstance();
                if (musicService != null) {
                    musicService.stopMusic(); // Dừng phát nhạc
                }

                // Dừng service
                Intent stopServiceIntent = new Intent(context, MusicPlayerService.class);
                context.stopService(stopServiceIntent);

                // Xóa danh sách nhạc từ DAO
                Music_Dao musicDao = new Music_Dao(context);
                musicDao.deleteAllMusicList();

                // Kiểm tra danh sách đã xóa chưa
                ArrayList<Music> deletedMusicList = musicDao.getSavedMusicList();
                if (deletedMusicList == null || deletedMusicList.isEmpty()) {
                    Log.e("onDestroy", "Music list has been successfully deleted.");
                } else {
                    Log.e("onDestroy", "Music list was not deleted.");
                }

                // Xóa notification
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(NOTIFICATION_ID);  // NOTIFICATION_ID là ID của notification bạn đã tạo
            }


        }
    }


}
