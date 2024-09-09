package com.example.musicapp.Model;

public class Playlist extends Music {
    private String tenpl;
    private String hinhpl;
    private String hinhiconpl;

    public Playlist(int idplaylist, String tenpl, String hinhpl, String hinhiconpl) {
        super(idplaylist);
        this.tenpl = tenpl;
        this.hinhpl = hinhpl;
        this.hinhiconpl = hinhiconpl;
    }

    public String getTenpl() {
        return tenpl;
    }

    public void setTenpl(String tenpl) {
        this.tenpl = tenpl;
    }

    public String getHinhpl() {
        return hinhpl;
    }

    public void setHinhpl(String hinhpl) {
        this.hinhpl = hinhpl;
    }

    public String getHinhiconpl() {
        return hinhiconpl;
    }

    public void setHinhiconpl(String hinhiconpl) {
        this.hinhiconpl = hinhiconpl;
    }
}
