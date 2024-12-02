package com.example.musicapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Model.Music;
import com.example.musicapp.R;

import java.util.ArrayList;

public class Adapter_ListMusicfm extends RecyclerView.Adapter<Adapter_ListMusicfm.ViewHolder_ListMusicfm> {

    public interface OnClick {
        void onClick(Music music);
    }

    private Context context;
    private ArrayList<Music> list;
    private OnClick onClick;

    public Adapter_ListMusicfm(Context context, ArrayList<Music> list, OnClick onClick) {
        this.context = context;
        this.list = list;
        this.onClick = onClick;
    }

    // Thêm phương thức clear để xóa danh sách
    public void clear() {
        if (list != null) {
            list.clear(); // Xóa tất cả dữ liệu trong danh sách
            notifyDataSetChanged(); // Cập nhật giao diện RecyclerView
        }
    }

    @NonNull
    @Override
    public ViewHolder_ListMusicfm onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_listmusic_fm, parent, false);
        return new ViewHolder_ListMusicfm(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_ListMusicfm holder, int position) {
        Music music = list.get(position);
        holder.tv_tenbh_fm.setText(music.getTenbaihat());
        holder.tv_tencasi_fm.setText(music.getCasi());
        holder.layout_listmusic_fm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onClick(music);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();//tra ve so luong item
        }
        return 0;
    }

    public class ViewHolder_ListMusicfm extends RecyclerView.ViewHolder {
        private TextView tv_tenbh_fm, tv_tencasi_fm;
        private LinearLayout layout_listmusic_fm;

        public ViewHolder_ListMusicfm(@NonNull View v) {
            super(v);
            tv_tencasi_fm = v.findViewById(R.id.tv_tencasi_fm);
            tv_tenbh_fm = v.findViewById(R.id.tv_tenbh_fm);
            layout_listmusic_fm = v.findViewById(R.id.layout_listmusic_fm);
        }
    }
}
