package com.example.musicapp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicapp.Model.Music_Model;
import com.example.musicapp.Repository.Search_Repository;

public class Search_ViewModel extends ViewModel {
    private Search_Repository search_repository;

    public Search_ViewModel() {
        this.search_repository = new Search_Repository();
    }

    public MutableLiveData<Music_Model> search(String search){
        return search_repository.searchMusic_modelMutableLiveData(search);
    }
}
