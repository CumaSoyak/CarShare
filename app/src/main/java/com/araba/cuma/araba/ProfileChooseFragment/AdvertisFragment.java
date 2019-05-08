package com.araba.cuma.araba.ProfileChooseFragment;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.araba.cuma.araba.R;
import com.araba.cuma.araba.ToolbarSetup;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdvertisFragment extends Fragment {

    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private View view;

    public AdvertisFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_advertis, container, false);
        appBarLayout=view.findViewById(R.id.app_bar_layout);
        toolbar=appBarLayout.findViewById(R.id.toolbar);
        ToolbarSetup toolbarSetup = new ToolbarSetup();
        toolbarSetup.setUpToolbar(getActivity(), "Reklam işbirliği ", toolbar);
        return view;
    }

}
