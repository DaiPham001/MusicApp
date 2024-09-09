package com.example.musicapp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicapp.Model.Playlist_Model;
import com.example.musicapp.Repository.Playlist_Repository;

public class Playlist_ViewModel extends ViewModel {

    private Playlist_Repository playlist_repository;

    public Playlist_ViewModel(){
        this.playlist_repository = new Playlist_Repository();
    }

    public MutableLiveData<Playlist_Model> getplaylist(){
        return playlist_repository.getplaylist_modelMutableLiveData();
    }
    public MutableLiveData<Playlist_Model> getallplaylist(){
        return playlist_repository.getallplaylist_modelMutableLiveData();
    }
}
