package com.example.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.musicapp.Adapter.Adapter_All_Album;
import com.example.musicapp.Model.Album;
import com.example.musicapp.R;
import com.example.musicapp.ViewModel.Album_ViewModel;

public class AlbumActivity extends AppCompatActivity implements Adapter_All_Album.OnClick {

    private RecyclerView rcv_all_album;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        addcontroll();
    }

    private void addcontroll() {
        rcv_all_album = findViewById(R.id.rcv_all_album);
        loadListAllAlbum();
    }

    private void loadListAllAlbum() {
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(this, 2);
        rcv_all_album.setLayoutManager(linearLayoutManager);
        Album_ViewModel album_viewModel = new ViewModelProvider(this).get(Album_ViewModel.class);
        album_viewModel.getallalbum().observe(this, album_model -> {
            if (album_model.isSuccess()) {
                Adapter_All_Album adapter_all_album = new Adapter_All_Album(this, album_model.getResult(), this);
                rcv_all_album.setAdapter(adapter_all_album);
            } else {
                Log.e("loadListAllAlbum", "null");
            }
        });
    }


    // add hoat dong cho all ietm album
    @Override
    public void onClick(Album album) {
        Intent intent = new Intent(this, ListMusicActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("idalbum", album.getIdalbum());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}