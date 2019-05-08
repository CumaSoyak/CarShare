package com.araba.cuma.araba.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.araba.cuma.araba.AdvertOption;
import com.araba.cuma.araba.Model.Bid;
import com.araba.cuma.araba.Fragment.MessageFragment;
import com.araba.cuma.araba.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class BidAdapter extends RecyclerView.Adapter<BidAdapter.MyviewHolder> {

    private List<Bid> bidList = new ArrayList<>();
    private Context context;
    private FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
    private String currentUserId = fuser.getUid();
    private String FRIEND_USER_ID = "FRIEND_USER_ID";
    private String ADVERT_ID = "ADVERT_ID";
    private String FROM_CITY = "FROM_CITY";
    private String TO_CITY = "TO_CITY";
    private String USER_PHOTO = "USER_PHOTO";
    private String NAME = "NAME";


    public BidAdapter(List<Bid> bidList, Context context) {
        this.bidList = bidList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_bid, viewGroup, false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyviewHolder myviewHolder, final int i) {
        final Bid bid=bidList.get(i);
        if (bid.getStatus().equals(context.getResources().getString(R.string.chauffeur))) {
            myviewHolder.statuBid.setText(context.getResources().getString(R.string.passenger));
        }
        if (bid.getStatus().equals(context.getResources().getString(R.string.passenger))) {
            myviewHolder.statuBid.setText(context.getResources().getString(R.string.chauffeur));
        }
        myviewHolder.nameBid.setText(bid.getNameSurname());
        myviewHolder.priceBid.setText(bid.getPrice());
        myviewHolder.fromBid.setText(bid.getFromCity());
        myviewHolder.toBid.setText(bid.getToCity());
        myviewHolder.bidReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageFragment fragment = new MessageFragment();
                Bundle bundle = new Bundle();
                if (currentUserId.equals(bid.getGiveBidUserId())) {
                    bundle.putString(FRIEND_USER_ID, bid.getReceiveBidUserId());

                } else {
                    bundle.putString(FRIEND_USER_ID, bid.getGiveBidUserId());

                }
                bundle.putString(ADVERT_ID, bid.getAdvertId());
                bundle.putString(NAME,bid.getNameSurname());
                bundle.putString(FROM_CITY, bid.getFromCity());
                bundle.putString(TO_CITY, bid.getToCity());
                bundle.putString(USER_PHOTO, bid.getImageUrl());
                fragment.setArguments(bundle);
                ((AppCompatActivity) context).getSupportFragmentManager().
                        beginTransaction().
                        replace(R.id.main_framelayout, fragment).
                        addToBackStack(null).commit();
            }
        });
        Glide.with(context).load(bid.getImageUrl()).into(myviewHolder.userImageBid);
        myviewHolder.optionsBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, myviewHolder.optionsBid);
                popupMenu.inflate(R.menu.bid_option);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                         AdvertOption advertOption;
                        advertOption = new AdvertOption(context);
                        switch (menuItem.getItemId()) {
                            case R.id.complaintBid:
                                String dbName = "ComplaintAdvert";
                                advertOption.advertAddOrComplation(dbName,
                                        bid.getAdvertId(), bid.getGiveBidUserId(), bid.getStatus(), bid.getImageUrl(),
                                        bid.getNameSurname(), bid.getFromCity(), bid.getToCity(), bid.getDate(),
                                        bid.getTime(), bid.getDescription(), bid.getDriverPerson(),
                                        bid.getTravelerPerson(), bid.getCarModel(), bid.getMaterial(), fuser.getUid());
                                break;
                            case R.id.deleteBid:
                                advertOption.deleteBid(currentUserId,bid.getGiveBidUserId());
                                break;
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
        return bidList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        private TextView statuBid, fromBid, toBid, nameBid,
                priceBid, userPointBid;
        private ImageView userImageBid;
        private ImageView optionsBid, bidReview;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            statuBid = itemView.findViewById(R.id.statu_bid);
            fromBid = itemView.findViewById(R.id.from_bid);
            toBid = itemView.findViewById(R.id.to_bid);
            nameBid = itemView.findViewById(R.id.name_bid);
            priceBid = itemView.findViewById(R.id.price_bid);
            userPointBid = itemView.findViewById(R.id.user_point_bid);
            userImageBid = itemView.findViewById(R.id.userImage_bid);
            optionsBid = itemView.findViewById(R.id.settings_bid);
            bidReview = itemView.findViewById(R.id.bid_review);

        }
    }
}
