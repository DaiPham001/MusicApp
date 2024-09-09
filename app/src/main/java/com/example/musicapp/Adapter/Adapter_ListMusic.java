package com.example.musicapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Model.Music;
import com.example.musicapp.R;

import java.util.ArrayList;

public class Adapter_ListMusic extends RecyclerView.Adapter<Adapter_ListMusic.ViewHolder_ListMusic> {
    public interface OnClick{
        void onClick_Luotthich(Music music);
        void onClick_Item(Music music);
    }
    private Context context;
    private ArrayList<Music> list;
    private OnClick onClick;
    public Adapter_ListMusic(Context context, ArrayList<Music> list, OnClick onClick) {
        this.context = context;
        this.list = list;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public Adapter_ListMusic.ViewHolder_ListMusic onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_music_play,parent,false);
        return new ViewHolder_ListMusic(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_ListMusic.ViewHolder_ListMusic holder, int position) {
        Music muisic = list.get(position);
        String indexText = String.format("%02d", position + 1);
        holder.tv_stt.setText("" + indexText);

        holder.tv_tenbh.setText(muisic.getTenbaihat());
        holder.tv_tencs.setText(muisic.getCasi());

        SharedPreferences sharedPreferences = context.getSharedPreferences("MusicPrefs", Context.MODE_PRIVATE);
        boolean isColorChanged = sharedPreferences.getBoolean("color_" + muisic.getIdbaihat(), false);

        if (isColorChanged) {
            holder.img_favorite.setColorFilter(ContextCompat.getColor(context, R.color.red), PorterDuff.Mode.SRC_IN);
        } else {
            holder.img_favorite.clearColorFilter();
        }

        holder.img_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isColorChanged = sharedPreferences.getBoolean("color_" + muisic.getIdbaihat(), false);

                if (isColorChanged) {
                    // If the color is currently changed, remove the color filter
                    holder.img_favorite.clearColorFilter();
                    // Update the SharedPreferences to reflect the change
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("color_" + muisic.getIdbaihat(), false);
                    editor.apply();
                } else {
                    // If the color is not changed, apply the color filter
                    holder.img_favorite.setColorFilter(ContextCompat.getColor(context, R.color.red), PorterDuff.Mode.SRC_IN);
                    // Update the SharedPreferences to reflect the change
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("color_" + muisic.getIdbaihat(), true);
                    editor.apply();
                    // Trigger the click event
                    onClick.onClick_Luotthich(muisic);
                }
            }
        });

        holder.layout_listmusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onClick_Item(muisic);
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
    public class ViewHolder_ListMusic extends RecyclerView.ViewHolder {
        private TextView tv_stt, tv_tenbh, tv_tencs;
        private ImageView img_favorite;
        private LinearLayout layout_listmusic;
        public ViewHolder_ListMusic(@NonNull View v) {
            super(v);
            tv_stt = v.findViewById(R.id.tv_stt_mpl);
            tv_tenbh = v.findViewById(R.id.tv_tenbh_mpl);
            tv_tencs = v.findViewById(R.id.tv_tencs_mpl);
            img_favorite = v.findViewById(R.id.img_favorite_mpl);
            layout_listmusic = v.findViewById(R.id.layout_listmusic);
        }
    }

}
