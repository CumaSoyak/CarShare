package com.araba.cuma.araba.Class;

public class Teklifler {
    private String statu, ad,soyad,nereden,nereye;
    private int fiyat,puan;

    public Teklifler(String statu, String ad, String soyad, String nereden, String nereye, int fiyat, int puan) {
        this.statu = statu;
        this.ad = ad;
        this.soyad = soyad;
        this.nereden = nereden;
        this.nereye = nereye;
        this.fiyat = fiyat;
        this.puan = puan;
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

    public int getFiyat() {
        return fiyat;
    }

    public void setFiyat(int fiyat) {
        this.fiyat = fiyat;
    }

    public int getPuan() {
        return puan;
    }

    public void setPuan(int puan) {
        this.puan = puan;
    }
}
