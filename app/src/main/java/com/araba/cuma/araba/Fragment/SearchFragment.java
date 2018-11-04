package com.araba.cuma.araba.Fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.araba.cuma.araba.R;

public class SearchFragment extends Fragment {
  private   LinearLayout layout_yolcu, layout_sofor;
  private   ImageView sofor_resim,yolcu_resim;
  private   TextView yolcu_tex,sofor_text;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        layout_yolcu=view.findViewById(R.id.yolcu_layout);
        layout_sofor=view.findViewById(R.id.sofor_layout);
        yolcu_resim=view.findViewById(R.id.yolcu_resim);
        sofor_resim=view.findViewById(R.id.sofor_resim);
        yolcu_tex=view.findViewById(R.id.yolcu_text);
        sofor_text=view.findViewById(R.id.sofor_text);

        layout_yolcu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                layout_sofor.setBackgroundResource(R.drawable.anasayfa_secenek_right_tikla);
                sofor_resim.setImageResource(R.drawable.sofor_siyah);
                yolcu_resim.setImageResource(R.drawable.yolcu);
                layout_yolcu.setBackgroundResource(R.drawable.anasayfa_secenek_left);
                yolcu_tex.setTextColor(Color.parseColor("#ffffff"));
                sofor_text.setTextColor(Color.parseColor("#6f6f6f"));
            }
        });
        layout_sofor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_yolcu.setBackgroundResource(R.drawable.anasayfa_secenek_left_tikla);
                yolcu_resim.setImageResource(R.drawable.yolcu_siyah);
                sofor_resim.setImageResource(R.drawable.sofor);
                layout_sofor.setBackgroundResource(R.drawable.anasayfa_secenek_right);
                yolcu_tex.setTextColor(Color.parseColor("#6f6f6f"));
                sofor_text.setTextColor(Color.parseColor("#ffffff"));

            }
        });


        return view;
    }

}
