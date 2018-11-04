package com.araba.cuma.araba.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.araba.cuma.araba.R;

public class SignUpActivity extends AppCompatActivity {
    Button kayit_signup;
    TextView giris_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        giris_signup = findViewById(R.id.giris_signup);
        kayit_signup = findViewById(R.id.kayit_signup);
        kayit_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ıntent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(ıntent);
            }
        });

        giris_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ıntent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(ıntent);
            }
        });
    }
}
