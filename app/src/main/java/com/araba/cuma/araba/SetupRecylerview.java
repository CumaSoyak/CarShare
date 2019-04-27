package com.araba.cuma.araba;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Adapter;

import com.araba.cuma.araba.Adapter.AdvertAdapter;
import com.araba.cuma.araba.Model.Advert;

import java.util.ArrayList;

public class SetupRecylerview {

    public void setupRecylerView(Context context,RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

    }
}
