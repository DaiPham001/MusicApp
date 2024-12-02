package com.example.musicapp.Fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.musicapp.Activity.MusicPlayerActivity;
import com.example.musicapp.Adapter.Adapter_ListMusicfm;
import com.example.musicapp.Model.FilesMP3;
import com.example.musicapp.Model.Music;
import com.example.musicapp.R;
import com.example.musicapp.Sevice.MusicPlayerService;

import java.util.ArrayList;
import java.util.List;

public class DowloadFragment extends Fragment implements Adapter_ListMusicfm.OnClick {

    private RecyclerView rcv_dowload;
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 1;
    private Adapter_ListMusicfm adapter_listMusicfm;

    String album;
    String title;
    String duration;
    String path;
    String artist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_dowload, container, false);
        addcontroll(view);
        addevenst();
        return view;
    }

    private void addcontroll(View view) {
        rcv_dowload = view.findViewById(R.id.rcv_dowload);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcv_dowload.setLayoutManager(linearLayoutManager);

        checkPemission();
        listMusicDowoload();
    }

    private void addevenst() {

    }


    public void checkPemission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_EXTERNAL_STORAGE);
        } else {
            listMusicDowoload();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("Permission", "Granted");
                listMusicDowoload();
            } else {
                Log.e("Permission", "Denied");
                Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public ArrayList<FilesMP3> getAllAudio(Context context) {
        ArrayList<FilesMP3> tempAudiolist = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
        };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                album = cursor.getString(0);
                title = cursor.getString(1);
                duration = cursor.getString(2);
                path = cursor.getString(3);
                artist = cursor.getString(4);

                FilesMP3 musicFiles = new FilesMP3(path, title, artist, album, duration);
                Log.e("path", "album" + album);
                Log.e("title", "title" + title);
                Log.e("path", "path" + path);
                Log.e("duration", "duration" + duration);
                Log.e("artist", "artist" + artist);


                tempAudiolist.add(musicFiles);
            }
            cursor.close();
        }
        return tempAudiolist;
    }

    public void listMusicDowoload() {
        ArrayList<FilesMP3> audioFiles = getAllAudio(getContext());
        Log.e("AudioFiles", "Size: " + audioFiles.size());
        ArrayList<Music> musicList = new ArrayList<>();
        for (FilesMP3 file : audioFiles) {
            musicList.add(new Music(file.getTitle(), file.getPath(), file.getArtist(), file.getAlbum()));
        }
        if (!musicList.isEmpty()) {
            adapter_listMusicfm = new Adapter_ListMusicfm(getContext(), musicList, this);
            rcv_dowload.setAdapter(adapter_listMusicfm);
            if (rcv_dowload.getAdapter() != null) {
                Log.e("RecyclerView", "Adapter is set");
            } else {
                Log.e("RecyclerView", "Adapter is null");
            }
        } else {
            Log.e("musiclist", "empty");
        }
    }


    @Override
    public void onClick(Music music) {
        // Gửi dữ liệu bài hát đã chọn đến MusicPlayerService để phát nhạc
        Intent intent = new Intent(getContext(), MusicPlayerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("musichome", music);
        intent.putExtras(bundle);
        getActivity().startService(intent);
        Log.e("ok", "ok");
    }
}