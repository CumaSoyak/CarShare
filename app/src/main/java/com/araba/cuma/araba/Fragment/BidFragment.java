package com.araba.cuma.araba.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.araba.cuma.araba.Adapter.BidAdapter;
import com.araba.cuma.araba.Class.Bid;
import com.araba.cuma.araba.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class BidFragment extends Fragment {

    private BidAdapter bidAdapter;
    private Bid mteklifler;
    private RecyclerView recyclerView;
    private ArrayList<Bid> bidList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String user_id;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bid, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        user = firebaseAuth.getCurrentUser();
        user_id = user.getUid();

        recyclerView = view.findViewById(R.id.teklif_recylerview);
        bidList = new ArrayList<Bid>();
        bidAdapter = new BidAdapter(bidList,getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        Firebase_get_yolcu();

        return view;
    }

    public void Firebase_get_yolcu() {
        databaseReference.child("Teklif").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    mteklifler = ds.getValue(Bid.class);
                    if (mteklifler.getNereden().matches(""))
                    {
                        Toast.makeText(getActivity(), "Alınan teklif bulunmamaktadır !", Toast.LENGTH_LONG).show();
                    }
                    else {
                        bidList.add(mteklifler);
                        recyclerView.setAdapter(bidAdapter);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
