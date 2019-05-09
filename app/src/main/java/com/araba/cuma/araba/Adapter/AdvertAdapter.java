package com.araba.cuma.araba.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Random;

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
        final Advert advert= advertList.get(i);
        if (visibleChoose) {
             myviewHolder.bidButton.setVisibility(View.GONE);
        }
        myviewHolder.nameSurnameAdvert.setText(advert.getNameSurname());
        myviewHolder.fromAdvert.setText(advert.getFromCity());
        myviewHolder.toAdvert.setText(advert.getToCity());
        myviewHolder.statusAdvert.setText(advert.getStatus());
        myviewHolder.advertDate.setText(advert.getDate());
        myviewHolder.advertTime.setText(advert.getTime());
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
                optionAdvert(myviewHolder,advert,visibleChoose);
            }
        });

        myviewHolder.userPointAdvert.setText("1.0");
    }

    @Override
    public int getItemCount() {
        return advertList.size();
    }


    public class MyviewHolder extends RecyclerView.ViewHolder {
        private TextView statusAdvert, fromAdvert,
                toAdvert, nameSurnameAdvert;
        private ConstraintLayout cardViewAdvert;
        private ImageView userImageAdvert, optionAdvert;
        private TextView userPointAdvert, advertDate, advertTime;
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
            advertTime = itemView.findViewById(R.id.advert_time);
        }
    }

    private void optionAdvert(MyviewHolder myviewHolder,final Advert advert,boolean visibleChoose) {
        final PopupMenu popupMenu = new PopupMenu(context, myviewHolder.optionAdvert);
        Menu menu=popupMenu.getMenu();
        popupMenu.inflate(R.menu.advert_option);
        if (visibleChoose){
            menu.findItem(R.id.complaint).setVisible(false);
            menu.findItem(R.id.save).setVisible(false);
            menu.findItem(R.id.delete).setVisible(true);

        }
        if (fuser.getUid().equals(advert.getUserId())) {
            menu.findItem(R.id.delete).setVisible(true);
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                String dbName ;
                AdvertOption advertOption;
                advertOption = new AdvertOption(context);
                switch (menuItem.getItemId()) {
                    case R.id.complaint:
                        dbName = "ComplaintAdvert";
                        advertOption.advertAddOrComplation(dbName,
                                advert.getAdvertId(), advert.getUserId(), advert.getStatus(), advert.getImageUrl(),
                                advert.getNameSurname(), advert.getFromCity(), advert.getToCity(), advert.getDate(),
                                advert.getTime(), advert.getDescription(), advert.getDriverPerson(),
                                advert.getTravelerPerson(), advert.getCarModel(), advert.getMaterial(), fuser.getUid());
                        break;
                    case R.id.save:
                        dbName = "SaveAdvert";
                        advertOption.advertAddOrComplation(dbName,
                                advert.getAdvertId(), advert.getUserId(), advert.getStatus(), advert.getImageUrl(),
                                advert.getNameSurname(), advert.getFromCity(), advert.getToCity(), advert.getDate(),
                                advert.getTime(), advert.getDescription(), advert.getDriverPerson(),
                                advert.getTravelerPerson(), advert.getCarModel(), advert.getMaterial(), fuser.getUid());
                        break;
                    case R.id.delete:
                        advertOption.deleteAdvert(advert.getAdvertId());
                        notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }
}
