package com.example.musicapp.Repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.musicapp.Model.Music_Model;
import com.example.musicapp.Retrofit.API;
import com.example.musicapp.Retrofit.RetrofitSave;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Search_Repository {
    private API api;

    public Search_Repository( ) {
        this.api = RetrofitSave.getRetrofit().create(API.class);
    }


    public MutableLiveData<Music_Model> searchMusic_modelMutableLiveData(String search){
        MutableLiveData<Music_Model> data = new MutableLiveData<>();
        api.search(search).enqueue(new Callback<Music_Model>() {
            @Override
            public void onResponse(Call<Music_Model> call, Response<Music_Model> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Music_Model> call, Throwable t) {
                Log.e("search",t.getMessage());
            }
        });
        return data;
    }
}
