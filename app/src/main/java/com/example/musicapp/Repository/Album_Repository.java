package com.example.musicapp.Repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.musicapp.Model.Album_Model;
import com.example.musicapp.Model.CategoryendTopic_Model;
import com.example.musicapp.Model.Music_Model;
import com.example.musicapp.Retrofit.API;
import com.example.musicapp.Retrofit.RetrofitSave;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Album_Repository {
    private API api;

    public Album_Repository(){
        this.api = RetrofitSave.getRetrofit().create(API.class);
    }

    public MutableLiveData<Album_Model> getalbum_modelMutableLiveData(){
        MutableLiveData<Album_Model> data = new MutableLiveData<>();
        api.getalbum().enqueue(new Callback<Album_Model>() {
            @Override
            public void onResponse(Call<Album_Model> call, Response<Album_Model> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Album_Model> call, Throwable t) {
                Log.e("getalnum",t.getMessage());
            }
        });
        return data;
    }

    public MutableLiveData<Album_Model> getallalbum_modelMutableLiveData(){
        MutableLiveData<Album_Model> data = new MutableLiveData<>();
        api.getallalbum().enqueue(new Callback<Album_Model>() {
            @Override
            public void onResponse(Call<Album_Model> call, Response<Album_Model> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Album_Model> call, Throwable t) {
                Log.e("getallalbum",t.getMessage());
            }
        });
        return data;
    }

}
