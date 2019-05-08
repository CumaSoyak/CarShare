package com.araba.cuma.araba.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.araba.cuma.araba.Adapter.BidAdapter;
import com.araba.cuma.araba.Adapter.MessageAdapter;
import com.araba.cuma.araba.Model.Bid;
import com.araba.cuma.araba.Model.Chat;
import com.araba.cuma.araba.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    private LinearLayout info;
    private ProgressBar progressBar;
    private DatabaseReference reference;


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
        progressBar = view.findViewById(R.id.progressbar);
        info = view.findViewById(R.id.info);
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
        reference = FirebaseDatabase.getInstance().getReference("teklif").child(currentUserId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bidList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Bid mBid = snapshot.getValue(Bid.class);
                    if (snapshot.child("receiveBidUserId").exists())
                    {
                            if (mBid.getReceiveBidUserId().equals(currentUserId)) {
                                bidList.add(mBid);
                                recyclerView.setAdapter(bidAdapter);
                                bidAdapter.notifyDataSetChanged();
                            }

                    }


                }
                progressBar.setVisibility(View.GONE);
                if (bidList.size() == 0)
                    info.setVisibility(View.VISIBLE);
                else
                    info.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
