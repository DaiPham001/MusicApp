package com.example.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.musicapp.Adapter.Adapter_All_Topic;
import com.example.musicapp.Model.Topic;
import com.example.musicapp.R;
import com.example.musicapp.ViewModel.CategoryTopic_ViewModel;

public class TopicActivity extends AppCompatActivity implements Adapter_All_Topic.OnClick {
    private RecyclerView rcv_topic;
    private CategoryTopic_ViewModel categoryTopic_viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        addcontroll();
    }

    private void addcontroll() {
        rcv_topic = findViewById(R.id.rcv_topic);

        loadList();
    }

    private void loadList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcv_topic.setLayoutManager(linearLayoutManager);
        categoryTopic_viewModel = new ViewModelProvider(this).get(CategoryTopic_ViewModel.class);
        categoryTopic_viewModel.getalltopic().observe(this, topic_model -> {
            if (topic_model.isSuccess()){
                Adapter_All_Topic adapter_all_topic = new Adapter_All_Topic(this, topic_model.getResult(),this);
                rcv_topic.setAdapter(adapter_all_topic);
            }else {
                Log.e("loadList","null");
            }
        });
    }

    @Override
    public void onClick(Topic topic) {
        Intent intent = new Intent(TopicActivity.this, CategoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("idchude",topic.getIdchude());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}