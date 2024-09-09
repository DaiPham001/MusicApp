package com.example.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.musicapp.Adapter.Adapter_ListMusic;
import com.example.musicapp.Adapter.Adapter_MusicPlay;
import com.example.musicapp.Fragment.MusicPlayerFragment;
import com.example.musicapp.Model.Music;
import com.example.musicapp.Model.Playlist;
import com.example.musicapp.R;
import com.example.musicapp.Sevice.MusicPlayerService;
import com.example.musicapp.Utils.Utils;
import com.example.musicapp.ViewModel.Album_ViewModel;
import com.example.musicapp.ViewModel.Music_ViewModel;

import java.util.ArrayList;

public class ListMusicActivity extends AppCompatActivity implements Adapter_ListMusic.OnClick {

    private ImageView img_listmusic;
    private TextView tv_ten_listmusic;
    private LinearLayout layout_alllistmusic;
    private RecyclerView rcv_listmusic;
    private Playlist playlist;
    private Adapter_ListMusic adapter_listMusic;
    private ArrayList<Music> list = new ArrayList<>();
    private int idqc, idtheloai, idalbum;
    private Music_ViewModel music_viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_music);
        addcontroll();
        addevenst();
    }

    // anh xa view
    private void addcontroll() {
        tv_ten_listmusic = findViewById(R.id.tv_ten_listmusic);
        rcv_listmusic = findViewById(R.id.rcv_listmusic);
        img_listmusic = findViewById(R.id.img_listmusic);
        layout_alllistmusic = findViewById(R.id.layout_alllistmusic);
        music_viewModel = new ViewModelProvider(this).get(Music_ViewModel.class);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcv_listmusic.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();
        // nhan idplaylist tu homefragment
        playlist = (Playlist) intent.getSerializableExtra("playlist");
        idqc = intent.getIntExtra("idqc", -1);
        idtheloai = intent.getIntExtra("idtheloai", -1);
        idalbum = intent.getIntExtra("idalbum", -1);
        if (playlist != null) {
            loadListMusic();
            loadData();
        } else if (idqc >= 0) {
            loadListMusicPlay_Adven();
            //loadData();
        } else if (idtheloai >= 0) {
            loadListMusicPlay_Cate_Top();
        } else if (idalbum >= 0) {
            loadListMusicPlay_Album();
        }

    }


    private void addevenst() {
        layout_alllistmusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListMusicActivity.this, MusicPlayerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("music_list", list);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void loadListMusicPlay_Album() {
        music_viewModel.getmusic_album(idalbum).observe(this, music_model -> {
            if (music_model.isSuccess()) {
                Adapter_ListMusic adapter_listMusic = new Adapter_ListMusic((Context) this, music_model.getResult(), this);
                rcv_listmusic.setAdapter(adapter_listMusic);
                list = music_model.getResult();
                loadData();
            } else {
                Log.e("loadListMusicPlay_Cate_Top", "null");
            }
        });
    }

    private void loadListMusicPlay_Cate_Top() {
        music_viewModel.getmusic_cate_top(idtheloai).observe(this, music_model -> {
            if (music_model.isSuccess()) {
                adapter_listMusic = new Adapter_ListMusic(this, music_model.getResult(), this);
                rcv_listmusic.setAdapter(adapter_listMusic);
                list = music_model.getResult();
                loadData();
            } else {
                Log.e("loadListMusicPlay_Cate_Top", "null");
            }
        });
    }

    private void loadListMusic() {
        music_viewModel.getmusic_playlist(playlist.getIdplaylist()).observe(this, music_model -> {
            if (music_model.isSuccess()) {
                adapter_listMusic = new Adapter_ListMusic(this, music_model.getResult(), this);
                list = music_model.getResult();
                rcv_listmusic.setAdapter(adapter_listMusic);
                Log.e("loadListMusic", String.valueOf(playlist.getIdplaylist()));
            } else {
                Log.e("loadListMusic", "null");
            }
        });
    }

    private void loadListMusicPlay_Adven() {
        music_viewModel = new ViewModelProvider(this).get(Music_ViewModel.class);
        music_viewModel.getmusic_qc(idqc).observe(this, music_model -> {
            if (music_model.isSuccess()) {
                adapter_listMusic = new Adapter_ListMusic(this, music_model.getResult(), this);
                list = music_model.getResult();
                //Log.e("hinhanh", String.valueOf(music_model.getResult().get(0).getHinhbaihat()));
                rcv_listmusic.setAdapter(adapter_listMusic);
                loadData();
            } else {
                Log.e("loadListMusicPlay", "null");
            }
        });
    }

    private void loadData() {
        String hinhanh = null;
        //String tenbh = null;
        if (playlist != null) {
            if (playlist.getHinhpl().contains("http")) {
                hinhanh = playlist.getHinhpl();
            } else {
                hinhanh = Utils.BASE + "imgplaylist/hinhpl/" + playlist.getHinhpl();
            }
            tv_ten_listmusic.setText(playlist.getTenpl());
        } else if (idqc >= 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getHinhbaihat().contains("http")) {
                    hinhanh = list.get(i).getHinhbaihat();
                } else {
                    hinhanh = Utils.BASE + "imgmusic/" + list.get(i).getHinhbaihat();
                }
                tv_ten_listmusic.setText(list.get(i).getTenbaihat());
            }
        } else if (idtheloai >= 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getHinhbaihat().contains("http")) {
                    hinhanh = list.get(i).getHinhbaihat();
                } else {
                    hinhanh = Utils.BASE + "imgmusic/" + list.get(i).getHinhbaihat();
                }
                tv_ten_listmusic.setText(list.get(i).getTenbaihat());
            }
        } else if (idalbum >= 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getHinhbaihat().contains("http")) {
                    hinhanh = list.get(i).getHinhbaihat();
                } else {
                    hinhanh = Utils.BASE + "imgalbum/" + list.get(i).getHinhbaihat();
                }
                tv_ten_listmusic.setText(list.get(i).getTenbaihat());
            }
        }
        Log.e("hinhanh", hinhanh);
        Glide.with(this).load(hinhanh).into(img_listmusic);
    }

    @Override
    public void onClick_Luotthich(Music music) {
        Log.e("luotthich", String.valueOf(music.getLuotthich()));
        Log.e("color", "true");
        music_viewModel.update_luotthich(music.getIdbaihat()).observe(this, music_model -> {
            if (music_model.isSuccess()) {
                Log.e("luotthich_update", String.valueOf(music.getLuotthich()));
                if (playlist != null) {
                    loadListMusic();
                    loadData();
                } else if (idqc >= 0) {
                    loadListMusicPlay_Adven();
                    //loadData();
                } else if (idtheloai >= 0) {
                    loadListMusicPlay_Cate_Top();
                } else if (idalbum >= 0) {
                    loadListMusicPlay_Album();
                }
            } else {
                Log.e("onClick_Luotthich", "null");
            }
        });
    }

    @Override
    public void onClick_Item(Music music) {
        // Start the MusicPlayerActivity with the selected music object
        Intent intent = new Intent(this, MusicPlayerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("musichome", music);
        intent.putExtras(bundle);
        startActivity(intent);

//        // Pass data to the fragment in the current activity
//        MusicPlayerFragment fragment = new MusicPlayerFragment();
//        Bundle fragmentArgs = new Bundle();
//        fragmentArgs.putSerializable("musichome", music);
//        fragment.setArguments(fragmentArgs);
//
//        // Ensure the correct container ID is used
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.viewpager, fragment) // Ensure this ID matches the container in your layout
//                .commit();
    }
}