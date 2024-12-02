package com.example.musicapp.Sevice;

import static com.example.musicapp.Sevice.Myapplocation.channel_id;

import android.annotation.SuppressLint;
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
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.musicapp.Activity.MusicPlayerActivity;
import com.example.musicapp.Database.Music_Dao;
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
    public static final String ACTION_STOP_AND_DELETE = "ACTION_STOP_SERVICE";
    // ID kênh thông báo và ID thông báo
    private static final String CHANNEL_ID = "MusicPlayerChannel";
    public static final int NOTIFICATION_ID = 1;
    private int currentMusicIndex;
    private Music currentMusic;
    private ArrayList<Music> musicList;
    private MediaPlayer mediaPlayer;
    private final IBinder binder = new LocalBinder();
    private Music_Dao musicDao;
    private MusicPlayerFragment musicPlayerFragment;


    private static MusicPlayerService instance; // Biến static giữ tham chiếu tới service

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        instance = this; // Khởi tạo instance khi service được tạo

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

            // Lấy dữ liệu từ SQLite nếu musicList và currentMusic bị null
            if (musicList == null || currentMusic == null) {
                musicDao = new Music_Dao(getApplicationContext());
                musicList = musicDao.getSavedMusicList(); // Lấy danh sách nhạc đã lưu
                currentMusic = musicList != null && !musicList.isEmpty() ? musicList.get(0) : null;
            }

            // Phát nhạc được chọn hoặc khôi phục trạng thái từ dữ liệu đã lưu
            // Kiểm tra và lấy dữ liệu từ intent
            if (intent.hasExtra("selected_music")) {
                currentMusic = (Music) intent.getSerializableExtra("selected_music");
                if (currentMusic != null) {
                    playMusic(currentMusic);
                } else {
                    Log.e("MusicPlayerService", "selected_music is null");
                    stopSelf();
                    return START_NOT_STICKY;
                }
            }else if (intent.hasExtra("musichome")) {
                currentMusic = (Music) intent.getSerializableExtra("musichome");
                if (currentMusic != null) {
                    playMusic(currentMusic);
                } else {
                    Log.e("MusicPlayerService", "musichome is null");
                    stopSelf();
                    return START_NOT_STICKY;
                }
            } else if (intent.hasExtra("music_list")) {
                musicList = (ArrayList<Music>) intent.getSerializableExtra("music_list");
                if (musicList != null && !musicList.isEmpty()) {
                    currentMusicIndex = 0;
                    currentMusic = musicList.get(currentMusicIndex);
                    playMusic(currentMusic);
                } else {
                    Log.e("MusicPlayerService", "music_list is null or empty");
                    stopSelf();
                    return START_NOT_STICKY;
                }
            } else if (currentMusic == null) {
                Log.e("MusicPlayerService", "No music to play and currentMusic is null");
                stopSelf();
                return START_NOT_STICKY;
            }

            if (action != null) {
                handleAction(action);
            }
        }
        return START_STICKY;
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
        if (music == null) {
            Log.e("MusicPlayerService", "Music is null, cannot play");
            stopSelf(); // Dừng service nếu không có bài nhạc hợp lệ
            return;
        }
        try {
            mediaPlayer.reset();
            String musicUrl = music.getLinkbaihat().contains("storage/emulated")
                    ? music.getLinkbaihat()
                    : Utils.BASE + "mp3/" + music.getLinkbaihat();

            mediaPlayer.setDataSource(musicUrl);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(mp -> {
                if (musicList != null && currentMusicIndex < musicList.size() - 1) {
                    currentMusicIndex++;
                    playMusic(musicList.get(currentMusicIndex));
                } else {
                    stopSelf();
                }
            });
            mediaPlayer.start();

            int duration = mediaPlayer.getDuration();
            sendDurationToUI(duration);
            buildNotification(true);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MusicPlayerService", "Error playing music: " + e.getMessage());
            stopSelf();
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
//    public void stopMusic() {
//        if (mediaPlayer != null) {
//            mediaPlayer.stop();
//            mediaPlayer.release();
//            Log.d("MusicPlayerService", "Stopping foreground service");
//            stopForeground(true); // Stop foreground service and remove notification
//            stopSelf(); // Stop the service itself
//            Log.d("MusicPlayerService", "Service stopped");
//        }
//    }

    public void stopMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        // Dừng foreground và xóa notification
        stopForeground(true);  // True để xóa notification
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

        // Intent để dừng service và xóa danh sách nhạc từ DAO
        Intent stopIntent = new Intent(this, MusicPlayerReceiver.class);
        stopIntent.setAction(ACTION_STOP_AND_DELETE); // Hành động dừng và xóa
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(
                this,
                1,  // Sử dụng requestCode khác
                stopIntent,
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

            // Sự kiện khi nhấn vào img_cancel
            remoteViews.setOnClickPendingIntent(R.id.img_cancel, stopPendingIntent);

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

    // hàm lưu list music vào DAO khi kill app
    public void saveMusic(ArrayList<Music> arrayList) {
        Music_Dao music_dao = new Music_Dao(getApplicationContext());
        boolean check = music_dao.saveMusicList(arrayList);
        if (check) {
            Log.e("check", String.valueOf(check));
        } else {
            Log.e("check", String.valueOf(check));
        }
    }

    // hàm lấy phương thức bên activity cập nhật hoạt động
    // cho notification
    public void setMusicPlayerFragment(MusicPlayerFragment musicPlayerFragment) {
        this.musicPlayerFragment = musicPlayerFragment;
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
    public IBinder onBind(Intent intent) {
        return binder;  // Trả về binder thay vì null
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        instance = null; // Xóa tham chiếu instance khi service bị hủy
        // Xóa danh sách nhạc từ DAO
        musicDao.deleteAllMusicList();

        // Lấy danh sách nhạc sau khi xóa để kiểm tra
        //ArrayList<Music> deletedMusicList = musicDao.getSavedMusicList();
//        if (deletedMusicList == null || deletedMusicList.isEmpty()) {
//            Log.e("onDestroy", "Music list has been successfully deleted.");
//        } else {
//            Log.e("onDestroy", "Music list was not deleted.");
//        }
    }

    // Phương thức static để lấy instance của service
    public static MusicPlayerService getInstance() {
        return instance;
    }

}