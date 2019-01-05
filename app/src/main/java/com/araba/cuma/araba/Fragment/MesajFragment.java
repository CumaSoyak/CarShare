package com.araba.cuma.araba.Fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.araba.cuma.araba.R;


public class MesajFragment extends Fragment {
    TextView yazdir;
    public static int id;

    public MesajFragment() {
    }

    @SuppressLint("ValidFragment")
    public MesajFragment(int id) {
        this.id=id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mesaj, container, false);
        yazdir = view.findViewById(R.id.yazdir);

        yazdir.setText(String.valueOf(id));

        return view;
    }

}
