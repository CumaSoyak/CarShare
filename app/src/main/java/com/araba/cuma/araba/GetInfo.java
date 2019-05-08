package com.araba.cuma.araba;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static com.araba.cuma.araba.Constant.CURRENT_NAME;
import static com.araba.cuma.araba.Constant.CURRENT_PHOTO_URL;

public class GetInfo {
    public String getInfoPhoto(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String photoUrl = sharedPreferences.getString(CURRENT_PHOTO_URL, "");
        return photoUrl;
    }


    public String getInfoName(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String name = sharedPreferences.getString(CURRENT_NAME, "Loading..");
        return name;
    }
}
