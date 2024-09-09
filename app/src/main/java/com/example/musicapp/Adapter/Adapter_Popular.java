package com.example.musicapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.Model.Music;
import com.example.musicapp.R;
import com.example.musicapp.Utils.Utils;

import java.util.ArrayList;


public class Adapter_Popular extends RecyclerView.Adapter<Adapter_Popular.ViewHolder_Popular> {
    public interface OnClick_Luotthich{
        void onClick_Luotthich(Music music);
        void onClickItem(Music music);
    }
    private Context context;
    private ArrayList<Music> list;
    private OnClick_Luotthich onClick_luotthich;

    public Adapter_Popular(Context context, ArrayList<Music> list,OnClick_Luotthich onClick_luotthich) {
        this.context = context;
        this.list = list;
        this.onClick_luotthich = onClick_luotthich;
    }

    @NonNull
    @Override
    public ViewHolder_Popular onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_popular,parent,false);
        return new ViewHolder_Popular(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_Popular holder, int position) {
        Music muisic = list.get(position);
        String hinhanh;
        if (muisic.getHinhbaihat().contains("http")) {
            hinhanh = muisic.getHinhbaihat();
        } else {
            hinhanh = Utils.BASE + "imgmusic/" + muisic.getHinhbaihat();
        }
        Log.d("Image URL", hinhanh);
        Glide.with(context).load(hinhanh).into(holder.img_hinhanh);

        holder.tv_tencs.setText(muisic.getCasi());
        holder.tv_tenbh.setText(muisic.getTenbaihat());

        SharedPreferences sharedPreferences = context.getSharedPreferences("MusicPrefs", Context.MODE_PRIVATE);
        boolean isColorChanged = sharedPreferences.getBoolean("color_" + muisic.getIdbaihat(), false);

        if (isColorChanged) {
            holder.img_pop.setColorFilter(ContextCompat.getColor(context, R.color.red), PorterDuff.Mode.SRC_IN);
        } else {
            holder.img_pop.clearColorFilter();
        }

        holder.img_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isColorChanged = sharedPreferences.getBoolean("color_" + muisic.getIdbaihat(), false);

                if (isColorChanged) {
                    // If the color is currently changed, remove the color filter
                    holder.img_pop.clearColorFilter();
                    // Update the SharedPreferences to reflect the change
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("color_" + muisic.getIdbaihat(), false);
                    editor.apply();
                } else {
                    // If the color is not changed, apply the color filter
                    holder.img_pop.setColorFilter(ContextCompat.getColor(context, R.color.red), PorterDuff.Mode.SRC_IN);
                    // Update the SharedPreferences to reflect the change
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("color_" + muisic.getIdbaihat(), true);
                    editor.apply();
                    // Trigger the click event
                    onClick_luotthich.onClick_Luotthich(muisic);
                }


            }
        });

        holder.layout_popular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_luotthich.onClickItem(muisic);
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

    public class ViewHolder_Popular extends RecyclerView.ViewHolder {
        public ImageView img_hinhanh,img_pop;
        public TextView tv_tenbh, tv_tencs;
        private LinearLayout layout_popular;
        public ViewHolder_Popular(@NonNull View v) {
            super(v);
            img_hinhanh = v.findViewById(R.id.img_hinhanh_popular);
            img_pop = v.findViewById(R.id.img_popular);
            tv_tenbh = v.findViewById(R.id.tv_tenbh_pop);
            tv_tencs = v.findViewById(R.id.tv_tencs_pop);
            layout_popular = v.findViewById(R.id.layout_popular);
        }
    }
}
