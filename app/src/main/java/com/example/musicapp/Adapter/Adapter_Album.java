package com.example.musicapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.Model.Album;
import com.example.musicapp.R;
import com.example.musicapp.Utils.Utils;

import java.util.ArrayList;

public class Adapter_Album extends RecyclerView.Adapter<Adapter_Album.ViewHolder_Album> {
    public interface OnClick{
        void onClick(Album album);
    }

    private Context context;
    private ArrayList<Album> list;
    private OnClick onClick;

    public Adapter_Album(Context context, ArrayList<Album> list,OnClick onClick) {
        this.context = context;
        this.list = list;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public ViewHolder_Album onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_album,parent,false);
        return new ViewHolder_Album(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_Album holder, int position) {
        Album album = list.get(position);
        if (album.getHinhalbum().contains("http")){
            Glide.with(context).load(album.getHinhalbum()).into(holder.img_album);
        }else {
            String hinhanh = Utils.BASE + "imgalbum/" + album.getHinhalbum();
            Glide.with(context).load(hinhanh).into(holder.img_album);
        }

        holder.tv_tenalbum.setText(album.getTenalbum());
        holder.tv_tencasi.setText(album.getTencasialbum());
        holder.layout_album_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onClick(album);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size(); // trả về số lượng item
        }
        return 0;
    }

    public class ViewHolder_Album extends RecyclerView.ViewHolder {
        private LinearLayout layout_album_home;
        private ImageView img_album;
        private TextView tv_tenalbum,tv_tencasi;
        public ViewHolder_Album(@NonNull View v) {
            super(v);
            layout_album_home = v.findViewById(R.id.layout_album_home);
            img_album = v.findViewById(R.id.img_album);
            tv_tenalbum = v.findViewById(R.id.tv_tenalbum);
            tv_tencasi = v.findViewById(R.id.tv_tencasi);
        }
    }
}
