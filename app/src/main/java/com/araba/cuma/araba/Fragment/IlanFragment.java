package com.araba.cuma.araba.Fragment;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
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
    private Spinner kisi_spinner, esya_spinner;
    private TextView kisi_text, esya_text;
    private RadioGroup radioGroup;
    private RadioButton kisi, kisi_ve_esya, esya;
    ConstraintLayout constraint_yolcu, constraint_sofor;
    private ScrollView ilan_scrolview;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_ilan, container, false);

        constraint_yolcu = view.findViewById(R.id.constraint_yolcu);
        constraint_sofor = view.findViewById(R.id.constarint_sofor);
        constraint_sofor.setVisibility(View.GONE);

        ilan_scrolview = view.findViewById(R.id.ilan_scrolview);

        layout_yolcu = view.findViewById(R.id.yolcu_layout);
        layout_sofor = view.findViewById(R.id.sofor_layout);
        yolcu_resim = view.findViewById(R.id.yolcu_resim);
        sofor_resim = view.findViewById(R.id.sofor_resim);
        yolcu_tex = view.findViewById(R.id.yolcu_text);
        sofor_text = view.findViewById(R.id.sofor_text);

        kisi_text = view.findViewById(R.id.kisi_text);
        kisi_image = view.findViewById(R.id.kisi_image);
        kisi_spinner = view.findViewById(R.id.kisi_spinner);

        esya_text = view.findViewById(R.id.esya_text);
        esya_image = view.findViewById(R.id.esya_image);
        esya_spinner = view.findViewById(R.id.esya_spinner);

        radioGroup = view.findViewById(R.id.radioGroup);
        kisi = radioGroup.findViewById(R.id.radioButton_kisi);
        kisi_ve_esya = radioGroup.findViewById(R.id.radioButton_kisive_ev);
        esya = radioGroup.findViewById(R.id.radioButton_ev);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButton_kisi:
                        kisi_text.setVisibility(View.VISIBLE);
                        kisi_image.setVisibility(View.VISIBLE);
                        kisi_spinner.setVisibility(View.VISIBLE);
                        esya_text.setVisibility(View.GONE);
                        esya_image.setVisibility(View.GONE);
                        esya_spinner.setVisibility(View.GONE);
                        break;
                    case R.id.radioButton_kisive_ev:
                        esya_text.setVisibility(View.VISIBLE);
                        esya_image.setVisibility(View.VISIBLE);
                        esya_spinner.setVisibility(View.VISIBLE);
                        kisi_text.setVisibility(View.VISIBLE);
                        kisi_image.setVisibility(View.VISIBLE);
                        kisi_spinner.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radioButton_ev:
                        kisi_text.setVisibility(View.GONE);
                        kisi_image.setVisibility(View.GONE);
                        kisi_spinner.setVisibility(View.GONE);
                        esya_text.setVisibility(View.VISIBLE);
                        esya_image.setVisibility(View.VISIBLE);
                        esya_spinner.setVisibility(View.VISIBLE);
                        break;


                }
            }
        });


        layout_yolcu.setOnClickListener(this);
        layout_sofor.setOnClickListener(this);


        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.yolcu_layout:
                constraint_yolcu.setVisibility(View.VISIBLE);
                constraint_sofor.setVisibility(View.GONE);
                layout_yolcu.setBackgroundResource(R.drawable.anasayfa_secenek_left_tikla);
                yolcu_resim.setImageResource(R.drawable.yolcu_siyah);
                sofor_resim.setImageResource(R.drawable.sofor);
                layout_sofor.setBackgroundResource(R.drawable.anasayfa_secenek_right);
                yolcu_tex.setTextColor(Color.parseColor("#6f6f6f"));
                sofor_text.setTextColor(Color.parseColor("#ffffff"));
                break;
            case R.id.sofor_layout:
                constraint_sofor.setVisibility(View.VISIBLE);
                constraint_yolcu.setVisibility(View.GONE);
                layout_sofor.setBackgroundResource(R.drawable.anasayfa_secenek_right_tikla);
                sofor_resim.setImageResource(R.drawable.sofor_siyah);
                yolcu_resim.setImageResource(R.drawable.yolcu);
                layout_yolcu.setBackgroundResource(R.drawable.anasayfa_secenek_left);
                yolcu_tex.setTextColor(Color.parseColor("#ffffff"));
                sofor_text.setTextColor(Color.parseColor("#6f6f6f"));


                break;

        }

    }
}
