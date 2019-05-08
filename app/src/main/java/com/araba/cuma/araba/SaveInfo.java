package com.araba.cuma.araba;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import static com.araba.cuma.araba.Constant.CURRENT_NAME;
import static com.araba.cuma.araba.Constant.CURRENT_PASSWORD;
import static com.araba.cuma.araba.Constant.CURRENT_PHOTO_URL;

public class SaveInfo {
    public void saveInfo(Context context, String uri,String name,String password){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CURRENT_PHOTO_URL, uri);
        editor.putString(CURRENT_NAME,name);
        editor.putString(CURRENT_PASSWORD,password);
        editor.commit();
    }
}
