package com.example.musicapp.Repository;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.example.musicapp.Model.Advertisement_Model;
import com.example.musicapp.Retrofit.API;
import com.example.musicapp.Retrofit.RetrofitSave;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Advertisement_Repository {
    private API api;

    public Advertisement_Repository() {
        this.api = RetrofitSave.getRetrofit().create(API.class);
    }

    public MutableLiveData<Advertisement_Model> getquangcaoAdvertisement_modelMutableLiveData() {
        MutableLiveData<Advertisement_Model> data = new MutableLiveData<>();
        api.getquangcao().enqueue(new Callback<Advertisement_Model>() {
            @Override
            public void onResponse(Call<Advertisement_Model> call, Response<Advertisement_Model> response) {
                data.setValue(response.body());
            }
            @Override
            public void onFailure(Call<Advertisement_Model> call, Throwable t) {
                Log.e("getquangcao", t.getMessage());
            }
        });
        return data;
    }
}
