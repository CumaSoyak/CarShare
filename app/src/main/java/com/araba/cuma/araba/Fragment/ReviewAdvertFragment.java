package com.araba.cuma.araba.Fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.araba.cuma.araba.Activity.MainActivity;
import com.araba.cuma.araba.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private String currentUserPhoto, currentUserName;


    public ReviewAdvertFragment() {
    }

    private ImageView imagePhoto, carOrMaterialImage;
    private TextView nameSurname, fromcity, toCity, date,
            time, person, carOrMaterial, status;
    @Nullable
    private TextView description;
    private ImageView bidButton;
    private EditText bidEdittext;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String currentUserId;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private ProgressBar progressBar;

    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_review_advert, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();//todo

        initView();
        printData();
        getNameAndPhoto();
        MainActivity.navigation.setVisibility(View.GONE);
        setupToolbar();
        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    currentUserId = user.getUid();
                    if ((!currentUserId.equals(mUserId) && !(bidEdittext.getText().toString().isEmpty() || bidEdittext.getText().toString().matches("")))) {
                        progressBar.setVisibility(View.VISIBLE);
                        bidPost(currentUserPhoto, currentUserName);
                    }
                } else {
                    getFragmentManager().beginTransaction().replace(R.id.main_framelayout, new RegisterFragment()).commit();

                }

            }
        });
        return view;

    }

    private void initView() {
        imagePhoto = view.findViewById(R.id.review_image);
        status = view.findViewById(R.id.status);
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
        bidButton = view.findViewById(R.id.review_bid_button);
        appBarLayout = view.findViewById(R.id.app_bar_layout);
        toolbar = appBarLayout.findViewById(R.id.toolbar);
        progressBar=view.findViewById(R.id.progressBar_bid);

    }

    private void setupToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Teklif ver");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.main_framelayout, new HomeFragment()).addToBackStack(null).commit();

            }
        });
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

    @Override
    public void onStop() {
        super.onStop();
        MainActivity.navigation.setVisibility(View.VISIBLE);
    }

    private void getNameAndPhoto() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        currentUserPhoto = sharedPreferences.getString(CURRENT_PHOTO_URL, "");
        currentUserName = sharedPreferences.getString(CURRENT_NAME, "");
    }

    private void bidPost(String photo, String name) {
        final DatabaseReference bidReference =
                FirebaseDatabase.getInstance().getReference("teklif")
                        .child(mUserId)
                        .child(currentUserId);
        bidReference.child("receiveBidUserId").setValue(mUserId);
        bidReference.child("imageUrl").setValue(photo);
        bidReference.child("advertId").setValue(mAdvertId);
        bidReference.child("giveBidUserId").setValue(currentUserId);
        bidReference.child("nameSurname").setValue(name);
        bidReference.child("fromCity").setValue(mFromCity);
        bidReference.child("toCity").setValue(mToCity);
        bidReference.child("time").setValue(mTime);
        bidReference.child("description").setValue(mDescription);
        if (mStatus.equals(getResources().getString(R.string.passenger))) {

            bidReference.child("status").setValue(getResources().getString(R.string.passenger));
            bidReference.child("material").setValue(mMaterial);
            bidReference.child("travelerPerson").setValue(mPerson);

        }
        if (mStatus.equals(getResources().getString(R.string.chauffeur))) {
            bidReference.child("status").setValue(getResources().getString(R.string.chauffeur));
            bidReference.child("carModel").setValue(mCar);
            bidReference.child("driverPerson").setValue(mPerson);

        }
        bidReference.child("price").setValue(bidEdittext.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    MessageFragment messageFragment = new MessageFragment();
                    messageFragment.createNotification(mUserId, "display");
                    progressBar.setVisibility(View.GONE);
                    Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                            "Teklifiniz iletilmiştir. İyi bir teklifti :)", Snackbar.LENGTH_LONG);
                    snackBar.show();
                    bidEdittext.setText("");
                } else {
                    Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                            "Daha sonra tekrar deneyiniz", Snackbar.LENGTH_LONG);
                }
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
        status.setText(mStatus);
        if (mStatus.equals(getResources().getString(R.string.passenger))) {
            carOrMaterial.setText(mMaterial);
            carOrMaterialImage.setImageResource(R.drawable.ic_material_review);
        } else {
            carOrMaterial.setText(mCar);
        }
        description.setText(mDescription);

    }

}
