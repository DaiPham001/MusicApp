package com.example.musicapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.Model.Topic;
import com.example.musicapp.R;

import java.util.ArrayList;

public class Adapter_All_Topic extends RecyclerView.Adapter<Adapter_All_Topic.ViewHolder_All_Topic> {
    public interface OnClick{
        void onClick(Topic topic);
    }
    private Context context;
    private ArrayList<Topic> list;
    private OnClick onClick;

    public Adapter_All_Topic(Context context, ArrayList<Topic> list,OnClick onClick) {
        this.context = context;
        this.list = list;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public ViewHolder_All_Topic onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_all_topic,parent,false);
        return new ViewHolder_All_Topic(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_All_Topic holder, int position) {
        Topic topic = list.get(position);
        Glide.with(context).load(topic.getHinhchude()).into(holder.img_topic);
        holder.img_topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onClick(topic);
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

    public class ViewHolder_All_Topic extends RecyclerView.ViewHolder {
        private ImageView img_topic;
        public ViewHolder_All_Topic(@NonNull View v) {
            super(v);
            img_topic = v.findViewById(R.id.img_top);
        }
    }
}
