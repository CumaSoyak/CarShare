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
import com.araba.cuma.araba.Model.Bid;
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


public class BidFragment extends Fragment {

    private BidAdapter bidAdapter;
    private Bid mteklifler;
    private RecyclerView recyclerView;
    private ArrayList<Bid> bidList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String currentUserId;
    private Bid mBid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bid, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        user = firebaseAuth.getCurrentUser();
        currentUserId = user.getUid();

        recyclerView = view.findViewById(R.id.teklif_recylerview);
        bidList = new ArrayList<Bid>();
        bidAdapter = new BidAdapter(bidList, getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        getBid();
        return view;
    }

    private void getBid() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("teklif").whereEqualTo("receiveBidUserId",currentUserId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                mBid = document.toObject(Bid.class);
                                if ((!mBid.getGiveBidUserId().equals(currentUserId))) {
                                    bidList.add(mBid);
                                    if (bidList.size() == 0)
                                        getBid();
                                    recyclerView.setAdapter(bidAdapter);
                                    bidAdapter.notifyDataSetChanged();
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


}
