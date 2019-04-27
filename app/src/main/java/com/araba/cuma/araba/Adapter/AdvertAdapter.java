package com.araba.cuma.araba.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.araba.cuma.araba.Model.Advert;
import com.araba.cuma.araba.Fragment.ReviewAdvertFragment;
import com.araba.cuma.araba.R;
import com.araba.cuma.araba.AdvertOption;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class AdvertAdapter extends RecyclerView.Adapter<AdvertAdapter.MyviewHolder> {
    private static final String USER_ID = "USER_ID";
    private static final String STATUS = "STATUS";
    private static final String USER_PHOTO = "USER_PHOTO";
    private static final String USERNAME = "USERNAME";
    private static final String FROM_CITY = "FROM_CITY";
    private static final String TO_CITY = "TO_CITY";
    private static final String DATE = "DATE";
    private static final String TIME = "TIME";
    private static final String PERSON = "PERSON";
    private static final String CAR = "CAR";
    private static final String MATERIAL = "MATERIAL";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String ADVERT_ID = "ADVERT_ID";
    private FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();

    private List<Advert> advertList;
    private Context context;
    private boolean visibleChoose = false;


    public AdvertAdapter(ArrayList<Advert> advertList, Context context) {
        this.context = context;
        this.advertList = advertList;
    }

    public AdvertAdapter(ArrayList<Advert> advertList, Context context, boolean visibleChoose) {
        this.context = context;
        this.advertList = advertList;
        this.visibleChoose = visibleChoose;
    }


    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_advert, viewGroup, false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyviewHolder myviewHolder, final int i) {
        final Advert advert = advertList.get(i);
        if (visibleChoose) {
            myviewHolder.optionAdvert.setVisibility(View.GONE);
            myviewHolder.bidButton.setVisibility(View.GONE);
        }
        myviewHolder.nameSurnameAdvert.setText(advert.getNameSurname());
        myviewHolder.fromAdvert.setText(advert.getFromCity());
        myviewHolder.toAdvert.setText(advert.getToCity());
        myviewHolder.statusAdvert.setText(advert.getStatus());
        myviewHolder.advertDate.setText(advert.getDate());
        Glide.with(context).load(advert.getImageUrl()).into(myviewHolder.userImageAdvert);
        myviewHolder.bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReviewAdvertFragment fragment = new ReviewAdvertFragment();
                Bundle args = new Bundle();
                args.putString(ADVERT_ID, advert.getAdvertId());
                args.putString(USER_ID, advert.getUserId());
                args.putString(STATUS, advert.getStatus());
                args.putString(USER_PHOTO, advert.getImageUrl());
                args.putString(USERNAME, advert.getNameSurname());
                args.putString(FROM_CITY, advert.getFromCity());
                args.putString(TO_CITY, advert.getToCity());
                args.putString(DATE, advert.getDate());
                args.putString(TIME, advert.getTime());
                args.putString(DESCRIPTION, advert.getDescription());
                args.putString(PERSON, advert.getDriverPerson());

                if (advert.getStatus().equals(context.getResources().getString(R.string.chauffeur))) {
                    /** if incoming data driver's data */
                    args.putString(CAR, advert.getCarModel());
                    args.putString(PERSON, advert.getDriverPerson());

                }
                if (advert.getStatus().equals(context.getResources().getString(R.string.passenger))) {
                    /** if incoming data passenger's data */
                    args.putString(MATERIAL, advert.getMaterial());
                    args.putString(PERSON, advert.getTravelerPerson());

                }
                fragment.setArguments(args);
                ((AppCompatActivity) context).getSupportFragmentManager().
                        beginTransaction().
                        replace(R.id.main_framelayout, fragment).
                        addToBackStack(null).commit();

            }
        });
        myviewHolder.optionAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, myviewHolder.optionAdvert);
                popupMenu.inflate(R.menu.advert_option);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String dbName = null;
                        AdvertOption advertOption;
                        advertOption = new AdvertOption(context);
                        switch (menuItem.getItemId()) {
                            case R.id.complaint:
                                dbName = "ComplaintAdvert";
                                break;
                            case R.id.save:
                                dbName = "SaveAdvert";
                                break;
                        }
                        if (dbName != null) {
                            advertOption.optionAdvert(dbName,
                                    advert.getAdvertId(), advert.getUserId(), advert.getStatus(), advert.getImageUrl(),
                                    advert.getNameSurname(), advert.getFromCity(), advert.getToCity(), advert.getDate(),
                                    advert.getTime(), advert.getDescription(), advert.getDriverPerson(),
                                    advert.getTravelerPerson(), advert.getCarModel(), advert.getMaterial(), fuser.getUid());
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return advertList.size();
    }


    public class MyviewHolder extends RecyclerView.ViewHolder {
        private TextView statusAdvert, fromAdvert,
                toAdvert, nameSurnameAdvert;
        private CardView cardViewAdvert;
        private ImageView userImageAdvert, optionAdvert;
        private TextView userPointAdvert, advertDate;
        private TextView bidButton;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            statusAdvert = itemView.findViewById(R.id.statu_advert);
            fromAdvert = itemView.findViewById(R.id.from_driver_advert);
            toAdvert = itemView.findViewById(R.id.to__driver_advert);
            nameSurnameAdvert = itemView.findViewById(R.id.user_surname_advert);
            cardViewAdvert = itemView.findViewById(R.id.advert_cardview);
            bidButton = itemView.findViewById(R.id.bid_button);
            userImageAdvert = itemView.findViewById(R.id.userImage_advert);
            userPointAdvert = itemView.findViewById(R.id.user_point_advert);
            optionAdvert = itemView.findViewById(R.id.option_advert);
            advertDate = itemView.findViewById(R.id.advert_date);
        }
    }
}
