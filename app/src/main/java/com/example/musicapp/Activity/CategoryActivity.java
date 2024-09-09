package com.example.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.musicapp.Adapter.Adapter_Category;
import com.example.musicapp.R;
import com.example.musicapp.ViewModel.CategoryTopic_ViewModel;

public class CategoryActivity extends AppCompatActivity {
    private RecyclerView rcv_cate;
    private int idchude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        addcontroll();
    }

    private void addcontroll() {
        rcv_cate = findViewById(R.id.rcv_cate);

        // nha du lieu tu TopicActivity
        Intent intent = getIntent();
        idchude = intent.getIntExtra("idchude",-1);

        loadListCate();
    }

    private void loadListCate() {
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(this, 2);
        rcv_cate.setLayoutManager(linearLayoutManager);
        CategoryTopic_ViewModel categoryTopic_viewModel = new ViewModelProvider(this).get(CategoryTopic_ViewModel.class);
        categoryTopic_viewModel.getcategory(idchude).observe(this, category_model -> {
            if (category_model.isSuccess()){
                Adapter_Category adapter_category = new Adapter_Category(this, category_model.getResult());
                rcv_cate.setAdapter(adapter_category);
            }else {
                Log.e("loadListCate","null");
            }
        });
    }
}