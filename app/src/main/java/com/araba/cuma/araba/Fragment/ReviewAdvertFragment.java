package com.araba.cuma.araba.Fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.araba.cuma.araba.Model.Bid;
import com.araba.cuma.araba.Model.Users;
import com.araba.cuma.araba.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.UUID;

import static com.araba.cuma.araba.Constant.CURRENT_NAME;
import static com.araba.cuma.araba.Constant.CURRENT_PHOTO_URL;

public class ReviewAdvertFragment extends Fragment {
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

    private String mUserId, mStatus, mUserPhoto, mUserName, mFromCity, mToCity, mdate,
            mTime, mPerson, mCar, mMaterial, mDescription, mAdvertId;
    private String currentUserPhoto,currentUserName;


    public ReviewAdvertFragment() {
    }

    private ImageView imagePhoto, carOrMaterialImage;
    private TextView nameSurname, fromcity, toCity, date,
            time, person, carOrMaterial;
    @Nullable
    private TextView description;
    private Button bid;
    private EditText bidEdittext;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String currentUserId;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_review_advert, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();//todo
        user = firebaseAuth.getCurrentUser();
        currentUserId = user.getUid();
        initView();
        printData();
        getNameAndPhoto();
        bid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bidPost(currentUserPhoto,currentUserName);
            }
        });
        return view;

    }


    private void initView() {
        imagePhoto = view.findViewById(R.id.review_image);
        nameSurname = view.findViewById(R.id.review_name);
        fromcity = view.findViewById(R.id.review_from_city);
        toCity = view.findViewById(R.id.review_to_city);
        date = view.findViewById(R.id.review_date);
        time = view.findViewById(R.id.review_time);
        person = view.findViewById(R.id.review_personal);
        carOrMaterial = view.findViewById(R.id.review_otomobile_or_material);
        carOrMaterialImage = view.findViewById(R.id.review_material_otomobile_image);
        description = view.findViewById(R.id.review_decription);
        bidEdittext = view.findViewById(R.id.review_edit_text);
        bid = view.findViewById(R.id.review_bid_button);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStatus = getArguments().getString(STATUS);
            mAdvertId = getArguments().getString(ADVERT_ID);
            mUserId = getArguments().getString(USER_ID);
            mUserPhoto = getArguments().getString(USER_PHOTO);
            mUserName = getArguments().getString(USERNAME);
            mFromCity = getArguments().getString(FROM_CITY);
            mToCity = getArguments().getString(TO_CITY);
            mdate = getArguments().getString(DATE);
            mTime = getArguments().getString(TIME);
            mPerson = getArguments().getString(PERSON);
            mCar = getArguments().getString(CAR);
            mMaterial = getArguments().getString(MATERIAL);
            mDescription = getArguments().getString(DESCRIPTION);

        }
    }
    private void getNameAndPhoto() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        currentUserPhoto = sharedPreferences.getString(CURRENT_PHOTO_URL, "");
        currentUserName = sharedPreferences.getString(CURRENT_NAME, "");
    }


    private void bidPost(String photo,String name) {
        UUID uuıd = UUID.randomUUID();
        String uuıdString = uuıd.toString();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference newAdvert = firebaseFirestore.collection("teklif").document(mAdvertId);
        Bid bid = new Bid();
        bid.setAdvertId(mAdvertId);
        bid.setGiveBidUserId(currentUserId);//teklif veren id
        bid.setReceiveBidUserId(mUserId);//teklif alan id
        bid.setImageUrl(photo);
        bid.setNameSurname(name);
        bid.setFromCity(mFromCity);
        bid.setToCity(mToCity);
        if (mStatus.equals(getResources().getString(R.string.passenger))) {
            bid.setStatus(getResources().getString(R.string.passenger));
            bid.setMaterial(mMaterial);
            bid.setTravelerPerson(mPerson);
        }
        if (mStatus.equals(getResources().getString(R.string.chauffeur))) {
            bid.setStatus(getResources().getString(R.string.chauffeur));
            bid.setCarModel(mCar);
            bid.setDriverPerson(mPerson);
        }
        bid.setDate(mdate);
        bid.setTime(mTime);
        bid.setDescription(mDescription);
        bid.setPrice(bidEdittext.getText().toString());
        newAdvert.set(bid).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), "Paylaşılmıştır", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void printData() {
        Glide.with(getActivity()).load(mUserPhoto).into(imagePhoto);
        nameSurname.setText(mUserName);
        fromcity.setText(mFromCity);
        toCity.setText(mToCity);
        date.setText(mdate);
        time.setText(mTime);
        person.setText(mPerson);
        if (mStatus.equals(getResources().getString(R.string.passenger))) {
            carOrMaterial.setText(mMaterial);
            carOrMaterialImage.setImageResource(R.drawable.ic_material);
        } else {
            carOrMaterial.setText(mCar);
        }
        description.setText(mDescription);

    }

}
