package com.araba.cuma.araba.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.araba.cuma.araba.GetInfo;
import com.araba.cuma.araba.ProfileChooseFragment.AdvertisFragment;
import com.araba.cuma.araba.ProfileChooseFragment.ConditionFragment;
import com.araba.cuma.araba.ProfileChooseFragment.HelpFragment;
import com.araba.cuma.araba.ProfileChooseFragment.MyAdvertFragment;
import com.araba.cuma.araba.ProfileChooseFragment.MyInfoFragment;
import com.araba.cuma.araba.ProfileChooseFragment.SavedAdvertFragment;
import com.araba.cuma.araba.R;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.araba.cuma.araba.Constant.sigIn;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {


    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private ImageView signOutButton;
    private TextView userName;
    private ImageView userPhoto;
    private View view;
    private LinearLayout myAdvertLayout, savedAdvertLayout, my_info_layout, help_layout,
            advertisment_layout, condition_layout;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);


        signOutButton = view.findViewById(R.id.sign_out);
        initView();
        GetInfo getInfo = new GetInfo();
        printPhotoName(getInfo.getInfoPhoto(getActivity()), getInfo.getInfoName(getActivity()));

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sigIn = false;
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.main_framelayout, new HomeFragment()).addToBackStack(null).commit();

            }
        });
        return view;
    }

    private void initView() {
        userName = view.findViewById(R.id.profile_user_name);
        userPhoto = view.findViewById(R.id.profile_photo);
        myAdvertLayout = view.findViewById(R.id.my_advert_layout);
        savedAdvertLayout = view.findViewById(R.id.saved_advert_layout);
        my_info_layout = view.findViewById(R.id.my_info_layout);
        help_layout = view.findViewById(R.id.help_layout);
        advertisment_layout = view.findViewById(R.id.advertisment_layout);
        condition_layout = view.findViewById(R.id.condition_layout);
        my_info_layout.setOnClickListener(this);
        myAdvertLayout.setOnClickListener(this);
        savedAdvertLayout.setOnClickListener(this);
        help_layout.setOnClickListener(this);
        advertisment_layout.setOnClickListener(this);
        condition_layout.setOnClickListener(this);


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
                fragment = new SavedAdvertFragment();
                break;
            case R.id.my_info_layout:
                fragment = new MyInfoFragment();
                break;
            case R.id.help_layout:
                fragment = new HelpFragment();
                break;
            case R.id.advertisment_layout:
                fragment = new AdvertisFragment();
                break;
            case R.id.condition_layout:
                fragment = new ConditionFragment();
                break;
        }
        getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .replace(R.id.main_framelayout, fragment).addToBackStack(null).commit();
    }

}
