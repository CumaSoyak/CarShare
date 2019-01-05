
package com.araba.cuma.araba.Class;

import com.araba.cuma.araba.R;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@IgnoreExtraProperties
public class Ilanlar {


    private String id;
    private String ad;
    private String soyad;
    private String nereden;
    private String nereye;
    private String statu;
    private int kullanicipuan;
    private String fiyat;
    private String userid;

    public Ilanlar(String ad, String soyad, String nereden, String nereye, String statu, int kullanicipuan, String fiyat ,String id,String userid) {
        this.ad = ad;
        this.soyad = soyad;
        this.nereden = nereden;
        this.nereye = nereye;
        this.statu = statu;
        this.kullanicipuan = kullanicipuan;
        this.fiyat = fiyat;
        this.id=id;
        this.userid=userid;

    }
    public  Ilanlar()
    {

    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public String getNereden() {
        return nereden;
    }

    public void setNereden(String nereden) {
        this.nereden = nereden;
    }

    public String getNereye() {
        return nereye;
    }

    public void setNereye(String nereye) {
        this.nereye = nereye;
    }

    public String getStatu() {
        return statu;
    }

    public void setStatu(String statu) {
        this.statu = statu;
    }

    public int getKullanicipuan() {
        return kullanicipuan;
    }

    public void setKullanicipuan(int kullanicipuan) {
        this.kullanicipuan = kullanicipuan;
    }

    public String getFiyat() {
        return fiyat;
    }

    public void setFiyat(String fiyat) {
        this.fiyat = fiyat;
    }
}
