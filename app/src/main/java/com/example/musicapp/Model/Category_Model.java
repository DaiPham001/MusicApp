package com.example.musicapp.Model;

import java.util.ArrayList;

public class Category_Model {
    private boolean success;
    private String message;
    private ArrayList<Category> result;

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

    public ArrayList<Category> getResult() {
        return result;
    }

    public void setResult(ArrayList<Category> result) {
        this.result = result;
    }
}
