package com.example.musicapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.Model.Music;
import com.example.musicapp.R;
import com.example.musicapp.Utils.Utils;

import java.util.ArrayList;

public class Adapter_MusicPlay extends RecyclerView.Adapter<Adapter_MusicPlay.ViewHolder_MusicPlay> {

    private Context context;
    private ArrayList<Music> list;

    public Adapter_MusicPlay(Context context, ArrayList<Music> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder_MusicPlay onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_music_play,parent,false);
        return new ViewHolder_MusicPlay(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_MusicPlay holder, int position) {
        Music music = list.get(position);
        String indexText = String.format("%02d", position + 1);
        holder.tv_stt.setText("" + indexText);

        holder.tv_tenbh.setText(music.getTenbaihat());
        holder.tv_tencs.setText(music.getCasi());
//        String hinhanh;
//        if (music.getHinhbaihat().contains("http")){
//            hinhanh = music.getHinhbaihat();
//        }else {
//            hinhanh = Utils.BASE + "imgmusic/" + music.getHinhbaihat();
//        }
//        Glide.with(context).load(hinhanh).into(holder.img_favorite);
    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    public class ViewHolder_MusicPlay extends RecyclerView.ViewHolder {
        private TextView tv_stt,tv_tenbh,tv_tencs;
        private ImageView img_favorite;
        public ViewHolder_MusicPlay(@NonNull View v) {
            super(v);
            tv_stt = v.findViewById(R.id.tv_stt_mpl);
            tv_tenbh = v.findViewById(R.id.tv_tenbh_mpl);
            tv_tencs = v.findViewById(R.id.tv_tencs_mpl);
            img_favorite = v.findViewById(R.id.img_favorite_mpl);
        }
    }
}
