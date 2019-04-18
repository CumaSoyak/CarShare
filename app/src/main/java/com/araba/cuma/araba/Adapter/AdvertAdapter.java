package com.araba.cuma.araba.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.araba.cuma.araba.Activity.MesajActivity;
import com.araba.cuma.araba.Class.Advert;
import com.araba.cuma.araba.R;

import java.util.ArrayList;
import java.util.List;

public class AdvertAdapter extends RecyclerView.Adapter<AdvertAdapter.MyviewHolder> {
    private List<Advert> advertList;
    private Context context;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public AdvertAdapter(ArrayList<Advert> advertList, Context context) {
        this.context = context;
        this.advertList = advertList;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_advert, viewGroup, false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder myviewHolder, final int i) {
        //todo user ımage çekilecek
        myviewHolder.nameAdvert.setText(advertList.get(i).getNameSurname());
        myviewHolder.fromAdvert.setText(advertList.get(i).getFromCity());
        myviewHolder.toAdvert.setText(advertList.get(i).getToCity());
        myviewHolder.statuAdvert.setText(advertList.get(i).getNameSurname());
        myviewHolder.cardViewAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MesajActivity.class);
                intent.putExtra("ilan_id", String.valueOf(advertList.get(i).getAdvertId()));
                intent.putExtra("user_id", String.valueOf(advertList.get(i).getUserId()));
                intent.putExtra("nereden", String.valueOf(advertList.get(i).getFromCity()));
                intent.putExtra("nereye", String.valueOf(advertList.get(i).getToCity()));
                intent.putExtra("statu", String.valueOf(advertList.get(i).getNameSurname()));
                context.startActivity(intent);


            }
        });
        myviewHolder.bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
    }

    @Override
    public int getItemCount() {
        return advertList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        private TextView statuAdvert, fromAdvert,
                toAdvert, nameAdvert, surnameAdvert;
        private CardView cardViewAdvert;
        private ImageView userImageAdvert;
        private RatingBar userPointAdvert;
        Button bidButton;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            statuAdvert = itemView.findViewById(R.id.statu_advert);
            fromAdvert = itemView.findViewById(R.id.from_driver_advert);
            toAdvert = itemView.findViewById(R.id.to__driver_advert);
            nameAdvert = itemView.findViewById(R.id.user_name_advert);
            surnameAdvert = itemView.findViewById(R.id.user_surname_advert);
            cardViewAdvert = itemView.findViewById(R.id.advert_cardview);
            bidButton = itemView.findViewById(R.id.bid_button);
            userImageAdvert = itemView.findViewById(R.id.userImage_advert);
            userPointAdvert = itemView.findViewById(R.id.user_point_advert);

        }
    }
}
