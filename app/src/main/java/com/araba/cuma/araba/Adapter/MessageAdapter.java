package com.araba.cuma.araba.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.araba.cuma.araba.Model.Chat;
import com.araba.cuma.araba.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Chat> messageList;
    private String friendUserPhoto,currentUserPhoto;
    FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();

    public MessageAdapter(Context context, List<Chat> messageList, String friendUserPhoto,String currentUserPhoto) {
        this.mContext = context;
        this.messageList = messageList;
        this.friendUserPhoto = friendUserPhoto;
        this.currentUserPhoto=currentUserPhoto;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.right_item_layout, viewGroup, false);
            return new MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.left_item_layout, viewGroup, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        Chat chat = messageList.get(position);
        if (messageList.get(position).getSender().equals(fuser.getUid())) {
            Glide.with(mContext).load(currentUserPhoto).into(myViewHolder.profile_image);

        } else {
            Glide.with(mContext).load(friendUserPhoto).into(myViewHolder.profile_image);

        }
        myViewHolder.show_message.setText(chat.getMessage());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;
        public ImageView profile_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image_message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getSender().equals(fuser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }


    }
}
