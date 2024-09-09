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
import com.example.musicapp.Model.Category;
import com.example.musicapp.R;

import java.util.ArrayList;

public class Adapter_Category extends RecyclerView.Adapter<Adapter_Category.ViewHolder_Category> {

    private Context context;
    private ArrayList<Category> list;

    public Adapter_Category(Context context, ArrayList<Category> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder_Category onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category,parent,false);
        return new ViewHolder_Category(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_Category holder, int position) {
        Category category = list.get(position);
        Glide.with(context).load(category.getHinhtheloai()).into(holder.img_category);
        holder.tv_cate.setText(category.getTentheloai());
    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    public class ViewHolder_Category extends RecyclerView.ViewHolder {
        private ImageView img_category;
        private TextView tv_cate;
        public ViewHolder_Category(@NonNull View v) {
            super(v);
            img_category = v.findViewById(R.id.img_category);
            tv_cate = v.findViewById(R.id.tv_cate);
        }
    }
}
