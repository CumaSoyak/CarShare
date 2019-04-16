package com.araba.cuma.araba.Api;

import com.araba.cuma.araba.Class.Advert;
import com.araba.cuma.araba.Class.Users;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {
    String BASE_URL ="http://192.168.50.2/Deneme/api/post/";
    //@Headers("Content-Type: application/json")
    @FormUrlEncoded
    @POST("createuser")
    Call<Users> postUsers(
            @Field("ad") String ad,
            @Field("soyad") String soyad,
            @Field("email") String email,
            @Field("telefon") String telefon,
            @Field("parola") String parola

    );

    @GET("createuser")
    Call<List<Users>> getUsers();


    @GET("read.php")
    Call<List<Advert>> getIlanlar();
}

