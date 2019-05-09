package com.araba.cuma.araba.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.LayoutInflater;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.araba.cuma.araba.Activity.MainActivity;
import com.araba.cuma.araba.Adapter.MessageAdapter;
import com.araba.cuma.araba.Api.APIService;
import com.araba.cuma.araba.Model.Chat;
import com.araba.cuma.araba.Notifications.Client;
import com.araba.cuma.araba.Notifications.Data;
import com.araba.cuma.araba.Notifications.MyResponse;
import com.araba.cuma.araba.Notifications.Sender;
import com.araba.cuma.araba.Notifications.Token;
import com.araba.cuma.araba.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import static com.araba.cuma.araba.Constant.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MessageFragment extends Fragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private FirebaseUser fuser;
    private DatabaseReference reference;
    private ImageView btn_send;
    private EditText messageEdittext;
    private Toolbar toolbar;
    private APIService apiService;
    private CircleImageView profileImage;
    private TextView username;
    private String currentUserId;
    private String FRIEND_USER_ID = "FRIEND_USER_ID";
    private String ADVERT_ID = "ADVERT_ID";
    private String FROM_CITY = "FROM_CITY";
    private String TO_CITY = "TO_CITY";
    private String USER_PHOTO = "USER_PHOTO";
    private String NAME = "NAME";

    private String friendUserId = null;
    private String friendName = null;
    private String advertId = null;
    private String fromCity = null;
    private String toCity = null;
    private String friendUserPhoto = null;
    private String currentUserName = null;
    private String currentUserPhoto = null;
    private FirebaseFirestore db;
    boolean valueCheck = true;
    private List<String> messageUuidListKey;
    private List<String> messageUuidList;
    private List<Chat> messageList;
    private String msg;
    private boolean firstMessageCretad = false;
    private TextView talkOne, talkTwo, talkTheree, talkFour;

    public static MessageFragment newInstance(String param1) {
        MessageFragment fragment = new MessageFragment();
        return fragment;
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message, container, false);
        initView();
        setupToolbar();
        MainActivity.navigation.setVisibility(View.GONE);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        currentUserId = fuser.getUid();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        getNameAndPhoto();
        setupRecylerView();
        checkMessageUuid();
        print();
        updateToken(FirebaseInstanceId.getInstance().getToken());

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msg = messageEdittext.getText().toString();
                if (!msg.equals("")) {
                    if (messageUuidListKey.contains(friendUserId)) {
                        getMessageUuidSender();
                    } else {
                        createMessageUuid();
                    }
                }
                messageEdittext.setText("");
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            friendUserId = getArguments().getString(FRIEND_USER_ID);
            friendName = getArguments().getString(NAME);
            advertId = getArguments().getString(ADVERT_ID);
            fromCity = getArguments().getString(FROM_CITY);
            toCity = getArguments().getString(TO_CITY);
            friendUserPhoto = getArguments().getString(USER_PHOTO);

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (firstMessageCretad) {
            seenPositionChangeFriend("display");
            createNotification(friendUserId, "display");
            firstMessageCretad = false;
        }
        MainActivity.navigation.setVisibility(View.VISIBLE);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void createNotification(String friendUserId, String value) {
        final DatabaseReference chatRefReceiverCurrent =
                FirebaseDatabase.getInstance().getReference("Notification")
                        .child(friendUserId);
        chatRefReceiverCurrent.child("notification").setValue(value);

    }

    private void seenPositionChangeFriend(String position) {
        final DatabaseReference chatRefReceiverCurrent =
                FirebaseDatabase.getInstance().getReference("Chatlist")
                        .child(friendUserId)
                        .child(currentUserId);
        chatRefReceiverCurrent.child("seenMessage").setValue(position);
    }

    private void seenPositionChangeCurrent(String position) {
        final DatabaseReference chatRefReceiverCurrent =
                FirebaseDatabase.getInstance().getReference("Chatlist")
                        .child(currentUserId)
                        .child(friendUserId);
        chatRefReceiverCurrent.child("seenMessage").setValue(position);
    }

    private void print() {
        username.setText(friendName);
        Glide.with(getActivity()).load(friendUserPhoto).into(profileImage);
    }

    private void initView() {
        recyclerView = view.findViewById(R.id.recylerview_message);
        toolbar = view.findViewById(R.id.toolbar);
        profileImage = view.findViewById(R.id.message_profile_image);
        username = view.findViewById(R.id.username);
        btn_send = view.findViewById(R.id.btn_send);
        messageEdittext = view.findViewById(R.id.text_send);
        talkOne = view.findViewById(R.id.talk_one);
        talkTwo = view.findViewById(R.id.talk_two);
        talkTheree = view.findViewById(R.id.talk_three);
        talkFour = view.findViewById(R.id.talk_four);

        talkOne.setOnClickListener(this);
        talkTwo.setOnClickListener(this);
        talkTheree.setOnClickListener(this);
        talkFour.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.talk_one:
                messageEdittext.setText(getResources().getString(R.string.talkOne));
                break;
            case R.id.talk_two:
                messageEdittext.setText(getResources().getString(R.string.talkTwo));
                break;
            case R.id.talk_three:
                messageEdittext.setText(getResources().getString(R.string.talkThree));
                break;
            case R.id.talk_four:
                messageEdittext.setText(getResources().getString(R.string.talkFour));
                break;
        }
    }

    private void setupToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationFragment notificationFragment = new NotificationFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.main_framelayout, notificationFragment).addToBackStack(null).commit();

            }
        });
    }

    public void setupRecylerView() {
        messageList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
    }

    private void getNameAndPhoto() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        currentUserPhoto = sharedPreferences.getString(CURRENT_PHOTO_URL, "");
        currentUserName = sharedPreferences.getString(CURRENT_NAME, "");
    }

    private void checkMessageUuid() {
        messageUuidListKey = new ArrayList<>();
        messageUuidListKey.clear();
        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(currentUserId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String clubkey = data.getKey();
                    messageUuidListKey.add(clubkey);
                    if (messageUuidListKey.contains(friendUserId)) {
                        if (valueCheck) {
                            getMessageUuid();
                            seenPositionChangeCurrent("nondisplay");
                            valueCheck = false;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getMessageUuid() {
        messageUuidList = new ArrayList<>();
        messageUuidList.clear();
        reference = FirebaseDatabase.getInstance().getReference("Chatlist").
                child(currentUserId).child(friendUserId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                String messageUUid = chat.getMessageUuid();
                callMessageUuid(messageUUid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void callMessageUuid(String messageUuid) {
        readMesagges(messageUuid);
    }

    private void getMessageUuidSender() {
        messageUuidList = new ArrayList<>();
        messageUuidList.clear();
        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(currentUserId).child(friendUserId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                String messageUUid = chat.getMessageUuid();
                callMessageUuidSender(messageUUid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void callMessageUuidSender(String messageUuid) {
        sendMessage(currentUserId, friendUserId, msg, messageUuid);
    }

    private void readMesagges(String messageUuid) {
        reference = FirebaseDatabase.getInstance().getReference("Message").child(messageUuid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    messageList.add(chat);
                    messageAdapter = new MessageAdapter(getActivity(), messageList, friendUserPhoto, currentUserPhoto);
                    recyclerView.setAdapter(messageAdapter);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createMessageUuid() {
        UUID uuıd = UUID.randomUUID();
        String messageUuid = uuıd.toString();
        final DatabaseReference chatRefReceiverCurrent = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(currentUserId)
                .child(friendUserId);
        chatRefReceiverCurrent.child("seenMessage").setValue("display");
        chatRefReceiverCurrent.child("friendId").setValue(friendUserId);
        chatRefReceiverCurrent.child("friendName").setValue(friendName);
        chatRefReceiverCurrent.child("friendPhoto").setValue(friendUserPhoto);
        chatRefReceiverCurrent.child("messageUuid").setValue(messageUuid);
        chatRefReceiverCurrent.child("fromCity").setValue(fromCity);
        chatRefReceiverCurrent.child("toCity").setValue(toCity);
        final DatabaseReference chatRefReceiverFriend = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(friendUserId)
                .child(currentUserId);
        chatRefReceiverFriend.child("seenMessage").setValue("display");
        chatRefReceiverFriend.child("friendId").setValue(currentUserId);
        chatRefReceiverFriend.child("friendName").setValue(currentUserName);
        chatRefReceiverFriend.child("friendPhoto").setValue(currentUserPhoto);
        chatRefReceiverFriend.child("messageUuid").setValue(messageUuid);
        chatRefReceiverFriend.child("fromCity").setValue(fromCity);
        chatRefReceiverFriend.child("toCity").setValue(toCity);
        sendMessage(currentUserId, friendUserId, msg, messageUuid);


    }

    private void sendMessage(String sender, String receiver, String message, String messageUuid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        reference.child("Message").child(messageUuid).push().setValue(hashMap);
        sendNotifiaction(friendUserId, sender, message);
        firstMessageCretad = true;
    }


    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
    }

    private void sendNotifiaction(String receiver, final String username, final String message) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(fuser.getUid(), R.mipmap.ic_launcher, username + ": " + message, "New Message",
                            friendUserId);

                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        if (response.body().success != 1) {
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
