package com.example.musicapp.Fragment;

import static android.content.Context.BIND_AUTO_CREATE;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.musicapp.Adapter.ViewPager_Playmusic_Adapter;
import com.example.musicapp.Model.Music;
import com.example.musicapp.R;
import com.example.musicapp.Sevice.MusicPlayerService;
import com.example.musicapp.Utils.Utils;
import com.example.musicapp.ViewModel.SharedViewModel;

import java.util.ArrayList;


public class MusicPlayerFragment extends Fragment {

    private ImageView imgAlbumArt, imgPlayPause;
    private ArrayList<Music> list = new ArrayList<>();
    private TextView tv_SongTitle, tv_ArtistName, txtCurrentTime, txtDuration;
    private SeekBar seekBar;
    private Music music;
    private ObjectAnimator rotationAnimator;
    private MusicPlayerService musicService;
    private boolean isServiceBound = false;
    private Handler handler = new Handler();

    private SharedViewModel sharedViewModel;
    public MusicPlayerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_music_player, container, false);
        addcontroll(view);
        addevenst();
        receiveData(); // Gọi hàm này để nhận dữ liệu từ Intent
        startAndBindService(); // Nếu bạn cần kết nối với Service

        // Thiết lập ViewModel để lắng nghe dữ liệu thay đổi
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getSelectedMusic().observe(getViewLifecycleOwner(), this::updateUIWithMusic);

        // Thiết lập OnCompletionListener khi service đã được kết nối
        setupOnCompletionListener();
        return view;
    }

    private void addcontroll(View v) {
       // rcv_music_play = v.findViewById(R.id.rcv_musicplay);
        imgAlbumArt = v.findViewById(R.id.imgAlbumArt);
        tv_ArtistName = v.findViewById(R.id.txtArtistName);
        tv_SongTitle = v.findViewById(R.id.tv_SongTitle);
        imgPlayPause = v.findViewById(R.id.imgPlayPause);
        txtCurrentTime = v.findViewById(R.id.txtCurrentTime);
        txtDuration = v.findViewById(R.id.txtDuration);
        seekBar = v.findViewById(R.id.seekBar);

    }

    private void addevenst() {
        imgPlayPause.setOnClickListener(v -> {
            if (isServiceBound && musicService != null) {
                if (musicService.isPlaying()) {
                    musicService.pause();
                    imgPlayPause.setImageResource(R.drawable.ic_play_pause);
                    stopRotation();
                } else {
                    musicService.resume();
                    imgPlayPause.setImageResource(R.drawable.ic_play);
                    startRotation();
                    updateSeekBar();
                }
                // Hiển thị thông báo khi bắt đầu phát nhạc
                musicService.showNotification(); // Gọi phương thức này để hiển thị thông báo
            } else {
                // Nếu dịch vụ chưa được kết nối, chỉ cần bắt đầu và kết nối dịch vụ.
                startAndBindService();
                if (musicService != null) {
                    musicService.playMusic(music); // Gọi phương thức playMusic() để phát nhạc khi nhấn vào nút
                    // Hiển thị thông báo khi bắt đầu phát nhạc
                    musicService.showNotification(); // Gọi phương thức này để hiển thị thông báo
                }
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && isServiceBound && musicService != null) {
                    musicService.seekTo(progress);
                }
                txtCurrentTime.setText(formatTime(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Không cần thiết
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (isServiceBound && musicService != null) {
                    musicService.seekTo(seekBar.getProgress());
                    updateSeekBar();
                }
            }
        });
    }

    private void setupOnCompletionListener() {
        if (musicService != null) {
            musicService.setOnCompletionListener(mp -> {
                stopRotation(); // Dừng xoay ảnh album
                imgPlayPause.setImageResource(R.drawable.ic_play_pause); // Chuyển icon về play
            });
        }
    }

    //Kết nối với Service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicPlayerService.LocalBinder binder = (MusicPlayerService.LocalBinder) service;
            musicService = binder.getService();
            isServiceBound = true;
            // Cung cấp tham chiếu tới Activity cho Service
            musicService.setMusicPlayerFragment(MusicPlayerFragment.this);
            // Đặt OnCompletionListener ở đây để đảm bảo nó được áp dụng sau khi dịch vụ được kết nối
            setupOnCompletionListener();

            // bắt đầu phát nhạc ngay lập tức nếu chưa đang phát
            if (!musicService.isPlaying()) {
                musicService.resume();// Gọi phương thức playMusic để phát nhạc
                imgPlayPause.setImageResource(R.drawable.ic_play); // Cập nhật biểu tượng nút play/pause
                startRotation(); // Bắt đầu xoay ảnh album
                updateSeekBar(); // Cập nhật SeekBar
                musicService.showNotification(); // Hiển thị thông báo
            }
            updateUI();
            updateSeekBar();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceBound = false;
        }
    };



//Nhận dữ liệu từ Intent
    private void receiveData() {
        Intent intent = getActivity() != null ? getActivity().getIntent() : null;
        if (intent == null) return;

         // Kiểm tra nếu có Extra "musichome" là một danh sách ArrayList<Music>
        if (intent.hasExtra("music_list") && intent.getSerializableExtra("music_list") instanceof ArrayList) {
            list = (ArrayList<Music>) intent.getSerializableExtra("music_list");

            if (list != null && !list.isEmpty()) {
                // Sử dụng bài hát đầu tiên trong danh sách để hiển thị trên UI
                music = list.get(0);
                tv_SongTitle.setText(music.getTenbaihat());
                tv_ArtistName.setText(music.getCasi());
                String hinhanh = music.getHinhbaihat().contains("http") ? music.getHinhbaihat() : Utils.BASE + "imgmusic/" + music.getHinhbaihat();

                // Check if fragment is added before loading image with Glide
                if (isAdded()) {
                    Glide.with(this).load(hinhanh).into(imgAlbumArt);
                }
            }
        } else if (intent.hasExtra("musichome") && intent.getSerializableExtra("musichome") instanceof Music) {
            music = (Music) intent.getSerializableExtra("musichome");

            if (music != null) {
                tv_SongTitle.setText(music.getTenbaihat());
                tv_ArtistName.setText(music.getCasi());
                String hinhanh = music.getHinhbaihat().contains("http") ? music.getHinhbaihat() : Utils.BASE + "imgmusic/" + music.getHinhbaihat();

                // Check if fragment is added before loading image with Glide
                if (isAdded()) {
                    Glide.with(this).load(hinhanh).into(imgAlbumArt);
                }
            }
        }
    }

    // click các item bai hát
    private void updateUIWithMusic(Music newMusic) {
        if (newMusic != null) {
            music = newMusic; // Cập nhật bài hát hiện tại

            // Cập nhật giao diện với bài hát mới
            tv_SongTitle.setText(music.getTenbaihat());
            tv_ArtistName.setText(music.getCasi());
            String hinhanh = music.getHinhbaihat().contains("http") ? music.getHinhbaihat() : Utils.BASE + "imgmusic/" + music.getHinhbaihat();
            if (isAdded()) {
                Glide.with(this).load(hinhanh).into(imgAlbumArt);
            }

            // Đặt lại SeekBar về 0 khi bài hát mới được chọn
            seekBar.setProgress(0);

//            // Cập nhật tổng thời gian của bài hát mới
//            updateDuration();

            // Kiểm tra và phát nhạc nếu Service đã được kết nối
            if (isServiceBound && musicService != null) {
                musicService.playMusic(music);
                musicService.showNotification();
                updateSeekBar(); // Cập nhật SeekBar
                // Cập nhật tổng thời gian của bài hát mới sau khi nhạc đã bắt đầu phát
                int duration = musicService.getDuration(); // Lấy tổng thời gian từ Service
                updateDuration(duration);
            }
        }
    }

    public void updateDuration(int duration) {
        String formattedDuration = formatTime(duration);
        txtDuration.setText(formattedDuration); // Cập nhật tổng thời gian trên giao diện
    }

    //Khởi động và kết nối với Service
    private void startAndBindService() {
        Intent serviceIntent = new Intent(getActivity(), MusicPlayerService.class);
        // Kiểm tra xem có danh sách nhạc không
        if (list != null && !list.isEmpty()){
            serviceIntent.putExtra("music_list",list);// Truyền danh sách nhạc
        }else {
            serviceIntent.putExtra("music", music);// Truyền một bài nhạc
        }

        ContextCompat.startForegroundService(getActivity(), serviceIntent);
        getActivity().bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    // Cập nhật giao diện dựa trên trạng thái của Service
    public void updateUI() {
        if (musicService != null && musicService.isPlaying()) {
            imgPlayPause.setImageResource(R.drawable.ic_play);
            startRotation();

            // Cập nhật thông tin bài hát hiện tại
            Music currentMusic = musicService.getCurrentMusic();
            if (currentMusic != null) {
                tv_SongTitle.setText(currentMusic.getTenbaihat());
                tv_ArtistName.setText(currentMusic.getCasi());
                String hinhanh = currentMusic.getHinhbaihat().contains("http") ? currentMusic.getHinhbaihat() : Utils.BASE + "imgmusic/" + currentMusic.getHinhbaihat();
                if (getActivity() != null && isAdded()) {
                    Glide.with(this).load(hinhanh).into(imgAlbumArt);
                }

            }
        } else {
            imgPlayPause.setImageResource(R.drawable.ic_play_pause);
            stopRotation();
        }

        int duration = musicService.getDuration();
        seekBar.setMax(duration);
        txtDuration.setText(formatTime(duration));
    }


    //Cập nhật SeekBar và thời gian hiện tại của bài hát
    public void updateSeekBar() {
        if (musicService != null) {
            int duration = musicService.getDuration();
            seekBar.setMax(duration); // Cập nhật lại giá trị tối đa của SeekBar theo thời gian của bài hát mới
            handler.postDelayed(updateRunnable, 1000); // Bắt đầu cập nhật lại SeekBar
        }
    }

    private Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            if (musicService != null && musicService.isPlaying()) {
                int currentPosition = musicService.getCurrentPosition();
                seekBar.setProgress(currentPosition);
                txtCurrentTime.setText(formatTime(currentPosition));
                handler.postDelayed(this, 1000);
            }
        }
    };

    //Bắt đầu hiệu ứng xoay cho ảnh album
    private void startRotation() {
        if (rotationAnimator == null) {
            rotationAnimator = ObjectAnimator.ofFloat(imgAlbumArt, "rotation", 0f, 360f);
            rotationAnimator.setDuration(10000);
            rotationAnimator.setInterpolator(new LinearInterpolator());
            rotationAnimator.setRepeatCount(ValueAnimator.INFINITE);
        }

        if (rotationAnimator.isPaused()) {
            rotationAnimator.resume(); // Tiếp tục xoay nếu nó đang bị tạm dừng
        } else if (!rotationAnimator.isRunning()) {
            rotationAnimator.start(); // Bắt đầu xoay nếu nó chưa đang chạy
        }
    }

    //   Dừng hiệu ứng xoay cho ảnh album
    private void stopRotation() {
        if (rotationAnimator != null && rotationAnimator.isRunning()) {
            rotationAnimator.pause();
        }
    }

    //Định dạng thời gian từ milliseconds sang mm:ss
    private String formatTime(int milliseconds) {
        int minutes = (milliseconds / 1000) / 60;
        int seconds = (milliseconds / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isServiceBound) {
            getActivity().unbindService(serviceConnection);
            isServiceBound = false;
        }
        handler.removeCallbacks(updateRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateSeekBar(); // Bắt đầu cập nhật seekBar khi trở lại fragment
    }
}