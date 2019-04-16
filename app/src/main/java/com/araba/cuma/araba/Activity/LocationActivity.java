package com.araba.cuma.araba.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.araba.cuma.araba.Adapter.LocationAdapter;
import com.araba.cuma.araba.Class.Location;
import com.araba.cuma.araba.Fragment.SearchFragment;
import com.araba.cuma.araba.R;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity {
    private LocationAdapter adapter;
    private List<Location> exampleList;
    private RecyclerView recyclerView;
    private EditText editText;
    private int inComingLocationKey;

/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        fillExampleList();
        setUpRecyclerView();
        editText = findViewById(R.id.search_search);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();

        editText.addTextChangedListener(new TextWatcher() {
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

    }

    private void filter(String text) {
        ArrayList<Location> filderlist = new ArrayList<>();
        for (Location item : exampleList) {
            if (item.getCity().toLowerCase().contains(text.toLowerCase()) ||
                    item.getArea().toLowerCase().contains(text.toLowerCase())) {
                filderlist.add(item);
            }
        }
        adapter.filterList(filderlist);
    }

    private void fillExampleList() {
        exampleList = new ArrayList<>();
        exampleList.add(new Location(R.drawable.ic_access_time_black_24dp, "One", "İstanbul"));
        exampleList.add(new Location(R.drawable.ic_car, "Two", "Niğde"));
        exampleList.add(new Location(R.drawable.ic_date_two, "Three", "Ankara"));
        exampleList.add(new Location(R.drawable.ic_access_time_black_24dp, "Four", "Elazığ"));
        exampleList.add(new Location(R.drawable.ic_access_time_black_24dp, "One", "Adana"));
        exampleList.add(new Location(R.drawable.ic_car, "Two", "Adıyaman"));
        exampleList.add(new Location(R.drawable.ic_date_two, "Three", "Mersin"));
        exampleList.add(new Location(R.drawable.ic_access_time_black_24dp, "Four", "Thirteen"));
        exampleList.add(new Location(R.drawable.ic_access_time_black_24dp, "One", "İstanbul"));
        exampleList.add(new Location(R.drawable.ic_car, "Two", "Niğde"));
        exampleList.add(new Location(R.drawable.ic_date_two, "Three", "Ankara"));
        exampleList.add(new Location(R.drawable.ic_access_time_black_24dp, "Four", "Elazığ"));
        exampleList.add(new Location(R.drawable.ic_access_time_black_24dp, "One", "Adana"));
        exampleList.add(new Location(R.drawable.ic_car, "Two", "Adıyaman"));
        exampleList.add(new Location(R.drawable.ic_date_two, "Three", "Mersin"));
        exampleList.add(new Location(R.drawable.ic_access_time_black_24dp, "Four", "Thirteen"));
    }

    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new LocationAdapter(exampleList, getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new LocationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                clickItem(position);
            }
        });
    }

    private void clickItem(int position) {
        editText.setText(exampleList.get(position).getArea() + "/" + exampleList.get(position).getCity());
        SearchFragment searchFragment = new SearchFragment();
        switch (inComingLocationKey) {
            case 1:
                break;
            case 2:
                break;

        }
        searchFragment.inEditText(exampleList.get(position).getArea() + "/" + exampleList.get(position).getCity());
        finish();

    }
*/

}
