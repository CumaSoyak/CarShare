package com.araba.cuma.araba.Fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.araba.cuma.araba.Activity.MainActivity;
import com.araba.cuma.araba.Activity.MesajActivity;
import com.araba.cuma.araba.Adapter.IlanAdapter;
import com.araba.cuma.araba.Api.Api;
import com.araba.cuma.araba.Class.Ilanlar;
import com.araba.cuma.araba.Class.Teklifler;
import com.araba.cuma.araba.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    LinearLayout linearLayout_yolcu, linearLayout_sofor;
    ImageView yolcu_resim, sofor_resim;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String user_id;

    private RecyclerView recyclerView;
    private IlanAdapter ılanAdapter;
    private ArrayList<Ilanlar> ilanlarList;
    private Ilanlar mılanlar;

    private String gelen_ad;
    private String gelen_soyad;
    private String gelen_telefon;

    final String PREFS_NAME = "MyPrefsFile";
    SharedPreferences settings;
    private int filtrele = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        user = firebaseAuth.getCurrentUser();
        user_id = user.getUid();

        settings = getActivity().getSharedPreferences(PREFS_NAME, 0);

        UUID uuıd = UUID.randomUUID();
        linearLayout_sofor = view.findViewById(R.id.sofor_cardView);
        linearLayout_yolcu = view.findViewById(R.id.yolcu_cardView);
        yolcu_resim = view.findViewById(R.id.yolcu_imageView);
        sofor_resim = view.findViewById(R.id.sofor_imageView);

        recyclerView = view.findViewById(R.id.home_recylerview);
        ilanlarList = new ArrayList<>();
        ılanAdapter = new IlanAdapter(ilanlarList, getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        //get_ilanlar();
        filtrele = 0;
        Firebase_get_yolcu();
        linearLayout_yolcu.performClick();

        linearLayout_yolcu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtrele = 1;
                Firebase_get_yolcu();
                linearLayout_sofor.setBackgroundResource(R.drawable.anasayfa_secenek_right_tikla);
                sofor_resim.setImageResource(R.drawable.sofor_siyah);
                yolcu_resim.setImageResource(R.drawable.yolcu);
                linearLayout_yolcu.setBackgroundResource(R.drawable.anasayfa_secenek_left);

            }
        });
        linearLayout_sofor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtrele = 2;
                Firebase_get_yolcu();
                linearLayout_yolcu.setBackgroundResource(R.drawable.anasayfa_secenek_left_tikla);
                yolcu_resim.setImageResource(R.drawable.yolcu_siyah);
                sofor_resim.setImageResource(R.drawable.sofor);
                linearLayout_sofor.setBackgroundResource(R.drawable.anasayfa_secenek_right);

            }
        });

        ilk();
        bilgi_al();
        return view;
    }


    public void Firebase_get_yolcu() {
        databaseReference.child("Yolcu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                switch (filtrele) {
                    case 0:
                        ilanlarList.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            mılanlar = ds.getValue(Ilanlar.class);
                            if (!mılanlar.getUserid().equals(user_id)) {
                                ilanlarList.add(mılanlar);
                                recyclerView.setAdapter(ılanAdapter);
                            }
                        }
                        break;
                    case 1:
                        ilanlarList.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            mılanlar = ds.getValue(Ilanlar.class);
                            if ((!mılanlar.getUserid().equals(user_id)) && (mılanlar.getStatu().equals("Yolcu"))) {
                                ilanlarList.add(mılanlar);
                                recyclerView.setAdapter(ılanAdapter);
                            }
                        }
                        break;
                    case 2:
                        ilanlarList.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            mılanlar = ds.getValue(Ilanlar.class);
                            if ((!mılanlar.getUserid().equals(user_id)) && (mılanlar.getStatu().equals("Şoför"))) {
                                ilanlarList.add(mılanlar);
                                recyclerView.setAdapter(ılanAdapter);
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


    public void get_ilanlar() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<List<Ilanlar>> call = api.getIlanlar();

        call.enqueue(new Callback<List<Ilanlar>>() {
            @Override
            public void onResponse(Call<List<Ilanlar>> call, Response<List<Ilanlar>> response) {
                List<Ilanlar> ılanlar = response.body();
                for (Ilanlar ilan : ılanlar) {

                }
                Toast.makeText(getContext(), "Oldu", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<List<Ilanlar>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void ilk() {

        if (settings.getBoolean("my_first_time", true)) {

            SharedPreferences.Editor editor = settings.edit();

            Intent ıntent = getActivity().getIntent();
            Bundle bundle = ıntent.getExtras();
            String ad = (String) bundle.get("ad");
            String soyad = (String) bundle.get("soyad");
            String telefon = (String) bundle.get("telefon");

            editor.putString("ad", ad);
            editor.putString("soyad", soyad);
            editor.putString("telefon", telefon);
            editor.commit();


            databaseReference.child("User").child(user_id).child("ad").setValue(ad);
            databaseReference.child("User").child(user_id).child("soyad").setValue(soyad);
            databaseReference.child("User").child(user_id).child("telefon").setValue(telefon);

            settings.edit().putBoolean("my_first_time", false).commit();
        }

    }

    public void bilgi_al() {

        gelen_ad = settings.getString("ad", "default");
        gelen_soyad = settings.getString("soyad", "default");
        gelen_telefon = settings.getString("telefon", "default");

        Log.i("bilgi", ":" + gelen_ad + gelen_soyad + gelen_telefon);

        YolcuFragment yolcuFragment = new YolcuFragment();
        SoforFragment soforFragment = new SoforFragment();
        ProfileFragment profileFragment = new ProfileFragment();
        MesajActivity mesajActivity = new MesajActivity();
        mesajActivity.kisi_bilgi_al(gelen_ad, gelen_soyad, gelen_telefon);
        profileFragment.kisi_bilgi_al(gelen_ad, gelen_soyad, gelen_telefon);
        yolcuFragment.kisi_bilgi_al(gelen_ad, gelen_soyad, gelen_telefon);
        soforFragment.kisi_bilgi_al(gelen_ad, gelen_soyad, gelen_telefon);
    }

}
