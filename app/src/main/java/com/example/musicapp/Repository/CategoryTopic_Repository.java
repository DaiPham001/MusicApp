package com.example.musicapp.Repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.musicapp.Model.Category_Model;
import com.example.musicapp.Model.CategoryendTopic_Model;
import com.example.musicapp.Model.Topic_Model;
import com.example.musicapp.Retrofit.API;
import com.example.musicapp.Retrofit.RetrofitSave;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryTopic_Repository {
    private API api;

    public CategoryTopic_Repository(){
        api = RetrofitSave.getRetrofit().create(API.class);
    }

    public MutableLiveData<CategoryendTopic_Model> getCategoryendTopic_modelMutableLiveData(){
        MutableLiveData<CategoryendTopic_Model> data = new MutableLiveData<>();
        api.getcategorytopic().enqueue(new Callback<CategoryendTopic_Model>() {
            @Override
            public void onResponse(Call<CategoryendTopic_Model> call, Response<CategoryendTopic_Model> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<CategoryendTopic_Model> call, Throwable t) {
                Log.e("getcatop",t.getMessage());
            }
        });

        return data;
    }

    // get all chu de
    public MutableLiveData<Topic_Model> getalltopic_modelMutableLiveData(){
        MutableLiveData<Topic_Model> data = new MutableLiveData<>();
        api.getalltopic().enqueue(new Callback<Topic_Model>() {
            @Override
            public void onResponse(Call<Topic_Model> call, Response<Topic_Model> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Topic_Model> call, Throwable t) {
                Log.e("getalltopic",t.getMessage());
            }
        });
        return data;
    }

    // lấy danh dánh the loai theo chu de
    public MutableLiveData<Category_Model> getcategory_modelMutableLiveData(int idchude){
        MutableLiveData<Category_Model> data = new MutableLiveData<>();
        api.getcategory(idchude).enqueue(new Callback<Category_Model>() {
            @Override
            public void onResponse(Call<Category_Model> call, Response<Category_Model> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Category_Model> call, Throwable t) {
                Log.e("getalltopic",t.getMessage());
            }
        });
        return data;
    }
}
