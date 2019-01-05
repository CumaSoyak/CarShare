package com.araba.cuma.araba.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.araba.cuma.araba.Class.Teklifler;
import com.araba.cuma.araba.R;

import java.util.ArrayList;
import java.util.List;

public class TeklifAdapter extends RecyclerView.Adapter<TeklifAdapter.MyviewHolder> {

    private List<Teklifler> tekliflerList = new ArrayList<>();

    public TeklifAdapter(List<Teklifler> tekliflerList) {
        this.tekliflerList = tekliflerList;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.teklifler_goster_cardview, viewGroup, false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder myviewHolder, int i) {

        //myviewHolder.kullanici_puan.setRating(tekliflerList.get(i).getPuan());
        myviewHolder.ad.setText(tekliflerList.get(i).getAd());
        myviewHolder.soyad.setText(tekliflerList.get(i).getSoyad());
        myviewHolder.teklif.setText(tekliflerList.get(i).getTeklif());
        myviewHolder.nereden.setText(tekliflerList.get(i).getNereden());
        myviewHolder.nereye.setText(tekliflerList.get(i).getNereye());
        myviewHolder.statu.setText(tekliflerList.get(i).getStatu());

    }

    @Override
    public int getItemCount() {
        return tekliflerList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        public TextView nereden, nereye, fiyat, ad, soyad, statu,teklif;
        public RatingBar kullanici_puan;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            kullanici_puan = itemView.findViewById(R.id.kullanici_puan);
            nereden = itemView.findViewById(R.id.nereden);
            nereye = itemView.findViewById(R.id.nereye);
            ad = itemView.findViewById(R.id.ad);
            soyad = itemView.findViewById(R.id.soyad);
            statu = itemView.findViewById(R.id.statu);
            teklif = itemView.findViewById(R.id.teklif);
        }
    }
}
