package com.araba.cuma.araba.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.araba.cuma.araba.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    TextView kayit_buton;
    EditText email, parola;
    Button login;
    ProgressBar progressBar;
    private static String URL_lOGIN = "";
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        firebaseAuth = FirebaseAuth.getInstance();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkEditTexts()) {
                    giris();
                } else {
                    Toast.makeText(getBaseContext(), "E-mail veya parola doğru değil", Toast.LENGTH_LONG).show();
                }
            }
        });

        kayit_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ıntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(ıntent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initView() {
        email = findViewById(R.id.email);
        parola = findViewById(R.id.parola);
        progressBar = findViewById(R.id.progressBar);
        login = findViewById(R.id.login);
        kayit_buton = findViewById(R.id.kayit_buton);

    }
    public void giris() {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), parola.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            Intent ıntent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(ıntent);

                        }

                        // ...
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Login", "Error" + e.getLocalizedMessage());
                 progressBar.setVisibility(View.GONE);

            }
        });
    }

    public boolean checkEditTexts() {
        if (email.getText().toString().isEmpty() || parola.getText().toString().isEmpty()) {
            email.setError("Kontrol ediniz");
            parola.setError("Kontrol ediniz");
            return false;
        } else {
            return true;
        }
    }


}