package com.araba.cuma.araba.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.araba.cuma.araba.Class.Bid;
import com.araba.cuma.araba.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BidAdapter extends RecyclerView.Adapter<BidAdapter.MyviewHolder> {

    private List<Bid> bidList = new ArrayList<>();
    private Context context;

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
    public void onBindViewHolder(@NonNull final MyviewHolder myviewHolder, int i) {
        myviewHolder.statuBid.setText(bidList.get(i).getStatu());
        myviewHolder.nameBid.setText(bidList.get(i).getAd());
        myviewHolder.surnameBid.setText(bidList.get(i).getSoyad());
        myviewHolder.priceBid.setText(bidList.get(i).getTeklif());
        myviewHolder.fromBid.setText(bidList.get(i).getNereden());
        myviewHolder.toBid.setText(bidList.get(i).getNereye());
        myviewHolder.userPointBid.setText(String.valueOf(bidList.get(i).getKullanicipuan()));

        myviewHolder.settingsBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, myviewHolder.settingsBid);
                popupMenu.inflate(R.menu.bid_settings);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.complaint:
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
                surnameBid, priceBid, userPointBid;
        private CircleImageView userImageBid;
        private ImageView settingsBid;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            statuBid = itemView.findViewById(R.id.statu_bid);
            fromBid = itemView.findViewById(R.id.from_bid);
            toBid = itemView.findViewById(R.id.to_bid);
            nameBid = itemView.findViewById(R.id.name_bid);
            surnameBid = itemView.findViewById(R.id.surname_bid);
            priceBid = itemView.findViewById(R.id.price_bid);
            userPointBid = itemView.findViewById(R.id.user_point_bid);
            userImageBid = itemView.findViewById(R.id.userImage_bid);
            settingsBid = itemView.findViewById(R.id.settings_bid);


        }
    }
}
