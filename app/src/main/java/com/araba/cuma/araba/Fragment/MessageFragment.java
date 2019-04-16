package com.araba.cuma.araba.Fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.araba.cuma.araba.R;


public class MessageFragment extends Fragment {
    TextView yazdir;
    public static int id;

    public MessageFragment() {
    }

    @SuppressLint("ValidFragment")
    public MessageFragment(int id) {
        this.id=id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        yazdir = view.findViewById(R.id.yazdir);
        String value = getArguments().getString("Key");
        Toast.makeText(getActivity(), value, Toast.LENGTH_SHORT).show();
        yazdir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchFragment fragment = new SearchFragment ();
                Bundle args = new Bundle();
                args.putString("Key", "Soyak");
                fragment.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.main_framelayout, fragment).commit();
            }
        });

        yazdir.setText(String.valueOf(id));

        return view;
    }

}
