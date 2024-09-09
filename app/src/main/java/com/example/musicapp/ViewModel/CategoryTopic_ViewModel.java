package com.example.musicapp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicapp.Model.Category_Model;
import com.example.musicapp.Model.CategoryendTopic_Model;
import com.example.musicapp.Model.Topic_Model;
import com.example.musicapp.Repository.CategoryTopic_Repository;

public class CategoryTopic_ViewModel extends ViewModel {
    private CategoryTopic_Repository categoryTopic_repository;

    public CategoryTopic_ViewModel(){
        categoryTopic_repository = new CategoryTopic_Repository();
    }

    public MutableLiveData<CategoryendTopic_Model> getCategoryTopic(){
        return categoryTopic_repository.getCategoryendTopic_modelMutableLiveData();
    }

    public MutableLiveData<Topic_Model> getalltopic(){
        return categoryTopic_repository.getalltopic_modelMutableLiveData();
    }

    public MutableLiveData<Category_Model> getcategory(int idchude){
        return categoryTopic_repository.getcategory_modelMutableLiveData(idchude);
    }
}
