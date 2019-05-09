package com.araba.cuma.araba.ProfileChooseFragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.araba.cuma.araba.Model.Users;
import com.araba.cuma.araba.R;
import com.araba.cuma.araba.SaveInfo;
import com.araba.cuma.araba.ToolbarSetup;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.IOException;

import static com.araba.cuma.araba.Constant.CURRENT_NAME;
import static com.araba.cuma.araba.Constant.CURRENT_PASSWORD;
import static com.araba.cuma.araba.Constant.CURRENT_PHOTO_URL;
import static com.araba.cuma.araba.Constant.sigIn;

public class MyInfoFragment extends Fragment implements View.OnClickListener {

    ImageView profilePhoto;
    TextView photoChangeText;
    EditText nameSurnameEdit, emailEdit, passwordEdit, passwordReplayEdit;
    TextView nameSurnameText, emailText, passwordText, passwordReplayText;
    String nameSurname,email,password;
    Button edit, cancel, save;
    LinearLayout cancelOrSave;
    View view;
    Toolbar toolbar;
    LinearLayout layoutDisplay, layoutEdit;
    FirebaseUser user;
    String photoUrlnfo;
    String nameInfo;
    String passwordInfo;
    String userEmail;
    ProgressBar progressBar;
    Uri uriProfileImagePath;
    private FirebaseFirestore firebaseFirestore;

    int RESULT_OK = -1;
    private final int CHOOSE_IMAGE = 101;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    public MyInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_info, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        initView();

        ToolbarSetup toolbarSetup = new ToolbarSetup();
        toolbarSetup.setUpToolbar(getActivity(), "Bilgilerim", toolbar);
        getInfo();
        printPhotoName(photoUrlnfo, nameInfo, passwordInfo);
        nameSurname=nameSurnameEdit.getText().toString();
        email=emailEdit.getText().toString();
        password=passwordEdit.getText().toString();
        nameSurnameEdit.addTextChangedListener(textWatcher);
        emailEdit.addTextChangedListener(textWatcher);
        passwordEdit.addTextChangedListener(textWatcher);
        passwordReplayEdit.addTextChangedListener(textWatcher);
        save.setEnabled(!nameSurname.equals(nameInfo));
        save.setEnabled(!email.equals(user.getEmail()));


        return view;
    }

    private void getInfo() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        photoUrlnfo = sharedPreferences.getString(CURRENT_PHOTO_URL, "");
        nameInfo = sharedPreferences.getString(CURRENT_NAME, "");
        passwordInfo = sharedPreferences.getString(CURRENT_PASSWORD, "");


    }

    private void printPhotoName(String photoUrl, String name, String password) {
        Glide.with(getActivity()).load(photoUrl).into(profilePhoto);
        nameSurnameText.setText(name);
        nameSurnameEdit.setText(name);
        emailText.setText(user.getEmail());
        emailEdit.setText(user.getEmail());
        passwordText.setText(password);


    }

    private void initView() {
        layoutDisplay = view.findViewById(R.id.display_layout);
        layoutEdit = view.findViewById(R.id.edit_layout);
        layoutEdit.setVisibility(View.GONE);

        profilePhoto = view.findViewById(R.id.profile_photo);
        photoChangeText = view.findViewById(R.id.photo_change);
        nameSurnameEdit = view.findViewById(R.id.name_surname_edit);
        emailEdit = view.findViewById(R.id.email_edit);
        passwordEdit = view.findViewById(R.id.password_edit);


        nameSurnameText = view.findViewById(R.id.name_surname);
        emailText = view.findViewById(R.id.email);
        passwordText = view.findViewById(R.id.password);

        progressBar = view.findViewById(R.id.progressBar_myınfo);

        passwordReplayEdit = view.findViewById(R.id.paswword_replay_edit);

        edit = view.findViewById(R.id.edit);
        cancel = view.findViewById(R.id.cancel);
        save = view.findViewById(R.id.save);
        toolbar = view.findViewById(R.id.toolbar);


        cancelOrSave = view.findViewById(R.id.cancel_or_save);
        cancelOrSave.setVisibility(View.GONE);
        edit.setOnClickListener(this);
        cancel.setOnClickListener(this);
        save.setOnClickListener(this);
        photoChangeText.setOnClickListener(this);
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
                progressBar.setVisibility(View.GONE);
                 SaveInfo saveInfo = new SaveInfo();
                saveInfo.saveInfo(getActivity(), uri.toString(), nameSurname, password);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(((AppCompatActivity) getActivity()).getContentResolver(), uriProfileImagePath);
                profilePhoto.setImageBitmap(bitmap);
                if (uriProfileImagePath != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    uploadImage(user.getUid());
                }
             } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit:
                layoutEdit.setVisibility(View.VISIBLE);
                layoutDisplay.setVisibility(View.GONE);
                cancelOrSave.setVisibility(View.VISIBLE);
                edit.setVisibility(View.GONE);
                photoChangeText.setVisibility(View.VISIBLE);

                break;
            case R.id.cancel:
                layoutEdit.setVisibility(View.GONE);
                layoutDisplay.setVisibility(View.VISIBLE);
                cancelOrSave.setVisibility(View.GONE);
                edit.setVisibility(View.VISIBLE);
                photoChangeText.setVisibility(View.GONE);

                break;
            case R.id.save:
                if (isCheck()) {
                    progressBar.setVisibility(View.VISIBLE);
                    if (!emailEdit.getText().toString().equals(user.getEmail()))
                        changeEmail(emailEdit.getText().toString());
                    if (!passwordEdit.getText().toString().equals(passwordInfo) && !passwordEdit.getText().toString().isEmpty()) {
                        changePassword(passwordEdit.getText().toString());
                    }


                }
                break;
            case R.id.photo_change:
                profileImageChooser();
                break;
        }
    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            save.setEnabled(!nameSurname.equals(nameInfo));
            save.setEnabled(!email.equals(user.getEmail()));
            save.setEnabled(password.length() > 5);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private boolean isCheck() {
        String passwordReplay = passwordReplayEdit.getText().toString();

        if (nameSurname.isEmpty())
            return false;
        if (!nameSurname.contains(" ")) {
            nameSurnameEdit.setError("Ad soyad arasında boşluk olmalıdır");
            return false;
        }

        if (!email.contains("@")) {
            emailEdit.setError("Kontrol ediniz");
            return false;
        }
        if (email.isEmpty())
            return false;
        if (!password.equals(passwordReplay)) {
            passwordReplayEdit.setError("Parola aynı olmalıdır");
        }
        if (password.trim().length() < 6) {
            passwordEdit.setError("En az 6 karakter");
        }
        return true;
    }

    private void changePassword(final String newPassword) {
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), passwordInfo);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        dataReload();

                                    } else {
                                    }
                                }
                            });
                        } else {
                        }
                    }
                });
    }

    private void changeEmail(final String newEmail) {
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), passwordInfo);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    dataReload();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
    }

    private void dataReload() {
        SaveInfo saveInfo = new SaveInfo();
        saveInfo.saveInfo(getActivity(), photoUrlnfo, nameSurname, password);
        progressBar.setVisibility(View.GONE);
        cancelOrSave.setVisibility(View.GONE);
        edit.setVisibility(View.VISIBLE);
        layoutEdit.setVisibility(View.GONE);
        layoutDisplay.setVisibility(View.VISIBLE);
        nameInfo = nameSurnameEdit.getText().toString();
        passwordInfo = passwordEdit.getText().toString();
        emailText.setText(emailEdit.getText().toString());
    }
    private void  addFirebase(final String uri){
        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference newUser = firebaseFirestore.collection("users").document();
        Users users = new Users();
        users.setName(nameSurname);
        users.setPassword(password);
        users.setUserId(user.getUid());
        users.setUserImage(String.valueOf(uri));
        newUser.set(users).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    SaveInfo saveInfo = new SaveInfo();
                    saveInfo.saveInfo(getActivity(),uri, nameSurname,password);

                }
            }
        });
    }


}
