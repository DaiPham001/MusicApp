package com.example.musicapp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicapp.Model.Album_Model;
import com.example.musicapp.Model.Music_Model;
import com.example.musicapp.Repository.Album_Repository;

public class Album_ViewModel extends ViewModel {
    private Album_Repository album_repository;
    public Album_ViewModel(){
        this.album_repository = new Album_Repository();
    }

    public MutableLiveData<Album_Model> getalbum(){
        return album_repository.getalbum_modelMutableLiveData();
    }

    public MutableLiveData<Album_Model> getallalbum(){
        return album_repository.getallalbum_modelMutableLiveData();
    }

}
