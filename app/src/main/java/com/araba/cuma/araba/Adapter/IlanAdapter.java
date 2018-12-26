package com.araba.cuma.araba.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.araba.cuma.araba.Class.Ilanlar;
import com.araba.cuma.araba.R;

import java.util.ArrayList;
import java.util.List;

public class IlanAdapter  extends RecyclerView.Adapter<IlanAdapter.MyviewHolder> {
    private List<Ilanlar> ılanlarList ;
    private Context context;

    public IlanAdapter(ArrayList<Ilanlar> ılanlarList, Context context) {
        this.context=context;
        this.ılanlarList = ılanlarList;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ilanlar_goster_card_view, viewGroup, false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder myviewHolder, int i) {

        //myviewHolder.kullanici_puan.setRating(ılanlarList.get(i).getKullanicipuan());
        myviewHolder.ad.setText(ılanlarList.get(i).getAd());
        myviewHolder.soyad.setText(ılanlarList.get(i).getSoyad());
        myviewHolder.fiyat.setText(ılanlarList.get(i).getFiyat());
        myviewHolder.nereden.setText(ılanlarList.get(i).getNereden());
        myviewHolder.nereye.setText(ılanlarList.get(i).getNereye());
        myviewHolder.statu.setText(ılanlarList.get(i).getStatu());

    }

    @Override
    public int getItemCount() {
        return ılanlarList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        public TextView  nereden, nereye, fiyat, ad, soyad,statu;
        public int kullanici_puan ;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
          //  kullanici_puan = itemView.findViewById(R.id.kullanici_puan);
            nereden = itemView.findViewById(R.id.nereden);
            nereye = itemView.findViewById(R.id.nereye);
            fiyat = itemView.findViewById(R.id.fiyat);
            ad = itemView.findViewById(R.id.ad);
            soyad = itemView.findViewById(R.id.soyad);
            statu = itemView.findViewById(R.id.statu);
        }
    }
}
