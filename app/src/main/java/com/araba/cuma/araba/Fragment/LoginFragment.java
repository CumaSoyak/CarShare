package com.araba.cuma.araba.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.araba.cuma.araba.Model.Advert;
import com.araba.cuma.araba.Model.Users;
import com.araba.cuma.araba.R;
import com.araba.cuma.araba.SaveInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.araba.cuma.araba.Constant.sigIn;

public class LoginFragment extends Fragment {
    TextView kayit_buton;
    EditText email, parola;
    Button login;
    ProgressBar progressBar;
    private static String URL_lOGIN = "";
    private FirebaseAuth firebaseAuth;
    private View view;
    private FirebaseFirestore firebaseFirestore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_login, container, false);
        initView();
        firebaseAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkEditTexts()) {
                    giris();
                } else {
                    Toast.makeText(getActivity(), "E-mail veya parola doğru değil", Toast.LENGTH_LONG).show();
                }
            }
        });

        kayit_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.main_framelayout, new RegisterFragment()).addToBackStack(null).commit();
            }
        });

        return view;
    }


    private void initView() {
        email = view.findViewById(R.id.email);
        parola = view.findViewById(R.id.parola);
        progressBar = view.findViewById(R.id.progressBar);
        login = view.findViewById(R.id.login);
        kayit_buton = view.findViewById(R.id.kayit_buton);

    }

    public void giris() {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), parola.getText().toString())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            sigIn = true;
                            FirebaseUser user;
                            user = firebaseAuth.getCurrentUser();
                            String userId = user.getUid();
                            getUsersInfo(userId);


                        }

                        // ...
                    }
                }).addOnFailureListener(getActivity(), new OnFailureListener() {
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

    private void getUsersInfo(final String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Users users = document.toObject(Users.class);
                                String name = users.getName();
                                String photoUrl = users.getUserImage();
                                String password = users.getPassword();

                                SaveInfo saveInfo = new SaveInfo();
                                saveInfo.saveInfo(getActivity(), photoUrl, name, password);
                                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                                        .replace(R.id.main_framelayout, new HomeFragment()).addToBackStack(null).commit();
                            }

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


}