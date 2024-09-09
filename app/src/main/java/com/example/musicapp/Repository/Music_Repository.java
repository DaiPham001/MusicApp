package com.example.musicapp.Repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.musicapp.Model.Music_Model;
import com.example.musicapp.Retrofit.API;
import com.example.musicapp.Retrofit.RetrofitSave;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Music_Repository {
    private API api;

    public Music_Repository (){
        this.api = RetrofitSave.getRetrofit().create(API.class);
    }

    public MutableLiveData<Music_Model> getPopular_modelMutableLiveData(){
        MutableLiveData<Music_Model> data = new MutableLiveData<>();
        api.getpopular().enqueue(new Callback<Music_Model>() {
            @Override
            public void onResponse(Call<Music_Model> call, Response<Music_Model> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Music_Model> call, Throwable t) {
                Log.e("getpopular",t.getMessage());
            }
        });
        return data;
    }

    public MutableLiveData<Music_Model> getmusic_qc_modelMutableLiveData(int id){
        MutableLiveData<Music_Model> data = new MutableLiveData<>();
        api.getmusic_qc(id).enqueue(new Callback<Music_Model>() {
            @Override
            public void onResponse(Call<Music_Model> call, Response<Music_Model> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Music_Model> call, Throwable t) {
                Log.e("getmusic_qc",t.getMessage());
            }
        });
        return data;
    }

    public MutableLiveData<Music_Model> getmusic_playlist_modelMutableLiveData(int idplaylist){
        MutableLiveData<Music_Model> data = new MutableLiveData<>();
        api.getmusic_playlist(idplaylist).enqueue(new Callback<Music_Model>() {
            @Override
            public void onResponse(Call<Music_Model> call, Response<Music_Model> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Music_Model> call, Throwable t) {
                Log.e("getmusic_playlist",t.getMessage());
            }
        });
        return data;
    }

    public MutableLiveData<Music_Model> getmusic_cate_top_modelMutableLiveData(int idtheloai){
        MutableLiveData<Music_Model> data = new MutableLiveData<>();
        api.getmusic_cate_top(idtheloai).enqueue(new Callback<Music_Model>() {
            @Override
            public void onResponse(Call<Music_Model> call, Response<Music_Model> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Music_Model> call, Throwable t) {
                Log.e("getmusic_cate_top",t.getMessage());
            }
        });
        return data;
    }

    public MutableLiveData<Music_Model> getmusic_album_modelMutableLiveData(int idalbum){
        MutableLiveData<Music_Model> data = new MutableLiveData<>();
        api.getmusic_album(idalbum).enqueue(new Callback<Music_Model>() {
            @Override
            public void onResponse(Call<Music_Model> call, Response<Music_Model> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Music_Model> call, Throwable t) {
                Log.e("getmusic_album",t.getMessage());
            }
        });
        return data;
    }

    // update cac bài hát đc yêu thích
    public MutableLiveData<Music_Model> update_luotthich_modelMutableLiveData(int idbaihat){
        MutableLiveData<Music_Model> data = new MutableLiveData<>();
        api.update_luotthich(idbaihat).enqueue(new Callback<Music_Model>() {
            @Override
            public void onResponse(Call<Music_Model> call, Response<Music_Model> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Music_Model> call, Throwable t) {
                Log.e("update_luotthich",t.getMessage());
            }
        });
        return data;
    }

      // tìm kiếm bài hát theo tên
    public MutableLiveData<Music_Model> search_modelMutableLiveData(String idbaihat){
        MutableLiveData<Music_Model> data = new MutableLiveData<>();
        api.search(idbaihat).enqueue(new Callback<Music_Model>() {
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
