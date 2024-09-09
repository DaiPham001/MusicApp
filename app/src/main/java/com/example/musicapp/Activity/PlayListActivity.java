package com.example.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.musicapp.Adapter.Adapter_AllPlaylist;
import com.example.musicapp.Model.Playlist;
import com.example.musicapp.R;
import com.example.musicapp.ViewModel.Playlist_ViewModel;

public class PlayListActivity extends AppCompatActivity implements Adapter_AllPlaylist.OnClcik {
    private Intent intent;
    private RecyclerView rcv_allpl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        addocntroll();
    }

    private void addocntroll() {
        rcv_allpl = findViewById(R.id.rcv_allpl);

        loadList();
    }

    private void loadList() {
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(this,2);
        rcv_allpl.setLayoutManager(linearLayoutManager);

        Playlist_ViewModel playlist_viewModel = new ViewModelProvider(this).get(Playlist_ViewModel.class);
        playlist_viewModel.getallplaylist().observe(this, playlist_model -> {
            if (playlist_model.isSuccess()){
                Adapter_AllPlaylist adapter_allPlaylist = new Adapter_AllPlaylist(this, playlist_model.getResult(),this);
                rcv_allpl.setAdapter(adapter_allPlaylist);
            }else {
                Log.e("loadList","null");
            }
        });
    }

    @Override
    public void onClick(Playlist playlist) {
        intent = new Intent(PlayListActivity.this, ListMusicActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("playlist",playlist);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}