package com.example.musicapp.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Advertisement_Model implements Serializable {
    private boolean success;
    private String message;
    private ArrayList<Advertisement> result;

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

    public ArrayList<Advertisement> getResult() {
        return result;
    }

    public void setResult(ArrayList<Advertisement> result) {
        this.result = result;
    }
}
