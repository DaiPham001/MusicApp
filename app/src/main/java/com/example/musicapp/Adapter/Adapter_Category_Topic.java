package com.example.musicapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.Model.Category;
import com.example.musicapp.Model.Item_CaTop;
import com.example.musicapp.Model.Topic;
import com.example.musicapp.R;
import com.example.musicapp.Utils.Utils;

import java.util.ArrayList;

public class Adapter_Category_Topic extends RecyclerView.Adapter<Adapter_Category_Topic.ViewHolder_Category_Topic> {
    public interface OnClick{
        void onClick(Item_CaTop item_caTop);
    }
    private Context context;
    private ArrayList<Item_CaTop> list;
    private OnClick onClick;
    public Adapter_Category_Topic(Context context, ArrayList<Item_CaTop> list,OnClick onClick) {
        this.context = context;
        this.list = list;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public ViewHolder_Category_Topic onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_catop,parent,false);
        return new ViewHolder_Category_Topic(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_Category_Topic holder, int position) {
        Item_CaTop item = list.get(position);
        String imageUrl = null;

        if (item.getCategory() != null) {
            Category category = item.getCategory();
            imageUrl = category.getHinhtheloai();
            //Log.e("hinhtheloai", category.getTentheloai());
        } else if (item.getTopic() != null) {
            Topic topic = item.getTopic();
            imageUrl = topic.getHinhchude();
            //Log.e("hinhchude", topic.getTenchude());
        }

        if (imageUrl != null && imageUrl.contains("http")) {
            Glide.with(context).load(imageUrl).into(holder.img_catetop);
        } else if (imageUrl != null) {
            String fullUrl = Utils.BASE + "imgcatetop/" + imageUrl;
            Glide.with(context).load(fullUrl).into(holder.img_catetop);
        }
        //Log.e("hinhanh",item.getCategory().getHinhtheloai());
        holder.img_catetop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onClick(item);
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

    public class ViewHolder_Category_Topic extends RecyclerView.ViewHolder {
        private ImageView img_catetop;
        public ViewHolder_Category_Topic(@NonNull View v) {
            super(v);
            img_catetop = v.findViewById(R.id.img_catetop);
        }
    }
}
