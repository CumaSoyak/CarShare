package com.araba.cuma.araba.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.araba.cuma.araba.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    public static String gelen_ad;
    public static String gelen_soyad;
    public static String gelen_telefon;

    public TextView textView_ad, textView_soyad, textView_telefon;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        textView_ad = view.findViewById(R.id.ad);
        textView_soyad = view.findViewById(R.id.soyad);
        textView_telefon = view.findViewById(R.id.telefon);

        textView_ad.setText(gelen_ad);
        textView_soyad.setText(gelen_soyad);
        textView_telefon.setText(gelen_telefon);

        return view;
    }

    public void kisi_bilgi_al(String ad, String soyad, String telefon) {
        gelen_ad = ad;
        gelen_soyad = soyad;
        gelen_telefon = telefon;

    }
}
