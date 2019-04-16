package com.araba.cuma.araba.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.araba.cuma.araba.R;


public class AdvertFragment extends Fragment implements View.OnClickListener {
    private LinearLayout layout_yolcu, layout_sofor;
    private ImageView sofor_resim, yolcu_resim, kisi_image, esya_image;
    private TextView yolcu_tex, sofor_text;


    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    DriverFragment driverFragment = new DriverFragment();
    TravelerFragment travelerFragment = new TravelerFragment();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_advert, container, false);


        layout_yolcu = view.findViewById(R.id.yolcu_layout);
        layout_sofor = view.findViewById(R.id.sofor_layout);
        yolcu_resim = view.findViewById(R.id.yolcu_resim);
        sofor_resim = view.findViewById(R.id.sofor_resim);
        yolcu_tex = view.findViewById(R.id.yolcu_text);
        sofor_text = view.findViewById(R.id.sofor_text);

        layout_yolcu.setOnClickListener(this);
        layout_sofor.setOnClickListener(this);
        openFragment(travelerFragment);
        layout_yolcu.performClick();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getContext(), "initilaze advertFragment", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.yolcu_layout:
                layout_sofor.setBackgroundResource(R.drawable.anasayfa_secenek_right_tikla);
                sofor_resim.setImageResource(R.drawable.ic_car_blue);
                yolcu_resim.setImageResource(R.drawable.ic_traveler_white);
                layout_yolcu.setBackgroundResource(R.drawable.anasayfa_secenek_left);
                yolcu_tex.setTextColor(ContextCompat.getColor(getActivity(), R.color.beyaz));
                sofor_text.setTextColor(ContextCompat.getColor(getActivity(), R.color.mavi));
                openFragment(travelerFragment);
                break;
            case R.id.sofor_layout:
                layout_yolcu.setBackgroundResource(R.drawable.anasayfa_secenek_left_tikla);
                yolcu_resim.setImageResource(R.drawable.ic_traveler_blue);
                sofor_resim.setImageResource(R.drawable.ic_car_white);
                layout_sofor.setBackgroundResource(R.drawable.anasayfa_secenek_right);
                yolcu_tex.setTextColor(ContextCompat.getColor(getActivity(), R.color.mavi));
                sofor_text.setTextColor(ContextCompat.getColor(getActivity(), R.color.beyaz));
                openFragment(driverFragment);

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
