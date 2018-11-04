package com.araba.cuma.araba.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.araba.cuma.araba.R;

public class LoginActivity extends AppCompatActivity {
    TextView kayit_buton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        kayit_buton = findViewById(R.id.kayit_buton);
        kayit_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ıntent=new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(ıntent);
            }
        });
    }
}
