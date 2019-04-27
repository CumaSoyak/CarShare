package com.araba.cuma.araba.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.araba.cuma.araba.Model.Users;
import com.araba.cuma.araba.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.araba.cuma.araba.Constant.*;

public class RegisterActivity extends AppCompatActivity {
    private final int CHOOSE_IMAGE = 101;
    private final int PICK_IMAGE_REQUEST = 71;
    Button registerButton;
    TextView loginTextview, conditions;
    EditText name, email, passwordRepeat, password;
    CheckBox conditions_check;
    ProgressBar progressBar;
    CircleImageView profileImage;
    Uri uriProfileImagePath;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private TextView selectPhoto;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
         registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCheck()) {
                    register();
                } else {
                    Toast.makeText(RegisterActivity.this, "Bilgileri kontrol ediniz", Toast.LENGTH_SHORT).show();
                }
            }
        });
        loginTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ıntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(ıntent);
            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileImageChooser();
            }
        });


    }

    public void initView() {
        name = findViewById(R.id.adSoyad);
        email = findViewById(R.id.email);
        passwordRepeat = findViewById(R.id.parola_tekrar);
        password = findViewById(R.id.parola);
        conditions_check = findViewById(R.id.conditions_check);
        progressBar = findViewById(R.id.progressBar);
        profileImage = findViewById(R.id.profile_image);
        loginTextview = findViewById(R.id.giris_signup);
        registerButton = findViewById(R.id.login);
        conditions = findViewById(R.id.conditions);
        selectPhoto=findViewById(R.id.select_photo_text);

     }

    @Override
    protected void onStart() {
        super.onStart();
        if (user != null) {
            Intent ıntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(ıntent);
        }
    }

    public void register() {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = firebaseAuth.getCurrentUser();
                            String userId = user.getUid();
                            uploadImage(userId);
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                  progressBar.setVisibility(View.GONE);
            }
        });
    }

    public boolean isCheck() {
        if (uriProfileImagePath == null) {
            return false;
        }
        if (!name.getText().toString().contains(" ")) {
            name.setError("Ad soyad arasında boşluk olmalıdır");
            return false;
        }
        if (!email.getText().toString().contains("@")) {
            email.setError("E-mail doğru değildir");
            return false;
        }
        if (email.getText().toString().isEmpty()) {
            email.setError("E-mail giriniz");
            return false;
        }
        if (passwordRepeat.getText().toString().isEmpty()) {
             return false;
        }
        if (!password.getText().toString().trim().equals(passwordRepeat.getText().toString().trim())) {
            password.setError("Parola aynı olmalıdır");
            return false;
        }
        if (password.getText().toString().isEmpty()) {
             return false;
        }
        if (password.getText().toString().length()<6) {
            password.setError("En az 6 karakter");
            return false;
        }
        if (!conditions_check.isChecked()) {
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImagePath = data.getData();
            try {
                selectPhoto.setVisibility(View.GONE);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImagePath);
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void profileImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
    }

    private void uploadImage(final String userId) {
        if (uriProfileImagePath != null) {
            StorageReference ref = storageReference.child("images/" + userId);
            ref.putFile(uriProfileImagePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            getUserPhoto(userId);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);

                        }
                    });
        }

    }

    public void getUserPhoto(final String userId) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        storageRef.child("images/" + userId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(final Uri uri) {
                firebaseFirestore = FirebaseFirestore.getInstance();
                DocumentReference newUser = firebaseFirestore.collection("users").document();
                Users users = new Users();
                users.setName(name.getText().toString());
                users.setPhone(passwordRepeat.getText().toString());
                users.setUserId(userId);
                users.setUserImage(String.valueOf(uri));
                newUser.set(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            savePhotoUrl(uri,name.getText().toString());
                            Intent ıntent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(ıntent);
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progressBar.setVisibility(View.GONE);

            }
        });

    }

    private void savePhotoUrl(Uri uri,String name) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CURRENT_PHOTO_URL, uri.toString());
        editor.putString(CURRENT_NAME,name);
        editor.commit();

    }

}
