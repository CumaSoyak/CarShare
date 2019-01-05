package com.araba.cuma.araba.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.araba.cuma.araba.Activity.MesajActivity;
import com.araba.cuma.araba.Class.Ilanlar;
import com.araba.cuma.araba.Fragment.HomeFragment;
import com.araba.cuma.araba.Fragment.MesajFragment;
import com.araba.cuma.araba.R;

import java.util.ArrayList;
import java.util.List;

public class IlanAdapter extends RecyclerView.Adapter<IlanAdapter.MyviewHolder> {
    private List<Ilanlar> ılanlarList;
    private Context context;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public IlanAdapter(ArrayList<Ilanlar> ılanlarList, Context context) {
        this.context = context;
        this.ılanlarList = ılanlarList;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ilanlar_goster_card_view, viewGroup, false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder myviewHolder, final int i) {

        //myviewHolder.kullanici_puan.setRating(ılanlarList.get(i).getKullanicipuan());
        myviewHolder.ad.setText(ılanlarList.get(i).getAd());
        myviewHolder.soyad.setText(ılanlarList.get(i).getSoyad());
        myviewHolder.nereden.setText(ılanlarList.get(i).getNereden());
        myviewHolder.nereye.setText(ılanlarList.get(i).getNereye());
        myviewHolder.statu.setText(ılanlarList.get(i).getStatu());
        myviewHolder.fiyat.setText(ılanlarList.get(i).getFiyat());
        myviewHolder.ilan_ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MesajActivity.class);
                intent.putExtra("ilan_id", String.valueOf(ılanlarList.get(i).getId()));
                intent.putExtra("user_id", String.valueOf(ılanlarList.get(i).getUserid()));
                intent.putExtra("nereden", String.valueOf(ılanlarList.get(i).getNereden()));
                intent.putExtra("nereye", String.valueOf(ılanlarList.get(i).getNereye()));
                intent.putExtra("statu", String.valueOf(ılanlarList.get(i).getStatu()));
                context.startActivity(intent);
            }
        });
        myviewHolder.ilan_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, MesajActivity.class);
                intent.putExtra("ilan_id", String.valueOf(ılanlarList.get(i).getId()));
                intent.putExtra("user_id", String.valueOf(ılanlarList.get(i).getUserid()));
                intent.putExtra("nereden", String.valueOf(ılanlarList.get(i).getNereden()));
                intent.putExtra("nereye", String.valueOf(ılanlarList.get(i).getNereye()));
                intent.putExtra("statu", String.valueOf(ılanlarList.get(i).getStatu()));
                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return ılanlarList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        public TextView nereden, nereye, fiyat, ad, soyad, statu;
        public int kullanici_puan;
        public CardView ilan_cardview;
        public Button ilan_ver;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            //  kullanici_puan = itemView.findViewById(R.id.kullanici_puan);
            nereden = itemView.findViewById(R.id.nereden);
            nereye = itemView.findViewById(R.id.nereye);
            ad = itemView.findViewById(R.id.ad);
            soyad = itemView.findViewById(R.id.soyad);
            statu = itemView.findViewById(R.id.statu);
            ilan_cardview = itemView.findViewById(R.id.ilan_cardview);
            ilan_ver = itemView.findViewById(R.id.ilan_ver_buton);
            fiyat = itemView.findViewById(R.id.ilan_fiyat);

        }
    }
}
