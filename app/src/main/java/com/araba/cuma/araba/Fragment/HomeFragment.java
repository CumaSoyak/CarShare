package com.araba.cuma.araba.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.araba.cuma.araba.R;

public class HomeFragment extends Fragment {

    LinearLayout linearLayout_yolcu, linearLayout_sofor;
    ImageView yolcu_resim, sofor_resim;
    RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        linearLayout_sofor = view.findViewById(R.id.sofor_cardView);
        linearLayout_yolcu = view.findViewById(R.id.yolcu_cardView);
        yolcu_resim = view.findViewById(R.id.yolcu_imageView);
        sofor_resim = view.findViewById(R.id.sofor_imageView);
        linearLayout_yolcu.performClick();

        linearLayout_yolcu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                linearLayout_yolcu.setBackgroundResource(R.drawable.anasayfa_secenek_left_tikla);
                yolcu_resim.setImageResource(R.drawable.yolcu_siyah);
                sofor_resim.setImageResource(R.drawable.sofor);
                linearLayout_sofor.setBackgroundResource(R.drawable.anasayfa_secenek_right);

            }
        });
        linearLayout_sofor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                linearLayout_sofor.setBackgroundResource(R.drawable.anasayfa_secenek_right_tikla);
                sofor_resim.setImageResource(R.drawable.sofor_siyah);
                yolcu_resim.setImageResource(R.drawable.yolcu);
                linearLayout_yolcu.setBackgroundResource(R.drawable.anasayfa_secenek_left);
            }
        });


        return view;
    }

}
