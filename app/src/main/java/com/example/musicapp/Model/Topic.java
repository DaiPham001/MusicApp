package com.example.musicapp.Model;

import java.io.Serializable;

public class Topic implements Serializable{
    private int idchude;
    private String tenchude;
    private String hinhchude;

    public int getIdchude() {
        return idchude;
    }

    public void setIdchude(int idchude) {
        this.idchude = idchude;
    }

    public String getTenchude() {
        return tenchude;
    }

    public void setTenchude(String tenchude) {
        this.tenchude = tenchude;
    }

    public String getHinhchude() {
        return hinhchude;
    }

    public void setHinhchude(String hinhchude) {
        this.hinhchude = hinhchude;
    }
}
