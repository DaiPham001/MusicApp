package com.example.musicapp.Model;

import java.util.ArrayList;

public class Album_Model {
    private boolean success;
    private String message;
    private ArrayList<Album> result;

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

    public ArrayList<Album> getResult() {
        return result;
    }

    public void setResult(ArrayList<Album> result) {
        this.result = result;
    }
}
