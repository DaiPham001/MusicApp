package com.example.musicapp.Sevice;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
                Log.e("clear","ok");
                context.stopService(serviceIntent);
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager != null) {
                    notificationManager.cancel(MusicPlayerService.NOTIFICATION_ID);
                }
            }
        }
    }
}
