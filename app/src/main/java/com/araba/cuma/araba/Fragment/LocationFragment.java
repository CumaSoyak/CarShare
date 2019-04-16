package com.araba.cuma.araba.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.araba.cuma.araba.Adapter.LocationAdapter;
import com.araba.cuma.araba.Class.Location;
import com.araba.cuma.araba.R;

import java.util.ArrayList;
import java.util.List;


public class LocationFragment extends Fragment implements LocationAdapter.OnItemClickListener {

    public static final String SEARCH_FROM_CITY = "SEARCH_FROM_CITY";
    public static final String SEARCH_TO_CITY = "SEARCH_TO_CITY";
    public static final String TRAVELER_FROM_CITY="TRAVELER_FROM_CITY";
    public static final String TRAVELER_TO_CITY="TRAVELER_TO_CITY";
    public static final String DRIVER_FROM_CITY="TRAVELER_FROM_CITY";
    public static final String DRIVER_TO_CITY="TRAVELER_TO_CITY";
    public static final String CITY = "CITY";
    private String cityValue;


    private LocationAdapter locationAdapter;
    private List<Location> locationList;
    private RecyclerView recyclerView;
    private EditText searchEditText;
    private View view;


    public static LocationFragment newInstance(String param1, String param2) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putString(CITY, param1);
        args.putString(CITY, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cityValue = getArguments().getString(CITY);
            Toast.makeText(getActivity(), cityValue, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_location, container, false);
        fillLocationList();
        setUpRecyclerView();
        searchEditText = view.findViewById(R.id.location_edittext);
        searchEditText.setFocusableInTouchMode(true);
        searchEditText.requestFocus();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
        return view;
    }

    private void setUpRecyclerView() {
        recyclerView = view.findViewById(R.id.location_recyclerview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        locationAdapter = new LocationAdapter(locationList, getActivity(),this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(locationAdapter);

    }

    private void fillLocationList() {
        locationList = new ArrayList<>();
        locationList.add(new Location(R.drawable.ic_access_time_black_24dp, "1", "on"));
        locationList.add(new Location(R.drawable.ic_access_time_black_24dp, "2", "yırmı"));
        locationList.add(new Location(R.drawable.ic_access_time_black_24dp, "3", "otuz"));
        locationList.add(new Location(R.drawable.ic_access_time_black_24dp, "4", "kırk"));
        locationList.add(new Location(R.drawable.ic_access_time_black_24dp, "5", "elli"));
        locationList.add(new Location(R.drawable.ic_access_time_black_24dp, "6", "altmış"));
        locationList.add(new Location(R.drawable.ic_access_time_black_24dp, "7", "yetmiş"));
        locationList.add(new Location(R.drawable.ic_access_time_black_24dp, "8", "seksen"));


    }

    private void filter(String text) {
        ArrayList<Location> filderList = new ArrayList<>();
        for (Location location : locationList) {
            if (location.getCity().toLowerCase().contains(text.toLowerCase()) ||
                    location.getArea().toLowerCase().contains(text.toLowerCase())) {
                filderList.add(location);

            }
        }
        locationAdapter.filterList(filderList);
    }

    @Override
    public void onItemClick(Location position) {
        Fragment selectedFragment=null;
        if (getArguments().getString(CITY).contains("SEARCH"))
        {
            selectedFragment = new SearchFragment();
        }
        if (getArguments().getString(CITY).contains("TRAVELER"))
        {
            selectedFragment = new TravelerFragment();
        }
        if (getArguments().getString(CITY).contains("SEARCH"))
        {
            selectedFragment = new DriverFragment();
        }
        Bundle args = new Bundle();
        String cityInfo = position.getArea() + "/" + position.getCity();
        searchEditText.setText(cityInfo);
        if (cityValue.equals(SEARCH_FROM_CITY)) {
            args.putString(SEARCH_FROM_CITY, cityInfo);
        }
        else if (cityValue.equals(SEARCH_TO_CITY)) {

            args.putString(SEARCH_TO_CITY, cityInfo);

        }
        selectedFragment.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.main_framelayout, selectedFragment).commit();

    }
}
