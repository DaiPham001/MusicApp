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
import com.example.musicapp.Model.Playlist;
import com.example.musicapp.R;
import com.example.musicapp.Utils.Utils;

import java.util.ArrayList;

public class Adapter_AllPlaylist extends RecyclerView.Adapter<Adapter_AllPlaylist.ViewHolder_AllPlaylist> {
    public interface OnClcik{
        void onClick(Playlist playlist);
    }
    private Context context;
    private ArrayList<Playlist> list;
    private OnClcik onClcik;

    public Adapter_AllPlaylist(Context context, ArrayList<Playlist> list,OnClcik onClcik) {
        this.context = context;
        this.list = list;
        this.onClcik = onClcik;
    }

    @NonNull
    @Override
    public ViewHolder_AllPlaylist onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_allplaylist,parent,false);
        return new ViewHolder_AllPlaylist(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_AllPlaylist holder, int position) {
        Playlist playlist = list.get(position);
        holder.tv_tenplaylit_all.setText(playlist.getTenpl());
        String hinhanh = null;
        if (playlist.getHinhpl().contains("http")){
            hinhanh = playlist.getHinhpl();
        }else {
            hinhanh = Utils.BASE + "imgplaylist/hinhpl/" + playlist.getHinhpl();
        }

        Glide.with(context).load(hinhanh).into(holder.img_allplaylit);
        holder.layout_allpl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClcik.onClick(playlist);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    public class ViewHolder_AllPlaylist extends RecyclerView.ViewHolder {
        private LinearLayout layout_allpl;
        private ImageView img_allplaylit;
        private TextView tv_tenplaylit_all;
        public ViewHolder_AllPlaylist(@NonNull View v) {
            super(v);
            img_allplaylit = v.findViewById(R.id.img_allplaylit);
            tv_tenplaylit_all = v.findViewById(R.id.tv_tenplaylit_all);
            layout_allpl = v.findViewById(R.id.layout_allpl);
        }
    }
}
