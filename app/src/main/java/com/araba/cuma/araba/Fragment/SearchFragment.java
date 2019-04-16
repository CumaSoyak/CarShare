package com.araba.cuma.araba.Fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import static com.araba.cuma.araba.Fragment.LocationFragment.CITY;
import static com.araba.cuma.araba.Fragment.LocationFragment.SEARCH_FROM_CITY;
import static com.araba.cuma.araba.Fragment.LocationFragment.SEARCH_TO_CITY;

public class SearchFragment extends Fragment implements View.OnClickListener {
    private LinearLayout layout_yolcu, layout_sofor;
    private TextView yolcu_tex, sofor_text, info;

    private Button fromCity;
    private Button toCity;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;

    private RecyclerView recyclerView;
    private AdvertAdapter advertAdapter;
    private ArrayList<Advert> advertList;
    private Advert mılanlar;
    private View view;
    private String cityInfoFrom = null, cityInfoTo = null;
    private SharedPreferences preferences;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();//todo
        user = firebaseAuth.getCurrentUser();
        preferences = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        initView();

        advertList = new ArrayList<>();
        advertAdapter = new AdvertAdapter(advertList, getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        info.setVisibility(View.GONE);
        layout_yolcu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kontrol()) {
                    Firebase_get_yolcu();
                    layout_sofor.setBackgroundResource(R.drawable.anasayfa_secenek_right_tikla);
                    layout_yolcu.setBackgroundResource(R.drawable.anasayfa_secenek_left);
                    yolcu_tex.setTextColor(ContextCompat.getColor(getActivity(), R.color.beyaz));
                    sofor_text.setTextColor(ContextCompat.getColor(getActivity(), R.color.mavi));

                } else {
                    Toast.makeText(getActivity(), "Alanları boş geçmeyiniz", Toast.LENGTH_LONG).show();
                }

            }
        });
        layout_sofor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kontrol()) {
                    Firebase_get_sofor();
                    layout_yolcu.setBackgroundResource(R.drawable.anasayfa_secenek_left_tikla);
                    layout_sofor.setBackgroundResource(R.drawable.anasayfa_secenek_right);
                    yolcu_tex.setTextColor(ContextCompat.getColor(getActivity(), R.color.mavi));
                    sofor_text.setTextColor(ContextCompat.getColor(getActivity(), R.color.beyaz));
                } else {
                    Toast.makeText(getActivity(), "Alanları boş geçmeyiniz", Toast.LENGTH_LONG).show();
                }


            }
        });


        if (!preferences.getString(SEARCH_FROM_CITY, "0").equals("0")) {

            fromCity.setText(preferences.getString(SEARCH_FROM_CITY, "1"));
        }

        if (!preferences.getString(SEARCH_TO_CITY, "0").equals("0")) {

            toCity.setText(preferences.getString(SEARCH_TO_CITY, "1"));
        }


        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cityInfoFrom = getArguments().getString(SEARCH_FROM_CITY);
            cityInfoTo = getArguments().getString(SEARCH_TO_CITY);
            SharedPreferences preferences = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            if (cityInfoFrom != null) {
                editor.putString(SEARCH_FROM_CITY, cityInfoFrom);
                editor.commit();
            }
            if (cityInfoTo != null) {
                editor.putString(SEARCH_TO_CITY, cityInfoTo);
                editor.commit();

            }
        }
    }

    @Override
    public void onClick(View view) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        switch (view.getId()) {
            case R.id.search_from:
                args.putString(CITY, SEARCH_FROM_CITY);
                break;
            case R.id.search_to:
                args.putString(CITY, SEARCH_TO_CITY);
                break;
        }
        fragment.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.main_framelayout, fragment).commit();

    }

    private void initView() {
        fromCity = view.findViewById(R.id.search_from);
        fromCity.setOnClickListener(this);
        toCity = view.findViewById(R.id.search_to);
        toCity.setOnClickListener(this);
        info = view.findViewById(R.id.info);
        recyclerView = view.findViewById(R.id.search_recylerview);
        layout_yolcu = view.findViewById(R.id.yolcu_layout);
        layout_sofor = view.findViewById(R.id.sofor_layout);
        yolcu_tex = view.findViewById(R.id.yolcu_text);
        sofor_text = view.findViewById(R.id.sofor_text);
    }

    public boolean kontrol() {
        if (fromCity.getText().toString().matches("")) {
            return false;
        } else if (toCity.getText().toString().matches("")) {
            return false;
        } else return true;
    }

    public void Firebase_get_yolcu() {
        advertList.clear();
        databaseReference.child("Yolcu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    mılanlar = ds.getValue(Advert.class);
                    if ((mılanlar.getNereden().equals(fromCity.getText().toString())) &&
                            (mılanlar.getNereye().equals(toCity.getText().toString())) &&
                            mılanlar.getStatu().equals("Yolcu")) {
                        info.setVisibility(View.GONE);
                        advertList.add(mılanlar);
                        recyclerView.setAdapter(advertAdapter);

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
        advertList.clear();
        databaseReference.child("Yolcu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    mılanlar = ds.getValue(Advert.class);
                    if ((mılanlar.getNereden().equals(fromCity.getText().toString())) &&
                            (mılanlar.getNereye().equals(toCity.getText().toString())) &&
                            mılanlar.getStatu().equals("Şoför")) {
                        info.setVisibility(View.GONE);
                        advertList.add(mılanlar);
                        recyclerView.setAdapter(advertAdapter);
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
