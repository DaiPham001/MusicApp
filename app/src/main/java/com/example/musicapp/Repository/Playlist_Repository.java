package com.example.musicapp.Repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.musicapp.Model.Advertisement_Model;
import com.example.musicapp.Model.Playlist_Model;
import com.example.musicapp.Retrofit.API;
import com.example.musicapp.Retrofit.RetrofitSave;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Playlist_Repository {
    private API api;

    public Playlist_Repository() {
        this.api = RetrofitSave.getRetrofit().create(API.class);
    }

    public MutableLiveData<Playlist_Model> getplaylist_modelMutableLiveData(){
        MutableLiveData<Playlist_Model> data = new MutableLiveData<>();
        api.getplaylist().enqueue(new Callback<Playlist_Model>() {
            @Override
            public void onResponse(Call<Playlist_Model> call, Response<Playlist_Model> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Playlist_Model> call, Throwable t) {
                Log.e("getplaylist",t.getMessage());
            }
        });

        return data;
    }

    public MutableLiveData<Playlist_Model> getallplaylist_modelMutableLiveData(){
        MutableLiveData<Playlist_Model> data = new MutableLiveData<>();
        api.getallplaylist().enqueue(new Callback<Playlist_Model>() {
            @Override
            public void onResponse(Call<Playlist_Model> call, Response<Playlist_Model> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Playlist_Model> call, Throwable t) {
                Log.e("getallplaylist",t.getMessage());
            }
        });

        return data;
    }
}
