package com.example.vonga.model;

import java.io.Serializable;

/**
 * Created by VoNga on 1/7/2017.
 */

public class Khach implements Serializable {
    private int Ma_Khach;
    private String Ten_Khach;
    private String CMND;
    private int Ma_Phong;

    public int getMa_Phong() {
        return Ma_Phong;
    }

    public void setMa_Phong(int ma_Phong) {
        Ma_Phong = ma_Phong;
    }

    public Khach() {
    }

    public Khach(int ma_Khach, String ten_Khach, String CMND,int ma_Phong) {
        Ma_Khach = ma_Khach;
        Ten_Khach = ten_Khach;
        this.CMND = CMND;
        Ma_Phong = ma_Phong;
    }

    public int getMa_Khach() {
        return Ma_Khach;
    }

    public void setMa_Khach(int ma_Khach) {
        Ma_Khach = ma_Khach;
    }

    public String getTen_Khach() {
        return Ten_Khach;
    }

    public void setTen_Khach(String ten_Khach) {
        Ten_Khach = ten_Khach;
    }

    public String getCMND() {
        return CMND;
    }

    public void setCMND(String CMND) {
        this.CMND = CMND;
    }
}
