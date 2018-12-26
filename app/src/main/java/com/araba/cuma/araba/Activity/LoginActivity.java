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

import com.araba.cuma.araba.Api.Api;
import com.araba.cuma.araba.Class.Users;
import com.araba.cuma.araba.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    TextView kayit_buton;
    EditText email, parola;
    Button login;
    ProgressBar progressBar;
    private static String URL_lOGIN = "";

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String user_id;
    private String uuid_String;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        user = firebaseAuth.getCurrentUser();



        email = findViewById(R.id.email);
        parola = findViewById(R.id.parola);
        progressBar = findViewById(R.id.progressBar);

        login = findViewById(R.id.login);
        kayit_buton = findViewById(R.id.kayit_buton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkEditTexts() == true) {
                    //  getir_user();
                    giris();

                } else {
                    Toast.makeText(getBaseContext(), "Girişleri kontrol et", Toast.LENGTH_LONG).show();
                }
            }
        });


        kayit_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ıntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(ıntent);
            }
        });


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
                Toast.makeText(getApplicationContext(),e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);

            }
        });
    }

    public boolean checkEditTexts() {
        if (email.getText().toString().matches("") || parola.getText().toString().matches("")) {
            if (email.length() == 0)
                email.setError("Email boş geçilemez");

            if (parola.length() == 0) {
                parola.setError("Parola boş geçilemez");
            }
            return false;
        } else {
            if (email.length() == 0) {
                email.setError("Email can not null");
                return false;
            } else if (parola.length() == 0 || parola.length() < 6 || parola.length() > 20) {
                parola.setError("Password length must be between 6 - 20");
                return false;
            } else {
                return true;
            }
        }
    }

   /* public void getir_user() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        Api api = retrofit.create(Api.class);
        Call<List<Users>> call = api.getUsers();

        call.enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                List<Users> members = response.body();
                boolean entry = false;

                for (Users m : members) {
                    String e_mail = m.getEmail();
                    String password_ = m.getParola();
                    if (!e_mail.matches("") && !password_.matches("") && e_mail != null && password_ != null) {
                        if (e_mail.equals(email.getText().toString()) && password_.equals(parola.getText().toString())) {
                            entry = true;
                            break;
                        }
                    }
                }
                if (entry) {
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                } else {
                    email.setError("Check E mail");
                    parola.setError("Check Password");
                }
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                Toast.makeText(getBaseContext(), "onFailure" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }*/
}