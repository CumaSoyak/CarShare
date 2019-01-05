package com.araba.cuma.araba.Fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.araba.cuma.araba.Adapter.IlanAdapter;
import com.araba.cuma.araba.Class.Ilanlar;
import com.araba.cuma.araba.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private LinearLayout layout_yolcu, layout_sofor;
    private ImageView sofor_resim, yolcu_resim;
    private TextView yolcu_tex, sofor_text, info;
    private EditText nereden, nereye;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String user_id;
    private String uuid_String;

    private RecyclerView recyclerView;
    private IlanAdapter ılanAdapter;
    private ArrayList<Ilanlar> ilanlarList;
    private Ilanlar mılanlar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();//todo
        user = firebaseAuth.getCurrentUser();
        user_id = user.getUid();

        nereden = view.findViewById(R.id.search_nereden);
        nereye = view.findViewById(R.id.search_nereye);
        info = view.findViewById(R.id.info);
        recyclerView = view.findViewById(R.id.search_recylerview);
        ilanlarList = new ArrayList<>();
        ılanAdapter = new IlanAdapter(ilanlarList, getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        layout_yolcu = view.findViewById(R.id.yolcu_layout);
        layout_sofor = view.findViewById(R.id.sofor_layout);
        yolcu_resim = view.findViewById(R.id.yolcu_resim);
        sofor_resim = view.findViewById(R.id.sofor_resim);
        yolcu_tex = view.findViewById(R.id.yolcu_text);
        sofor_text = view.findViewById(R.id.sofor_text);
        info.setVisibility(View.GONE);
        layout_yolcu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kontrol() == true) {
                    Firebase_get_yolcu();
                    layout_sofor.setBackgroundResource(R.drawable.anasayfa_secenek_right_tikla);
                    sofor_resim.setImageResource(R.drawable.sofor_siyah);
                    yolcu_resim.setImageResource(R.drawable.yolcu);
                    layout_yolcu.setBackgroundResource(R.drawable.anasayfa_secenek_left);
                    yolcu_tex.setTextColor(Color.parseColor("#ffffff"));
                    sofor_text.setTextColor(Color.parseColor("#6f6f6f"));

                } else {
                    Toast.makeText(getActivity(), "Alanları boş geçmeyiniz", Toast.LENGTH_LONG).show();
                }

            }
        });
        layout_sofor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kontrol() == true) {
                    Firebase_get_sofor();
                    layout_yolcu.setBackgroundResource(R.drawable.anasayfa_secenek_left_tikla);
                    yolcu_resim.setImageResource(R.drawable.yolcu_siyah);
                    sofor_resim.setImageResource(R.drawable.sofor);
                    layout_sofor.setBackgroundResource(R.drawable.anasayfa_secenek_right);
                    yolcu_tex.setTextColor(Color.parseColor("#6f6f6f"));
                    sofor_text.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    Toast.makeText(getActivity(), "Alanları boş geçmeyiniz", Toast.LENGTH_LONG).show();
                }


            }
        });

        return view;
    }

    public boolean kontrol() {
        if (nereden.getText().toString().matches("")) {
            return false;
        } else if (nereye.getText().toString().matches("")) {
            return false;
        } else return true;
    }

    public void Firebase_get_yolcu() {
        ilanlarList.clear();
        databaseReference.child("Yolcu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    mılanlar = ds.getValue(Ilanlar.class);
                    if ((mılanlar.getNereden().equals(nereden.getText().toString())) &&
                            (mılanlar.getNereye().equals(nereye.getText().toString())) &&
                            mılanlar.getStatu().equals("Yolcu")) {
                        info.setVisibility(View.GONE);
                        ilanlarList.add(mılanlar);
                        recyclerView.setAdapter(ılanAdapter);

                    } else {
                        info.setVisibility(View.VISIBLE);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void Firebase_get_sofor() {
        ilanlarList.clear();
        databaseReference.child("Yolcu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    mılanlar = ds.getValue(Ilanlar.class);
                    if ((mılanlar.getNereden().equals(nereden.getText().toString())) &&
                            (mılanlar.getNereye().equals(nereye.getText().toString())) &&
                            mılanlar.getStatu().equals("Şoför")) {
                        info.setVisibility(View.GONE);
                        ilanlarList.add(mılanlar);
                        recyclerView.setAdapter(ılanAdapter);

                    } else {
                        info.setVisibility(View.VISIBLE);

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
