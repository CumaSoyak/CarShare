package com.araba.cuma.araba.ProfileChooseFragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.araba.cuma.araba.Adapter.AdvertAdapter;
import com.araba.cuma.araba.Model.Advert;
import com.araba.cuma.araba.R;
import com.araba.cuma.araba.SetupRecylerview;
import com.araba.cuma.araba.ToolbarSetup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SavedAdvertFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView dontFound;
    private AdvertAdapter advertAdapter;
    private ArrayList<Advert> advertList;
    private Advert mAdvert;
    private View view;
    private FirebaseUser fuser;
    private String userId;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;

    public SavedAdvertFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_saved, container, false);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        userId = fuser.getUid();
        initView();

        ToolbarSetup toolbarSetup = new ToolbarSetup();
        toolbarSetup.setUpToolbar(getActivity(), "Kaydedilen Yolculuklar", toolbar);

        SetupRecylerview setupRecylerview = new SetupRecylerview();
        setupRecylerview.setupRecylerView(getActivity(), recyclerView);
        getAdvert();
        return view;
    }

    private void initView() {
        recyclerView = view.findViewById(R.id.saved_recyclerview);
        dontFound = view.findViewById(R.id.my_saved_dont_foud);
        appBarLayout = view.findViewById(R.id.include_app_bar);
        toolbar = appBarLayout.findViewById(R.id.toolbar);
    }

    private void getAdvert() {
        advertList = new ArrayList<>();
        advertAdapter = new AdvertAdapter(advertList, getActivity());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("SaveAdvert").whereEqualTo("savedUserId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                mAdvert = document.toObject(Advert.class);
                                advertList.add(mAdvert);
                                recyclerView.setAdapter(advertAdapter);
                                advertAdapter.notifyDataSetChanged();
                            }
                            if (advertList.size() == 0)
                                dontFound.setVisibility(View.VISIBLE);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
