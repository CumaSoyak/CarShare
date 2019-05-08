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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.araba.cuma.araba.Adapter.AdvertAdapter;
import com.araba.cuma.araba.Model.Advert;
import com.araba.cuma.araba.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static com.araba.cuma.araba.Constant.*;

import java.util.ArrayList;


public class SearchFragment extends Fragment implements View.OnClickListener {
    private RelativeLayout layout_yolcu, layout_sofor;
    private TextView yolcu_tex, sofor_text, infoSearch;

    private Button fromCity;
    private Button toCity;
    private View toggleTraveler, toggleDriver;

    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;

    private RecyclerView recyclerView;
    private AdvertAdapter advertAdapter;
    private ArrayList<Advert> advertList;
    private Advert mılanlar;
    private View view;
    private String cityInfoFrom = null, cityInfoTo = null;
    private SharedPreferences preferences;
    private String userId;
    private String statusString = null;
    private ImageView womanImage;
    private ProgressBar progressBar;


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        preferences = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        initView();

        advertList = new ArrayList<>();
        advertAdapter = new AdvertAdapter(advertList, getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);


        fromCity.setText(FROM);
        toCity.setText(TO);


        initLayoutChoose();


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.search_in_down, R.anim.search_out_up)
                .replace(R.id.main_framelayout, fragment).addToBackStack(null).commit();

    }

    private void initView() {
        fromCity = view.findViewById(R.id.search_from);
        fromCity.setOnClickListener(this);
        toCity = view.findViewById(R.id.search_to);
        toCity.setOnClickListener(this);

        womanImage = view.findViewById(R.id.woman);
        progressBar = view.findViewById(R.id.progressbar);
        recyclerView = view.findViewById(R.id.search_recylerview);
        layout_yolcu = view.findViewById(R.id.traveler_layout);
        layout_sofor = view.findViewById(R.id.driver_layout);
        yolcu_tex = view.findViewById(R.id.yolcu_text);
        sofor_text = view.findViewById(R.id.sofor_text);
        toggleTraveler = view.findViewById(R.id.toggle_traveler);
        toggleDriver = view.findViewById(R.id.toggle_driver);
        toggleTraveler.setVisibility(View.GONE);
        toggleDriver.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        infoSearch = view.findViewById(R.id.deneme);
    }

    private void getSearch(String statusString) {
        infoSearch.setVisibility(View.GONE);
        womanImage.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("ilan").
                whereEqualTo("fromCity", fromCity.getText().toString()).
                whereEqualTo("toCity", toCity.getText().toString()).
                whereEqualTo("status", statusString)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            advertList.clear();
                            advertAdapter.notifyDataSetChanged();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                mılanlar = document.toObject(Advert.class);
                                advertList.add(mılanlar);
                                recyclerView.setAdapter(advertAdapter);
                                advertAdapter.notifyDataSetChanged();
                            }
                            progressBar.setVisibility(View.GONE);
                            if (advertList.size() == 0) {
                                infoSearch.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);

                            } else
                            {
                                recyclerView.setVisibility(View.VISIBLE);
                                infoSearch.setVisibility(View.GONE);
                            }



                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void initLayoutChoose() {
        layout_yolcu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kontrol()) {
                    advertList.clear();
                    layout_sofor.setBackgroundResource(R.drawable.anasayfa_secenek_right_tikla);
                    layout_yolcu.setBackgroundResource(R.drawable.anasayfa_secenek_left);
                    yolcu_tex.setTextColor(ContextCompat.getColor(getActivity(), R.color.beyaz));
                    sofor_text.setTextColor(ContextCompat.getColor(getActivity(), R.color.mavi));
                    toggleTraveler.setVisibility(View.VISIBLE);
                    toggleDriver.setVisibility(View.GONE);
                    statusString = getResources().getString(R.string.passenger);
                    getSearch(statusString);


                } else {
                    Toast.makeText(getActivity(), "Alanları boş geçmeyiniz", Toast.LENGTH_LONG).show();
                }

            }
        });
        layout_sofor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kontrol()) {
                    advertList.clear();
                    layout_yolcu.setBackgroundResource(R.drawable.anasayfa_secenek_left_tikla);
                    layout_sofor.setBackgroundResource(R.drawable.anasayfa_secenek_right);
                    yolcu_tex.setTextColor(ContextCompat.getColor(getActivity(), R.color.mavi));
                    sofor_text.setTextColor(ContextCompat.getColor(getActivity(), R.color.beyaz));
                    toggleTraveler.setVisibility(View.GONE);
                    toggleDriver.setVisibility(View.VISIBLE);
                    statusString = getResources().getString(R.string.chauffeur);
                    getSearch(statusString);


                } else {
                    Toast.makeText(getActivity(), "Alanları boş geçmeyiniz", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    public boolean kontrol() {
        if (fromCity.getText().toString().matches("")) {
            return false;
        } else if (toCity.getText().toString().matches("")) {
            return false;
        } else return true;
    }


}
