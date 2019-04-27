package com.araba.cuma.araba.Fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.araba.cuma.araba.Activity.LoginActivity;
import com.araba.cuma.araba.ProfileChooseFragment.MyAdvertFragment;
import com.araba.cuma.araba.ProfileChooseFragment.SavedAdvertFragment;
import com.araba.cuma.araba.R;

import static com.araba.cuma.araba.Constant.*;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {


    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String userEmail, userId;
    private Button signOutButton;
    private TextView userName;
    private ImageView userPhoto;
    private View view;
    private LinearLayout myAdvertLayout,savedAdvertLayout;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userEmail = user.getEmail();
        userId = user.getUid();
        signOutButton = view.findViewById(R.id.sign_out);
        signOutButton.setText(user.getEmail());
        initView();
        getCurrentPhotoUrl();

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Intent ıntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(ıntent);

            }
        });
        return view;
    }

    private void initView() {
        userName = view.findViewById(R.id.profile_user_name);
        userPhoto = view.findViewById(R.id.profile_photo);
        myAdvertLayout = view.findViewById(R.id.my_advert_layout);
        savedAdvertLayout=view.findViewById(R.id.saved_advert_layout);
        savedAdvertLayout.setOnClickListener(this);
        myAdvertLayout.setOnClickListener(this);
    }

    private void getCurrentPhotoUrl() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String photoUrl = sharedPreferences.getString(CURRENT_PHOTO_URL, "");
        String name = sharedPreferences.getString(CURRENT_NAME, "Loading..");
        printPhotoName(photoUrl, name);
    }

    private void printPhotoName(String photoUrl, String name) {
        Glide.with(getActivity()).load(photoUrl).into(userPhoto);
        userName.setText(name);
    }

    @Override
    public void onClick(View view) {
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.my_advert_layout:
                fragment = new MyAdvertFragment();
                break;
            case R.id.saved_advert_layout:
                fragment=new SavedAdvertFragment();
         }
        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .replace(R.id.main_framelayout, fragment).addToBackStack(null).commit();
    }
}
