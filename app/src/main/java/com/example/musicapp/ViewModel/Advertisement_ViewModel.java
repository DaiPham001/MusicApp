package com.example.musicapp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicapp.Model.Advertisement_Model;
import com.example.musicapp.Repository.Advertisement_Repository;

public class Advertisement_ViewModel extends ViewModel {
    private Advertisement_Repository advertisement_repository;

    public Advertisement_ViewModel() {
        this.advertisement_repository = new Advertisement_Repository();
    }

    public MutableLiveData<Advertisement_Model> getquangcao(){
       return advertisement_repository.getquangcaoAdvertisement_modelMutableLiveData();
    }
}
