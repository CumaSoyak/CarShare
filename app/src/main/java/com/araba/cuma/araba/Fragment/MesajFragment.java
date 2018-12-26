package com.araba.cuma.araba.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.araba.cuma.araba.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MesajFragment extends Fragment {


    public MesajFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mesaj, container, false);
    }

}
