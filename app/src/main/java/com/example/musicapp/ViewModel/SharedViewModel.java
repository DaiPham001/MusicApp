package com.example.musicapp.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicapp.Model.Music;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Music> selectedMusic = new MutableLiveData<>();

    public void selectMusic(Music music) {
        selectedMusic.setValue(music);
    }

    public LiveData<Music> getSelectedMusic() {
        return selectedMusic;
    }
}

