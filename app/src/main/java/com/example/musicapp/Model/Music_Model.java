package com.example.musicapp.Model;

import java.util.ArrayList;

public class Music_Model {
    private boolean success;
    private String message;
    private ArrayList<Music> result;

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

    public ArrayList<Music> getResult() {
        return result;
    }

    public void setResult(ArrayList<Music> result) {
        this.result = result;
    }
}
