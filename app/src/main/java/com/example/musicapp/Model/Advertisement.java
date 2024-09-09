package com.example.musicapp.Model;

import java.io.Serializable;

public class Advertisement extends Music implements Serializable{
    private int idqc;
    private String hinhanhqc;
    private String noidungqc;


    public Advertisement(int idbaihat, String tenbaihat, String hinhbaihat, String casi, int idqc, String hinhanhqc, String noidungqc) {
        super(idbaihat, tenbaihat, hinhbaihat, casi);
        this.idqc = idqc;
        this.hinhanhqc = hinhanhqc;
        this.noidungqc = noidungqc;
    }

    public int getIdqc() {
        return idqc;
    }

    public void setIdqc(int idqc) {
        this.idqc = idqc;
    }

    public String getHinhanhqc() {
        return hinhanhqc;
    }

    public void setHinhanhqc(String hinhanhqc) {
        this.hinhanhqc = hinhanhqc;
    }

    public String getNoidungqc() {
        return noidungqc;
    }

    public void setNoidungqc(String noidungqc) {
        this.noidungqc = noidungqc;
    }


}
