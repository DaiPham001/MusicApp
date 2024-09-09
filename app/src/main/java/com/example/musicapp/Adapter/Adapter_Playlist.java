package com.example.musicapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.Model.Playlist;
import com.example.musicapp.R;
import com.example.musicapp.Utils.Utils;

import java.util.ArrayList;

public class Adapter_Playlist extends RecyclerView.Adapter<Adapter_Playlist.ViewHolder_Playlist> {
    public interface Onclick{
        void onClick(Playlist playlist);
    }
    private Context context;
    private ArrayList<Playlist> list;
    private Onclick  onclick;

    public Adapter_Playlist(Context context, ArrayList<Playlist> list,Onclick onclick) {
        this.context = context;
        this.list = list;
        this.onclick = onclick;
    }

    @NonNull
    @Override
    public ViewHolder_Playlist onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_playlist, parent, false);
        return new ViewHolder_Playlist(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_Playlist holder, int position) {
        Playlist playlist = list.get(position);
        if (playlist.getHinhpl().contains("http")) {
            Glide.with(context).load(playlist.getHinhpl()).into(holder.img_hinhpl);
        } else {
            String hinhanh = Utils.BASE + "imgplaylist/hinhpl/" + playlist.getHinhpl();
            Glide.with(context).load(hinhanh).into(holder.img_hinhpl);
        }

        if (playlist.getHinhiconpl().contains("http")) {
            Glide.with(context).load(playlist.getHinhiconpl()).into(holder.img_icon);
        } else {
            String hinhanh = Utils.BASE + "imgplaylist/iconpl/" + playlist.getHinhiconpl();
            Glide.with(context).load(hinhanh).into(holder.img_icon);
//            Log.e("icon",playlist.getHinhiconpl());
        }
//        Log.e("hinhpl",playlist.getHinhpl());
        holder.tv_tenpl.setText(playlist.getTenpl());

        holder.layout_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick.onClick(playlist);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();// số lượng item
        }
        return 0;
    }

    public class ViewHolder_Playlist extends RecyclerView.ViewHolder {
        private ConstraintLayout layout_playlist;
        private ImageView img_hinhpl, img_icon;
        private TextView tv_tenpl;

        public ViewHolder_Playlist(@NonNull View v) {
            super(v);
            img_hinhpl = v.findViewById(R.id.img_hinhpl);
            img_icon = v.findViewById(R.id.img_icon_pl);
            tv_tenpl = v.findViewById(R.id.tv_tenpl);
            layout_playlist = v.findViewById(R.id.layout_playlist);
        }
    }
}
