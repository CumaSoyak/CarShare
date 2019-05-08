package com.araba.cuma.araba.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.araba.cuma.araba.Activity.MainActivity;
import com.araba.cuma.araba.R;

import java.util.ArrayList;
import java.util.List;

import static com.araba.cuma.araba.Activity.MainActivity.itemView;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    View view;
    private ViewPager viewPager;
    private NotificationAdapter notificationAdapter;
    private TabLayout tabLayout;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notification, container, false);
        viewPager = view.findViewById(R.id.view_pager);
        tabLayout=view.findViewById(R.id.tab_layout);
        notificationAdapter = new NotificationAdapter(getChildFragmentManager());
        setupViewPager(viewPager);
       MainActivity.badge = LayoutInflater.from(getActivity()).inflate(R.layout.notification_badge_non, itemView, true);

        return view;
    }



    private void setupViewPager(ViewPager viewPager) {
    NotificationAdapter notificationAdapter=new NotificationAdapter(getChildFragmentManager());
    notificationAdapter.addFragment(new ChatsFragment(),"Mesajlar");
    notificationAdapter.addFragment(new BidFragment(),"Teklifler");
    viewPager.setAdapter(notificationAdapter);
    tabLayout.setupWithViewPager(viewPager);
    }

    class NotificationAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);

        }

        public NotificationAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }

}
