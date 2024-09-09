package com.example.musicapp.Model;

import java.io.Serializable;

public class Music implements Serializable {
    private int idbaihat;
    private int idalbum;
    private int idtheloai;
    private int idplaylist;
    private String tenbaihat;
    private String hinhbaihat;
    private String casi;
    private String linkbaihat;
    private int luotthich;



    public Music(int idplaylist) {
        this.idplaylist = idplaylist;
    }


    public Music(int idbaihat, String tenbaihat, String hinhbaihat, String casi) {
        this.idbaihat = idbaihat;
        this.tenbaihat = tenbaihat;
        this.hinhbaihat = hinhbaihat;
        this.casi = casi;
    }

    public int getIdbaihat() {
        return idbaihat;
    }

    public void setIdbaihat(int idbaihat) {
        this.idbaihat = idbaihat;
    }

    public int getIdalbum() {
        return idalbum;
    }

    public void setIdalbum(int idalbum) {
        this.idalbum = idalbum;
    }

    public int getIdtheloai() {
        return idtheloai;
    }

    public void setIdtheloai(int idtheloai) {
        this.idtheloai = idtheloai;
    }

    public int getIdplaylist() {
        return idplaylist;
    }

    public void setIdplaylist(int idplaylist) {
        this.idplaylist = idplaylist;
    }

    public String getTenbaihat() {
        return tenbaihat;
    }

    public void setTenbaihat(String tenbaihat) {
        this.tenbaihat = tenbaihat;
    }

    public String getHinhbaihat() {
        return hinhbaihat;
    }

    public void setHinhbaihat(String hinhbaihat) {
        this.hinhbaihat = hinhbaihat;
    }

    public String getCasi() {
        return casi;
    }

    public void setCasi(String casi) {
        this.casi = casi;
    }

    public String getLinkbaihat() {
        return linkbaihat;
    }

    public void setLinkbaihat(String linkbaihat) {
        this.linkbaihat = linkbaihat;
    }

    public int getLuotthich() {
        return luotthich;
    }

    public void setLuotthich(int luotthich) {
        this.luotthich = luotthich;
    }
}
