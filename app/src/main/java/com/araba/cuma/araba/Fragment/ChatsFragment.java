package com.araba.cuma.araba.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.araba.cuma.araba.Adapter.ChatsAdapter;
import com.araba.cuma.araba.Model.Chat;
import com.araba.cuma.araba.Notifications.Token;
import com.araba.cuma.araba.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;

    private ChatsAdapter chatsAdapter;
    private ArrayList<Chat> chatList;

    private FirebaseUser fuser;
    private DatabaseReference reference;
    private String currentUserId;
    private ProgressBar progressBar;
    private LinearLayout emptyChatsInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = fuser.getUid();

        recyclerView = view.findViewById(R.id.recylerview_chat_list);
        progressBar = view.findViewById(R.id.progressbar);
        emptyChatsInfo = view.findViewById(R.id.info);

        chatList = new ArrayList<>();
        chatsAdapter = new ChatsAdapter(chatList, getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        chatList();
        MessageFragment messageFragment=new MessageFragment();
        messageFragment.createNotification(currentUserId,"nondisplay");
        updateToken(FirebaseInstanceId.getInstance().getToken());


        return view;
    }

    private void chatList() {
        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(currentUserId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat mChat = snapshot.getValue(Chat.class);
                    chatList.add(mChat);
                    recyclerView.setAdapter(chatsAdapter);
                    chatsAdapter.notifyDataSetChanged();
                }
                progressBar.setVisibility(View.GONE);
                if (chatList.size() == 0) {
                    emptyChatsInfo.setVisibility(View.VISIBLE);
                }
                else
                    emptyChatsInfo.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
    }


}
