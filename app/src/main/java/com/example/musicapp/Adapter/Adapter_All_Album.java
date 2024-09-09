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

public class Adapter_All_Album extends RecyclerView.Adapter<Adapter_All_Album.ViewHolder_All_Album> {

    public interface OnClick {
        void onClick(Album album);
    }

    private Context context;
    private ArrayList<Album> list;
    private OnClick onClick;

    public Adapter_All_Album(Context context, ArrayList<Album> list, OnClick onClick) {
        this.context = context;
        this.list = list;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public ViewHolder_All_Album onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_all_album, parent, false);
        return new ViewHolder_All_Album(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_All_Album holder, int position) {
        Album album = list.get(position);
        String hinhanh = null;
        if (album.getHinhalbum().contains("http")) {
            hinhanh = album.getHinhalbum();
        } else {
            hinhanh = Utils.BASE + "imgalbum" + album.getHinhalbum();
        }

        Glide.with(context).load(hinhanh).into(holder.img_allalbum);
        holder.tv_tenall_album.setText(album.getTenalbum());
        holder.layout_allab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onClick(album);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public class ViewHolder_All_Album extends RecyclerView.ViewHolder {
        private LinearLayout layout_allab;
        private ImageView img_allalbum;
        private TextView tv_tenall_album;

        public ViewHolder_All_Album(@NonNull View v) {
            super(v);
            layout_allab = v.findViewById(R.id.layout_allab);
            tv_tenall_album = v.findViewById(R.id.tv_tenall_album);
            img_allalbum = v.findViewById(R.id.img_allalbum);
        }
    }
}
