package com.araba.cuma.araba.Class;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Bid {
    private String statu, ad,soyad,nereden,nereye, teklif;
    private int kullanicipuan;

    public Bid(String statu, String ad, String soyad, String nereden, String nereye, String teklif, int kullanicipuan) {
        this.statu = statu;
        this.ad = ad;
        this.soyad = soyad;
        this.nereden = nereden;
        this.nereye = nereye;
        this.teklif = teklif;
        this.kullanicipuan = kullanicipuan;
    }
    public Bid(){

    }
    public String getStatu() {
        return statu;
    }

    public void setStatu(String statu) {
        this.statu = statu;
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

    public String getTeklif() {
        return teklif;
    }

    public void setTeklif(String teklif) {
        this.teklif = teklif;
    }

    public int getKullanicipuan() {
        return kullanicipuan;
    }

    public void setKullanicipuan(int kullanicipuan) {
        this.kullanicipuan = kullanicipuan;
    }
}
