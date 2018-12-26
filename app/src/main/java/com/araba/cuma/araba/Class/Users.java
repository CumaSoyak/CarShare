package com.araba.cuma.araba.Class;

public class Users {
    private int user_id;
    private String ad;
    private String soyad;
    private String email;
    private String telefon;
    private String parola;

    public Users(int user_id, String ad, String soyad, String email, String telefon, String parola) {
        this.user_id = user_id;
        this.ad = ad;
        this.soyad = soyad;
        this.email = email;
        this.telefon = telefon;
        this.parola = parola;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getAd() {
        return ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getParola() {
        return parola;
    }
}
