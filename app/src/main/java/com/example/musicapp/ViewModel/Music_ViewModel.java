package com.example.musicapp.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicapp.Model.Music_Model;
import com.example.musicapp.Repository.Music_Repository;

public class Music_ViewModel extends ViewModel {

    private Music_Repository music_repository;
    public Music_ViewModel(){
        this.music_repository = new Music_Repository();
    }

    public MutableLiveData<Music_Model> getpopular(){
        return music_repository.getPopular_modelMutableLiveData();
    }

    public MutableLiveData<Music_Model> getmusic_qc(int idqc){
        return music_repository.getmusic_qc_modelMutableLiveData(idqc);
    }

    public MutableLiveData<Music_Model> getmusic_playlist(int idplaylist){
        return music_repository.getmusic_playlist_modelMutableLiveData(idplaylist);
    }

    public MutableLiveData<Music_Model> getmusic_cate_top(int idtheloai){
        return music_repository.getmusic_cate_top_modelMutableLiveData(idtheloai);
    }

    public MutableLiveData<Music_Model> getmusic_album(int idalbum){
        return music_repository.getmusic_album_modelMutableLiveData(idalbum);
    }

    public MutableLiveData<Music_Model> update_luotthich(int idbaihat){
        return music_repository.update_luotthich_modelMutableLiveData(idbaihat);
    }

    public MutableLiveData<Music_Model> search(String idbaihat){
        return music_repository.search_modelMutableLiveData(idbaihat);
    }
}
