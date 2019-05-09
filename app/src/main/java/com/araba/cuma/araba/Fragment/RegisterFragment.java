package com.araba.cuma.araba.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.araba.cuma.araba.Activity.MainActivity;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.araba.cuma.araba.Constant.sigIn;


public class RegisterFragment extends Fragment {
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
    private View view;
    private RelativeLayout relativeLayout;
    int RESULT_OK = -1;
    private final int CHOOSE_IMAGE = 101;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.activity_register, container, false);


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
                    Toast.makeText(getActivity(), "Bilgileri kontrol ediniz", Toast.LENGTH_SHORT).show();
                }
            }
        });
        loginTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.main_framelayout, new LoginFragment()).addToBackStack(null).commit();
            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileImageChooser();
            }
        });
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileImageChooser();
            }
        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileImageChooser();
            }
        });

        return view;
    }


    public void initView() {
        name = view.findViewById(R.id.adSoyad);
        email = view.findViewById(R.id.email);
        passwordRepeat = view.findViewById(R.id.parola_tekrar);
        password = view.findViewById(R.id.parola);
        conditions_check = view.findViewById(R.id.conditions_check);
        progressBar = view.findViewById(R.id.progressBar);
        profileImage = view.findViewById(R.id.profile_image);
        loginTextview = view.findViewById(R.id.giris_signup);
        registerButton = view.findViewById(R.id.login);
        conditions = view.findViewById(R.id.conditions);
        selectPhoto = view.findViewById(R.id.select_photo_text);
        relativeLayout=view.findViewById(R.id.select_photo);

    }



    public void register() {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = firebaseAuth.getCurrentUser();
                            String userId = user.getUid();
                            uploadImage(userId);
                        }
                    }
                }).addOnFailureListener(getActivity(), new OnFailureListener() {
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
        if (password.getText().toString().length() < 6) {
            password.setError("En az 6 karakter");
            return false;
        }
        if (!conditions_check.isChecked()) {
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImagePath = data.getData();
            try {
                selectPhoto.setVisibility(View.GONE);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(((AppCompatActivity) getActivity()).getContentResolver(), uriProfileImagePath);
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
                users.setPassword(passwordRepeat.getText().toString());
                users.setUserId(userId);
                users.setUserImage(String.valueOf(uri));
                newUser.set(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            SaveInfo saveInfo = new SaveInfo();
                            saveInfo.saveInfo(getActivity(), uri.toString(), name.getText().toString(),password.getText().toString());
                            sigIn=true;
                            createNotification();

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
    private void createNotification() {
        final DatabaseReference chatRefReceiverCurrent =
                FirebaseDatabase.getInstance().getReference("Notification")
                        .child(user.getUid());
        chatRefReceiverCurrent.child("notification").setValue("nondisplay").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.main_framelayout, new HomeFragment()).addToBackStack(null).commit();
            }
        });

    }

}
