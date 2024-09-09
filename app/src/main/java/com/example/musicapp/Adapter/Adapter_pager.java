package com.example.musicapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.musicapp.Model.Advertisement;
import com.example.musicapp.R;
import com.example.musicapp.Utils.Utils;

import java.util.ArrayList;

public class Adapter_pager extends PagerAdapter {
    public interface Onclick {
        void onClick (Advertisement advertisement);
    }
    private Context context;
    private ArrayList<Advertisement> list;
    private Onclick onclick;

    public Adapter_pager(Context context, ArrayList<Advertisement> list, Onclick onclick) {
        this.context = context;
        this.list = list;
        this.onclick = onclick;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_page, container, false);
        Advertisement advertisement = list.get(position);
        ImageView img_pager = view.findViewById(R.id.img_page);
        ImageView icon_nho = view.findViewById(R.id.icon_nho);
        TextView tv_tenbh_page = view.findViewById(R.id.tv_tenbh_page);
        TextView tv_body_page = view.findViewById(R.id.tv_body_page);

        if (list != null) {
            if (advertisement.getHinhanhqc().contains("http")) {
                Glide.with(context).load(advertisement.getHinhanhqc()).into(img_pager);
            } else {
                String hinhanh = Utils.BASE + "images/" + advertisement.getHinhanhqc();
                Glide.with(context).load(hinhanh).into(img_pager);
            }

            if (advertisement.getHinhbaihat().contains("http")) {
                Glide.with(context).load(advertisement.getHinhbaihat()).into(icon_nho);
            } else {
                String hinhanh = Utils.BASE + "imgmusic/" + advertisement.getHinhbaihat();
                Glide.with(context).load(hinhanh).into(icon_nho);
            }

//            Log.e("hinhanh", advertisement.getHinhanhqc());
//            Log.e("iconbaihat", advertisement.getHinhbaihat());
            tv_tenbh_page.setText(advertisement.getTenbaihat());
            tv_body_page.setText(advertisement.getNoidungqc());
        }

        // xu ly hoat dong viewpage
        img_pager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick.onClick(advertisement);
            }
        });
        container.addView(view);
        return view;
    }

//    // Method to update data
//    public void updateData(ArrayList<String> newList) {
//        this.list = newList;
//        notifyDataSetChanged();
//    }
}
