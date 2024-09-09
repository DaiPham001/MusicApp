package com.example.musicapp.Model;

import java.util.ArrayList;

public class Playlist_Model {
    private boolean success;
    private String message;
    private ArrayList<Playlist> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Playlist> getResult() {
        return result;
    }

    public void setResult(ArrayList<Playlist> result) {
        this.result = result;
    }
}
