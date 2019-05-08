package com.araba.cuma.araba.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.araba.cuma.araba.Adapter.LocationAdapter;
import com.araba.cuma.araba.Model.Location;
import com.araba.cuma.araba.R;
import com.araba.cuma.araba.SetupRecylerview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.araba.cuma.araba.Constant.*;


public class LocationFragment extends Fragment implements LocationAdapter.OnItemClickListener {


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_location, container, false);
        setUpRecyclerView();
        searchEditText = view.findViewById(R.id.location_edittext);
        searchEditText.setFocusableInTouchMode(true);
        searchEditText.requestFocus();
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                if (count > 0 && (charSequence.charAt(0) != '1')) {
                    filter(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });


        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cityValue = getArguments().getString(CITY);
        }
    }


    @Override
    public void onItemClick(Location position) {
        Fragment selectedFragment = null;
        cityInfo = position.getCity() + "/" + position.getArea();
        if (getArguments().getString(CITY).contains("SEARCH")) {
            selectedFragment = new SearchFragment();
        }
        if (getArguments().getString(CITY).contains("ADVERT")) {
            selectedFragment = new AdvertFragment();
        }
        if (getArguments().getString(CITY).contains("FROM"))
            FROM = cityInfo;
        if (getArguments().getString(CITY).contains("TO"))
            TO = cityInfo;
        searchEditText.setText(cityInfo);
        getFragmentManager().beginTransaction().replace(R.id.main_framelayout, selectedFragment).addToBackStack(null).commit();

    }


    private void setUpRecyclerView() {
        recyclerView = view.findViewById(R.id.location_recyclerview);
        SetupRecylerview setupRecylerview = new SetupRecylerview();
        setupRecylerview.setupRecylerView(getActivity(), recyclerView);
        retrofit();


    }

    private void retrofit() {
        locationList = new ArrayList<>();
        locationAdapter = new LocationAdapter(locationList, getActivity(), this);
        String json;
        int a = 0;
        try {
            InputStream ınputStream = ((AppCompatActivity) getActivity()).getAssets().open("city.json");
            int size = ınputStream.available();
            byte[] buffer = new byte[size];
            ınputStream.read(buffer);
            ınputStream.close();

            json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONArray ilceler = (JSONArray) jsonObject.get("ilceleri");
                for (int j = 0; j < ilceler.length(); j++) {
                    System.out.println(ilceler.get(j));
                    locationList.add(new Location(R.drawable.ic_galata_tower, jsonObject.getString("il"), ilceler.get(j).toString()));
                    recyclerView.setAdapter(locationAdapter);
                }


            }


        } catch (IOException e) {

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void filter(String text) {
        ArrayList<Location> filderList = new ArrayList<>();
        for (Location location : locationList) {
            if (location.getCity().toLowerCase().contains(text.toLowerCase()) ||
                    location.getCity().toLowerCase().equals(text.toLowerCase())||
                    location.getArea().toLowerCase().contains(text.toLowerCase())) {
                filderList.add(location);

            }
        }
        locationAdapter.filterList(filderList);
    }
}
