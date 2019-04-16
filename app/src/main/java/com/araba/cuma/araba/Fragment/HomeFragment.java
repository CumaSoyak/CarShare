package com.araba.cuma.araba.Fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.araba.cuma.araba.Activity.MesajActivity;
import com.araba.cuma.araba.Adapter.AdvertAdapter;
import com.araba.cuma.araba.Class.Advert;
import com.araba.cuma.araba.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class HomeFragment extends Fragment {

    LinearLayout linearLayout_yolcu, linearLayout_sofor;
    ImageView yolcu_resim, sofor_resim;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String user_id;

    private RecyclerView recyclerView;
    private AdvertAdapter advertAdapter;
    private ArrayList<Advert> advertList;
    private Advert mılanlar;

    private String gelen_ad;
    private String gelen_soyad;
    private String gelen_telefon;
    private TextView info_home;
    final String PREFS_NAME = "MyPrefsFile";
    SharedPreferences settings;
    View arka;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        user = firebaseAuth.getCurrentUser();
        user_id = user.getUid();

        settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        Toolbar toolbar=view.findViewById(R.id.toolbar);

        Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_up);
        toolbar.setAnimation(animation);

        UUID uuıd = UUID.randomUUID();
        linearLayout_sofor = view.findViewById(R.id.sofor_cardView);
        linearLayout_yolcu = view.findViewById(R.id.yolcu_cardView);
        yolcu_resim = view.findViewById(R.id.yolcu_imageView);
        sofor_resim = view.findViewById(R.id.sofor_imageView);
        info_home = view.findViewById(R.id.info_home);

        recyclerView = view.findViewById(R.id.home_recylerview);
        advertList = new ArrayList<>();
        advertAdapter = new AdvertAdapter(advertList, getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        linearLayout_yolcu.performClick();
        info_home.setVisibility(View.GONE);

        linearLayout_yolcu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Firebase_get_yolcu(1);
                linearLayout_sofor.setBackgroundResource(R.drawable.anasayfa_secenek_right_tikla);
                sofor_resim.setImageResource(R.drawable.ic_car_blue);
                yolcu_resim.setImageResource(R.drawable.ic_traveler_white);
                linearLayout_yolcu.setBackgroundResource(R.drawable.anasayfa_secenek_left);

            }
        });
        linearLayout_sofor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Firebase_get_yolcu(2);
                linearLayout_yolcu.setBackgroundResource(R.drawable.anasayfa_secenek_left_tikla);
                yolcu_resim.setImageResource(R.drawable.ic_traveler_blue);
                sofor_resim.setImageResource(R.drawable.ic_car_white);
                linearLayout_sofor.setBackgroundResource(R.drawable.anasayfa_secenek_right);

            }
        });


         return view;
    }


    public void Firebase_get_yolcu(final int deger) {
        advertList.clear();
        databaseReference.child("Yolcu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                switch (deger) {
                    case 1:
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            mılanlar = ds.getValue(Advert.class);

                            if ((!mılanlar.getUserid().equals(user_id)) && (mılanlar.getStatu().equals("Yolcu"))) {
                                advertList.add(mılanlar);
                                recyclerView.setAdapter(advertAdapter);
                                bilgi(1);
                            } else {
                                bilgi(2);
                            }

                        }
                        break;
                    case 2:
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            mılanlar = ds.getValue(Advert.class);

                            if ((!mılanlar.getUserid().equals(user_id)) && (mılanlar.getStatu().equals("Şoför"))) {
                                advertList.add(mılanlar);
                                recyclerView.setAdapter(advertAdapter);
                                bilgi(1);
                            } else {
                                bilgi(2);
                            }


                        }
                        break;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void bilgi(int i) {
        switch (i) {
            case 1:
                info_home.setVisibility(View.GONE);
                break;
            case 2:
                info_home.setVisibility(View.VISIBLE);
                break;
        }

    }

}
