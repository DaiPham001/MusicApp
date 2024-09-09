package com.example.musicapp.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable {
    private int idtheloai;
    private int idchude;
    private String tentheloai;
    private String hinhtheloai;

    public int getIdtheloai() {
        return idtheloai;
    }

    public void setIdtheloai(int idtheloai) {
        this.idtheloai = idtheloai;
    }

    public int getIdchude() {
        return idchude;
    }

    public void setIdchude(int idchude) {
        this.idchude = idchude;
    }

    public String getTentheloai() {
        return tentheloai;
    }

    public void setTentheloai(String tentheloai) {
        this.tentheloai = tentheloai;
    }

    public String getHinhtheloai() {
        return hinhtheloai;
    }

    public void setHinhtheloai(String hinhtheloai) {
        this.hinhtheloai = hinhtheloai;
    }
}
