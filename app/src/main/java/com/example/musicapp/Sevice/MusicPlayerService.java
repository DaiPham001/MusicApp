package com.example.musicapp.Sevice;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.musicapp.Activity.MusicPlayerActivity;
import com.example.musicapp.Fragment.MusicPlayerFragment;
import com.example.musicapp.Model.Music;
import com.example.musicapp.R;
import com.example.musicapp.Utils.Utils;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayerService extends Service {
    // Khai báo các hành động cho BroadcastReceiver
    public static final String ACTION_PLAY = "com.example.musicapp.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.example.musicapp.ACTION_PAUSE";
    public static final String ACTION_STOP = "com.example.musicapp.ACTION_STOP";
    public static final String ACTION_NEXT = "com.example.musicapp.ACTION_NEXT";
    public static final String ACTION_PREVIOUS = "com.example.musicapp.ACTION_PREVIOUS";
    public static final String ACTION_CLEAR = "com.example.musicapp.ACTION_CLEAR";
    // ID kênh thông báo và ID thông báo
    private static final String CHANNEL_ID = "MusicPlayerChannel";
    public static final int NOTIFICATION_ID = 1;
    private int currentMusicIndex;
    private Music currentMusic;
    private ArrayList<Music> musicList;
    private MediaPlayer mediaPlayer;
    private final IBinder binder = new LocalBinder();
    //private MusicPlayerActivity musicPlayerActivity;
    private MusicPlayerFragment musicPlayerFragment;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();

        // Lắng nghe sự kiện khi bài hát phát xong
        mediaPlayer.setOnCompletionListener(mp -> {
            // Xử lý khi bài hát kết thúc (có thể chuyển sang bài tiếp theo)
            stopSelf();
        });
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();

            // Get the music list or single music object
            if (intent.hasExtra("selected_music")) {
                Music selectedMusic = (Music) intent.getSerializableExtra("selected_music");
                playMusic(selectedMusic); // Cập nhật bài hát hiện tại và phát nó
            } else if (intent.hasExtra("music_list")) {
                musicList = (ArrayList<Music>) intent.getSerializableExtra("music_list");
                currentMusicIndex = 0;
                currentMusic = musicList.get(currentMusicIndex);
                playMusic(currentMusic);
            } else if (intent.hasExtra("music")) {
                currentMusic = (Music) intent.getSerializableExtra("music");
                playMusic(currentMusic);
            }

            // Handle the action if provided
            if (action != null) {
                handleAction(action);
            }
        }
        return START_STICKY;  // Ensures the service restarts if killed
    }

    //Xử lý các hành động play, pause, stop, next, previous
    private void handleAction(String action) {
        switch (action) {
            case ACTION_PLAY:
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    buildNotification(true);
                }
                break;
            case ACTION_PAUSE:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    buildNotification(false);
                }
                break;
            case ACTION_STOP:
                stopMusic();
                break;
            case ACTION_CLEAR:
                stopSelf();
                stopForeground(true);
                break;
            // Handle other actions if needed
        }

        if (musicPlayerFragment != null) {
            musicPlayerFragment.updateUI();
            musicPlayerFragment.updateSeekBar();
        }
    }


    //Phát nhạc từ đối tượng Music
    public void playMusic(Music music) {
        try {
            mediaPlayer.reset();
            String musicUrl = Utils.BASE + "mp3/" + music.getLinkbaihat();
            mediaPlayer.setDataSource(musicUrl);
            mediaPlayer.prepare();
            // Đặt OnCompletionListener ngay sau khi chuẩn bị và khởi động MediaPlayer
            mediaPlayer.setOnCompletionListener(mp -> {
                if (musicList != null && currentMusicIndex < musicList.size() -1){
                    currentMusicIndex++ ;
                    playMusic(musicList.get(currentMusicIndex));
                }else {
                    /// Thông báo hoạt động bài hát đã kết thúc
                    stopSelf(); // Tùy chọn: Dừng dịch vụ nếu cần
                }
            });

            mediaPlayer.start();

            // Gửi tổng thời gian bài hát đến Activity hoặc Fragment
            int duration = mediaPlayer.getDuration();
            sendDurationToUI(duration); // Một phương thức để gửi thời gian đến giao diện người dùng
            buildNotification(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Phương thức để tạm dừng phát nhạc
    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            buildNotification(false);
        }
    }

    // Phương thức để tiếp tục phát nhạc
    public void resume() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            buildNotification(true);
        }
    }

    //Dừng phát nhạc và giải phóng tài nguyên
    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            Log.d("MusicPlayerService", "Stopping foreground service");
            stopForeground(true); // Stop foreground service and remove notification
            stopSelf(); // Stop the service itself
            Log.d("MusicPlayerService", "Service stopped");
        }
    }



    //Xây dựng và hiển thị thông báo điều khiển phát nhạc
    @SuppressLint("RemoteViewLayout")
    private void buildNotification(boolean isPlaying) {
        if (currentMusic == null) {
            Log.e("MusicPlayerService", "Current music is null in buildNotification");
            return; // Exit the method if currentMusic is null
        }

        Intent notificationIntent = new Intent(this, MusicPlayerActivity.class);
        notificationIntent.putExtra("music", currentMusic);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent playPauseIntent = new Intent(this, MusicPlayerReceiver.class);
        playPauseIntent.setAction(isPlaying ? ACTION_PAUSE : ACTION_PLAY);
        PendingIntent playPausePendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                playPauseIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.item_notifi);
        remoteViews.setTextViewText(R.id.tvSongTitle, currentMusic.getTenbaihat());
        remoteViews.setTextViewText(R.id.tvArtist, currentMusic.getCasi());
        String imgURL = currentMusic.getLinkbaihat().contains("http") ? currentMusic.getHinhbaihat() : Utils.BASE + "imgmusic/" + currentMusic.getHinhbaihat();

        getBitmapFromURL(imgURL, bitmap -> {
            if (bitmap != null) {
                remoteViews.setImageViewBitmap(R.id.imgAlbumArt, bitmap);
            } else {
                remoteViews.setImageViewResource(R.id.imgAlbumArt, R.drawable.ic_music_note); // Default image
            }

            remoteViews.setImageViewResource(R.id.imgPlayPause_notifi, isPlaying ? R.drawable.ic_play : R.drawable.ic_play_pause);


            remoteViews.setOnClickPendingIntent(R.id.imgPlayPause_notifi, playPausePendingIntent);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(MusicPlayerService.this, CHANNEL_ID)
                    .setContent(remoteViews)
                    .setSmallIcon(R.drawable.ic_music_note)
                    .setContentIntent(pendingIntent)
                    .setOnlyAlertOnce(true)
                    .setOngoing(isPlaying);

            startForeground(NOTIFICATION_ID, builder.build());
        });
    }



    // Binder để Activity có thể tương tác với Service
    public class LocalBinder extends Binder {
        public MusicPlayerService getService() {
            return MusicPlayerService.this;
        }
    }


    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        if (mediaPlayer != null) {
            mediaPlayer.setOnCompletionListener(listener);
        }
    }

    //Chuyển đổi URL hình ảnh thành Bitmap
    public interface BitmapCallback {
        void onBitmapLoaded(Bitmap bitmap);
    }

    private void getBitmapFromURL(String imageUrl, final BitmapCallback callback) {
        Glide.with(getApplicationContext())
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        callback.onBitmapLoaded(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Handle the situation when the bitmap load is cleared, if necessary
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        callback.onBitmapLoaded(null); // Callback with null if the image fails to load
                    }
                });
    }

    // hàm lấy phương thức bên activity cập nhật hoạt động
    // cho notification
    public void setMusicPlayerFragment(MusicPlayerFragment musicPlayerFragment) {
        this.musicPlayerFragment = musicPlayerFragment;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;  // Trả về binder thay vì null
    }


    //Kiểm tra MediaPlayer có đang phát nhạc hay không
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    //Lấy thời lượng tổng của bài hát
    public int getDuration() {
        return mediaPlayer != null ? mediaPlayer.getDuration() : 0;
    }

    //Lấy vị trí hiện tại của bài hát
    public int getCurrentPosition() {
        return mediaPlayer != null ? mediaPlayer.getCurrentPosition() : 0;
    }

    // Method to get the currently playing music
    public Music getCurrentMusic() {
        return currentMusic;
    }

    // Chuyển đến vị trí cụ thể trong bài hát
    public void seekTo(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(position);
        }
    }
    // Phương thức để gửi tổng thời gian bài hát đến MusicPlayerFragment
    private void sendDurationToUI(int duration) {
        if (musicPlayerFragment != null) {
            musicPlayerFragment.updateDuration(duration); // Gọi phương thức updateDuration trong Fragment
        }
    }

    public void showNotification() {
        // Logic để tạo và hiển thị thông báo
        buildNotification(isPlaying());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}