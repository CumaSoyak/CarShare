package com.araba.cuma.araba.Activity;

import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.araba.cuma.araba.Api.Api;
import com.araba.cuma.araba.Api.RetrofitClient;
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

import java.io.IOException;
import java.util.List;

import javax.xml.transform.Result;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {
    Button kayit_signup;
    TextView giris_signup;
    EditText ad, soyad, email, telefon, parola;
    CheckBox hatirla_beni;
    ProgressBar progressBar;
    private static String URL_REGIST = "http://192.168.50.2/Araba/register.php";

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        user = firebaseAuth.getCurrentUser();

        ad = findViewById(R.id.ad);
        soyad = findViewById(R.id.soyad);
        email = findViewById(R.id.email);
        telefon = findViewById(R.id.telefon);
        parola = findViewById(R.id.parola);
        hatirla_beni = findViewById(R.id.hatirla_beni);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        giris_signup = findViewById(R.id.giris_signup);
        kayit_signup = findViewById(R.id.login);
        kayit_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /////////////
               register();
                ////////////
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
    @Override
    protected void onStart() {
        super.onStart();
        if (user != null) {
            Intent ıntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(ıntent);
        }
    }
    public void register(){


        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), parola.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            //   Toast.makeText(getApplicationContext(), "Başarılı", Toast.LENGTH_LONG).show();

                            Intent ıntent = new Intent(getApplicationContext(), MainActivity.class);
                            ıntent.putExtra("ad",email.getText().toString());
                            ıntent.putExtra("soyad",email.getText().toString());
                            ıntent.putExtra("telefon",email.getText().toString());
                             startActivity(ıntent);

                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              //  Toast.makeText(getApplicationContext(), getResources().getString(R.string.bilgileri_kontrol), Toast.LENGTH_LONG).show();
                Log.i("Hata", ":" + e.getLocalizedMessage());
                progressBar.setVisibility(View.GONE);
            }
        });






    }

   /* public void getMembers() {
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
                    if (!e_mail.matches("") && e_mail != null) {
                        if (e_mail.equals(email.getText().toString())) {
                            entry = true;
                            break;
                        }
                    }
                }
                if (entry) {
                    Toast.makeText(getBaseContext(), "This e mail allready registered.", Toast.LENGTH_LONG).show();
                } else {
                    //   Toast.makeText(getBaseContext(), "Fuck your self", Toast.LENGTH_LONG).show();

                    postRequestUser(ad.getText().toString(), soyad.getText().toString(), email.getText().toString(), telefon.getText().toString(), parola.getText().toString());
                }
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {
                Toast.makeText(getBaseContext(), "onFailure" + t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }*/

   /* public void postRequestUser(String ad, String soyad, String email, String telefon, String parola) {

        Toast.makeText(getBaseContext(), "Fuck", Toast.LENGTH_LONG).show();
        Call<Users> call = RetrofitClient
                .getmInstance()
                .getApi()
                .postUsers(ad, soyad, email, telefon, parola);

        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                try {

                    Toast.makeText(getBaseContext(), "Registration Successful", Toast.LENGTH_LONG).show();
                    Intent ıntent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(ıntent);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "onResponse da patladı", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                Toast.makeText(getBaseContext(), t.getMessage() + " onFailure", Toast.LENGTH_LONG).show();
            }
        });
    }*/

}
