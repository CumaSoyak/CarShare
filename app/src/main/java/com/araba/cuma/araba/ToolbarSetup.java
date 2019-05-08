package com.araba.cuma.araba;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.support.v7.widget.Toolbar;

import com.araba.cuma.araba.Fragment.ProfileFragment;

public class ToolbarSetup {
    public void setUpToolbar(final Context context,String title,Toolbar toolbar) {
        ((AppCompatActivity) context).setSupportActionBar(toolbar);
        ((AppCompatActivity) context).getSupportActionBar().setTitle(title);
        ((AppCompatActivity) context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.main_framelayout, new ProfileFragment())
                        .addToBackStack(null).commit();

            }
        });
    }
}
