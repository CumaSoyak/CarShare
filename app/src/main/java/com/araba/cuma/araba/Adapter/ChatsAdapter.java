package com.araba.cuma.araba.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.araba.cuma.araba.Model.Chat;
import com.araba.cuma.araba.Fragment.MessageFragment;
import com.araba.cuma.araba.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.MyviewHolder> {

    private List<Chat> chatList;
    private Context context;
    private FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
    private String currentUserId = fuser.getUid();
    private String FRIEND_USER_ID = "FRIEND_USER_ID";
    private String FROM_CITY = "FROM_CITY";
    private String TO_CITY = "TO_CITY";
    private String USER_PHOTO = "USER_PHOTO";
    private String NAME = "NAME";

    public ChatsAdapter(ArrayList<Chat> chatList, Context context) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_chats, viewGroup, false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder myviewHolder, final int i) {
        final Chat chats = chatList.get(i);
        Glide.with(context).load(chats.getFriendPhoto()).into(myviewHolder.photo);
        myviewHolder.fromCity.setText(chats.getFromCity());
        myviewHolder.toCity.setText(chats.getToCity());
        myviewHolder.friendName.setText(chats.getFriendName());
        if (chats.getSeenMessage().equals("display")) {
            myviewHolder.seenMessage.setVisibility(View.VISIBLE);
        } else
            myviewHolder.seenMessage.setVisibility(View.GONE);
        myviewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageFragment fragment = new MessageFragment();
                Bundle bundle = new Bundle();
                bundle.putString(FRIEND_USER_ID, chats.getFriendId());
                bundle.putString(NAME, chats.getFriendName());
                bundle.putString(FROM_CITY, chats.getFromCity());
                bundle.putString(TO_CITY, chats.getToCity());
                bundle.putString(USER_PHOTO, chats.getFriendPhoto());
                fragment.setArguments(bundle);
                ((AppCompatActivity) context).getSupportFragmentManager().
                        beginTransaction().
                        replace(R.id.main_framelayout, fragment).
                        addToBackStack(null).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView fromCity, toCity, friendName;
        RelativeLayout seenMessage;
        ConstraintLayout cardView;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.chats_photo_user);
            fromCity = itemView.findViewById(R.id.from_chats);
            toCity = itemView.findViewById(R.id.to_chats);
            cardView = itemView.findViewById(R.id.chat_cardview);
            friendName = itemView.findViewById(R.id.friend_name);
            seenMessage = itemView.findViewById(R.id.seen_message);
        }
    }
}
