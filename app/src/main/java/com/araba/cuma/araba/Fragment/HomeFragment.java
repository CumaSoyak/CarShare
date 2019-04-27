package com.araba.cuma.araba.Fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener {

    LinearLayout layoutTravels;
    ImageView imageTraveler, imageDriver;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    FirebaseUser fuser;
    private FirebaseAuth firebaseAuth;
    private String userId;

    private RecyclerView recyclerView;
    private AdvertAdapter advertAdapter;
    private ArrayList<Advert> advertList;
    private Advert mAdvert;
    private LinearLayout toolbar;
    private FrameLayout frameLayoutImage;


    private TextView info_home;
    final String PREFS_NAME = "MyPrefsFile";
    SharedPreferences settings;
    View arka, view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        userId = fuser.getUid();
        initView();
        //animation();
        setupRecylerView();


        return view;
    }

    private void initView() {
        info_home = view.findViewById(R.id.info_home);
        recyclerView = view.findViewById(R.id.home_recylerview);
        toolbar = view.findViewById(R.id.toolbar);
        frameLayoutImage = view.findViewById(R.id.reklam);
        layoutTravels = view.findViewById(R.id.travels);
        layoutTravels.setOnClickListener(this);


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
        frameLayoutImage.setVisibility(View.GONE);
        getAdvert();
    }

    private void setupRecylerView() {
        advertList = new ArrayList<>();
        advertAdapter = new AdvertAdapter(advertList, getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
    }

    private void getAdvert() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("ilan")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                mAdvert = document.toObject(Advert.class);
                                if ((!mAdvert.getUserId().equals(userId))) {
                                    advertList.add(mAdvert);
                                    if (advertList.size() == 0)
                                        getAdvert();
                                    recyclerView.setAdapter(advertAdapter);
                                    advertAdapter.notifyDataSetChanged();
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), (CharSequence) task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.travels:
                // layoutTravels.setBackgroundResource(R.drawable.custom_layout_home_click);
                frameLayoutImage.setVisibility(View.GONE);
                getAdvert();
                animation();
                break;
        }
    }

    private void animation() {
        /*
        Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                "Look at me, I'm a fancy snackbar", Snackbar.LENGTH_LONG);
        snackBar.show();
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
        toolbar.setAnimation(animation);
        */
    }
}
