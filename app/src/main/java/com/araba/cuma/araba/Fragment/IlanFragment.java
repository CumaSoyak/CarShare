package com.araba.cuma.araba.Fragment;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.araba.cuma.araba.R;


public class IlanFragment extends Fragment implements View.OnClickListener {
    private LinearLayout layout_yolcu, layout_sofor;
    private ImageView sofor_resim, yolcu_resim, kisi_image, esya_image;
    private TextView yolcu_tex, sofor_text;


    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    SoforFragment soforFragment = new SoforFragment();
    YolcuFragment yolcuFragment = new YolcuFragment();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_ilan, container, false);


        layout_yolcu = view.findViewById(R.id.yolcu_layout);
        layout_sofor = view.findViewById(R.id.sofor_layout);
        yolcu_resim = view.findViewById(R.id.yolcu_resim);
        sofor_resim = view.findViewById(R.id.sofor_resim);
        yolcu_tex = view.findViewById(R.id.yolcu_text);
        sofor_text = view.findViewById(R.id.sofor_text);

        layout_yolcu.setOnClickListener(this);
        layout_sofor.setOnClickListener(this);
        openFragment(yolcuFragment);
        layout_yolcu.performClick();

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.yolcu_layout:
                layout_sofor.setBackgroundResource(R.drawable.anasayfa_secenek_right_tikla);
                sofor_resim.setImageResource(R.drawable.sofor_siyah);
                yolcu_resim.setImageResource(R.drawable.yolcu);
                layout_yolcu.setBackgroundResource(R.drawable.anasayfa_secenek_left);
                yolcu_tex.setTextColor(Color.parseColor("#ffffff"));
                sofor_text.setTextColor(Color.parseColor("#6f6f6f"));
                openFragment(yolcuFragment);
                break;
            case R.id.sofor_layout:
                layout_yolcu.setBackgroundResource(R.drawable.anasayfa_secenek_left_tikla);
                yolcu_resim.setImageResource(R.drawable.yolcu_siyah);
                sofor_resim.setImageResource(R.drawable.sofor);
                layout_sofor.setBackgroundResource(R.drawable.anasayfa_secenek_right);
                yolcu_tex.setTextColor(Color.parseColor("#6f6f6f"));
                sofor_text.setTextColor(Color.parseColor("#ffffff"));
                openFragment(soforFragment);

                break;

        }

    }

    private void openFragment(final Fragment fragment) {
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_ilan, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}
