package com.example.vonga.model;

import java.io.Serializable;

/**
 * Created by VoNga on 1/7/2017.
 */

public class Phong implements Serializable {
    private int Ma_Phong;
    private String Ten_Phong;
    private int Trang_Thai;
    //private String User_Name;

    public Phong() {
    }

    public Phong(int ma_Phong, String ten_Phong, int trang_Thai) {
        Ma_Phong = ma_Phong;
        Ten_Phong = ten_Phong;
        Trang_Thai = trang_Thai;
    }

    public int getMa_Phong() {
        return Ma_Phong;
    }

    public void setMa_Phong(int ma_Phong) {
        Ma_Phong = ma_Phong;
    }

    public String getTen_Phong() {
        return Ten_Phong;
    }

    public void setTen_Phong(String ten_Phong) {
        Ten_Phong = ten_Phong;
    }

    public int getTrang_Thai() {
        return Trang_Thai;
    }

    public void setTrang_Thai(int trang_Thai) {
        Trang_Thai = trang_Thai;
    }
}
