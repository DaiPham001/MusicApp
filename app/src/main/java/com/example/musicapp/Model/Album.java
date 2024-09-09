package com.example.musicapp.Model;

import java.io.Serializable;

public class Album implements Serializable {
    private int idalbum;
    private String tenalbum;
    private String tencasialbum;
    private String hinhalbum;

    public int getIdalbum() {
        return idalbum;
    }

    public void setIdalbum(int idalbum) {
        this.idalbum = idalbum;
    }

    public String getTenalbum() {
        return tenalbum;
    }

    public void setTenalbum(String tenalbum) {
        this.tenalbum = tenalbum;
    }

    public String getTencasialbum() {
        return tencasialbum;
    }

    public void setTencasialbum(String tencasialbum) {
        this.tencasialbum = tencasialbum;
    }

    public String getHinhalbum() {
        return hinhalbum;
    }

    public void setHinhalbum(String hinhalbum) {
        this.hinhalbum = hinhalbum;
    }
}
