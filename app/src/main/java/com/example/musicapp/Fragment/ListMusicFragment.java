package com.example.musicapp.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.musicapp.Adapter.Adapter_ListMusicfm;
import com.example.musicapp.Model.Music;
import com.example.musicapp.R;
import com.example.musicapp.Sevice.MusicPlayerService;
import com.example.musicapp.Sevice.SpacingItemDecoration;
import com.example.musicapp.Utils.Utils;
import com.example.musicapp.ViewModel.SharedViewModel;

import java.util.ArrayList;


public class ListMusicFragment extends Fragment implements Adapter_ListMusicfm.OnClick {

    private RecyclerView rcv_listmusic_fm;
    private Intent intent;
    private ArrayList<Music> list = new ArrayList<>();
    private Music music;
    private Adapter_ListMusicfm adapter_listMusicfm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_list_music, container, false);
        addcontroll(view);
        receiveData();
        loadListMusic();
        return view;
    }

    // ánh xạ view
    private void addcontroll(View v) {
        rcv_listmusic_fm = v.findViewById(R.id.rcv_listmusic_fm);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        rcv_listmusic_fm.addItemDecoration(new SpacingItemDecoration(spacingInPixels));
    }

    //Nhận dữ liệu từ Intent
    private void receiveData() {
        intent = getActivity() != null ? getActivity().getIntent() : null;
        if (intent == null) return;

        // Kiểm tra nếu có Extra "musichome" là một danh sách ArrayList<Music>
        if (intent.hasExtra("music_list") && intent.getSerializableExtra("music_list") instanceof ArrayList) {
            list = (ArrayList<Music>) intent.getSerializableExtra("music_list");

            if (list != null && !list.isEmpty()) {
                // Sử dụng bài hát đầu tiên trong danh sách để hiển thị trên UI
                music = list.get(0);
                Log.e("MusicPlayerActivity", "Received music_list with size: " + list.size());
            } else {
                Log.e("MusicPlayerActivity", "music_list is null");
            }
        } else if (intent.hasExtra("musichome") && intent.getSerializableExtra("musichome") instanceof Music) {
            music = (Music) intent.getSerializableExtra("musichome");

            if (music != null) {
                Log.e("MusicPlayerActivity", "Received single music object: " + music.getHinhbaihat());
                list.add(music);
            } else {
                Log.e("MusicPlayerActivity", "musichome is null");
            }
        }
    }

    private void loadListMusic() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcv_listmusic_fm.setLayoutManager(linearLayoutManager);
        adapter_listMusicfm = new Adapter_ListMusicfm(getContext(),list, this);
        rcv_listmusic_fm.setAdapter(adapter_listMusicfm);
    }

    @Override
    public void onClick(Music music) {
        // Gửi dữ liệu bài hát đã chọn đến MusicPlayerService để phát nhạc
        intent = new Intent(getContext(), MusicPlayerService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("selected_music", music);
        intent.putExtras(bundle);
        getActivity().startService(intent);

        // Cập nhật SharedViewModel để MusicPlayerFragment nhận được bài hát mới
        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.selectMusic(music);

    }
}