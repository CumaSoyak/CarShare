package com.araba.cuma.araba.ProfileChooseFragment;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

 import com.araba.cuma.araba.R;
import com.araba.cuma.araba.ToolbarSetup;

import ru.whalemare.sheetmenu.SheetMenu;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment  {
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private View view, bottomSheet ;
     private BottomSheetDialog bottomSheetDialog;

    public HelpFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_help, container, false);

        bottomSheetDialog = new BottomSheetDialog(getActivity());
        View bottomSheetDialogView = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        bottomSheetDialog.setContentView(bottomSheetDialogView);



        initView();




        ToolbarSetup toolbarSetup = new ToolbarSetup();
        toolbarSetup.setUpToolbar(getActivity(), "Yardım", toolbar);
        return view;

    }

    private void initView() {
        appBarLayout = view.findViewById(R.id.app_bar_layout);
        toolbar = appBarLayout.findViewById(R.id.toolbar);



    }




}
