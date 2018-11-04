package com.araba.cuma.araba.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.araba.cuma.araba.Adapter.TeklifAdapter;
import com.araba.cuma.araba.Class.Teklifler;
import com.araba.cuma.araba.R;

import java.util.ArrayList;
import java.util.List;


public class BidFragment extends Fragment {

    private TeklifAdapter teklifAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Teklifler> tekliflerList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bid, container, false);

        recyclerView=view.findViewById(R.id.teklif_recylerview);
        tekliflerList=new ArrayList<Teklifler>();
        teklifAdapter=new TeklifAdapter(tekliflerList);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(teklifAdapter);

        tekliflerList.add(new Teklifler("Yolcuyum","Cuma","Soyak","Niğde","Elazığ",45,4));
        tekliflerList.add(new Teklifler("Yolcuyum","Cuma","Soyak","Niğde","Elazığ",45,4));
        tekliflerList.add(new Teklifler("Yolcuyum","Cuma","Soyak","Niğde","Elazığ",45,4));
        tekliflerList.add(new Teklifler("Yolcuyum","Cuma","Soyak","Niğde","Elazığ",45,4));
        tekliflerList.add(new Teklifler("Yolcuyum","Cuma","Soyak","Niğde","Elazığ",45,4));
        tekliflerList.add(new Teklifler("Yolcuyum","Cuma","Soyak","Niğde","Elazığ",45,4));
        tekliflerList.add(new Teklifler("Yolcuyum","Cuma","Soyak","Niğde","Elazığ",45,4));
        tekliflerList.add(new Teklifler("Yolcuyum","Cuma","Soyak","Niğde","Elazığ",45,4));




        return view;
    }

}
