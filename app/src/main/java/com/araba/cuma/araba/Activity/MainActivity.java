package com.araba.cuma.araba.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.araba.cuma.araba.Fragment.HomeFragment;
import com.araba.cuma.araba.Fragment.AdvertFragment;
import com.araba.cuma.araba.Fragment.NotificationFragment;
import com.araba.cuma.araba.Fragment.ProfileFragment;
import com.araba.cuma.araba.Fragment.RegisterFragment;
import com.araba.cuma.araba.Fragment.SearchFragment;
import com.araba.cuma.araba.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.araba.cuma.araba.Constant.sigIn;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    private List observers;
    private FirebaseUser user;
    public static View badge;
    Fragment selectedFragment = null;
    public static BottomNavigationView navigation;
    public static BottomNavigationItemView itemView;
    public static View viewNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = FirebaseAuth.getInstance().getCurrentUser();

        observers = new ArrayList<>();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        viewNavigation = bottomNavigationMenuView.getChildAt(3);
        itemView = (BottomNavigationItemView) viewNavigation;


        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userCheck())
                {
                    navigation.setSelectedItemId(R.id.navigation_ilanver);
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_framelayout, new AdvertFragment()).commit();
                }

                else
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_framelayout, new RegisterFragment()).commit();

            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.main_framelayout, new HomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.navigation_search:
                    selectedFragment = new SearchFragment();
                    break;
                case R.id.navigation_ilanver:
                    if (userCheck())
                        selectedFragment = new AdvertFragment();
                    else
                        selectedFragment = new RegisterFragment();
                case R.id.navigation_teklifler:
                    if (userCheck())
                        selectedFragment = new NotificationFragment();
                    else
                        selectedFragment = new RegisterFragment();
                    break;
                case R.id.navigation_profil:
                    if (userCheck())
                        selectedFragment = new ProfileFragment();
                    else
                        selectedFragment = new RegisterFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_framelayout, selectedFragment).commit();
            return true;
        }
    };

    private boolean userCheck() {
        return sigIn;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (user != null) {
            getNotification();
            sigIn = true;
        } else
            sigIn = false;
    }

    private void getNotification() {
        DatabaseReference reference;
        reference = FirebaseDatabase.getInstance().getReference("Notification").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String notification = dataSnapshot.child("notification").getValue(String.class);
                displayNotification(notification);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void displayNotification(String notification) {
        if (notification.equals("display"))
            badge = LayoutInflater.from(this).inflate(R.layout.notification_badge, itemView, true);
        else {
            badge = LayoutInflater.from(this).inflate(R.layout.notification_badge_non, itemView, true);

        }
    }


}


