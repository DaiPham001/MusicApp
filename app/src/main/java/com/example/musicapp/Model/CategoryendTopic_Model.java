package com.example.musicapp.Model;

import java.util.ArrayList;

public class CategoryendTopic_Model {
    private boolean success;
    private String message;
    private ArrayList<Category> theloai;
    private ArrayList<Topic> chude;

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

    public ArrayList<Category> getTheloai() {
        return theloai;
    }

    public void setTheloai(ArrayList<Category> theloai) {
        this.theloai = theloai;
    }

    public ArrayList<Topic> getChude() {
        return chude;
    }

    public void setChude(ArrayList<Topic> chude) {
        this.chude = chude;
    }
}
