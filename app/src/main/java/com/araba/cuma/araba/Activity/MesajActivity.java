package com.araba.cuma.araba.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.araba.cuma.araba.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MesajActivity extends AppCompatActivity {
    static int productID;

    TextView statu;
    TextView ad;
    TextView soyad;
    TextView nereden;
    TextView nereye;
    TextView saat;
    TextView tarih;
    TextView telefon;
    TextView esya;
    TextView kisi;
    TextView aciklama;
    TextView fiyat;
    EditText teklif;
    Button button;

    int puan;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String user_id;
    private String ilan_id, gelen_user_id, gelen_nereden, gelen_nereye, gelen_statu;

    public static String gelen_ad;
    public static String gelen_soyad;
    public static String gelen_telefon;
    private TextView esya_bilgi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        user = firebaseAuth.getCurrentUser();
        user_id = user.getUid();
/*
        statu = findViewById(R.id.statu);
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        nereden = findViewById(R.id.from_driver);
        nereye = findViewById(R.id.to__driver);
        saat = findViewById(R.id.saat);
        tarih = findViewById(R.id.tarih);
        phone = findViewById(R.id.phone);
        esya = findViewById(R.id.esya);
        kisi = findViewById(R.id.kisi);
        aciklama = findViewById(R.id.aciklama_mesaj);
        teklif = findViewById(R.id.editmesaj);
        fiyat = findViewById(R.id.fiyat);
        esya_bilgi = findViewById(R.id.textesya);
*/
        button = findViewById(R.id.button);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            // ilan_id = (String)bundle.get("ilan_id");
            ilan_id = getIntent().getStringExtra("ilan_id");
            gelen_user_id = (String) bundle.get("user_id");
            gelen_nereden = (String) bundle.get("nereden");
            gelen_nereye = (String) bundle.get("nereye");
            gelen_statu = (String) bundle.get("statu");
            Log.i("ilanid", ":" + ilan_id);
            Log.i("gelenuserid", ":" + gelen_user_id);
            Log.i("gelenereden", ":" + gelen_nereden);
            Log.i("gelennereye", ":" + gelen_nereye);
        }
        //Firebase_get_yolcu();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!teklif.getText().toString().matches("")) {

                    databaseReference.child("Teklif").child(gelen_user_id).child(ilan_id).child("teklif").setValue(teklif.getText().toString());
                    databaseReference.child("Teklif").child(gelen_user_id).child(ilan_id).child("name").setValue(gelen_ad);
                    databaseReference.child("Teklif").child(gelen_user_id).child(ilan_id).child("surname").setValue(gelen_soyad);
                    databaseReference.child("Teklif").child(gelen_user_id).child(ilan_id).child("nereden").setValue(gelen_nereden);
                    databaseReference.child("Teklif").child(gelen_user_id).child(ilan_id).child("nereye").setValue(gelen_nereye);
                    if (gelen_statu.equals("Yolcu")) {
                        databaseReference.child("Teklif").child(gelen_user_id).child(ilan_id).child("statu").setValue("Şoför");
                    } else {
                        databaseReference.child("Teklif").child(gelen_user_id).child(ilan_id).child("statu").setValue("Yolcu");
                    }
                    teklif.getText().clear();
                    button.setEnabled(false);

                } else {
                    Toast.makeText(getApplicationContext(), "Boş fiyat", Toast.LENGTH_LONG).show();
                }


            }
        });

    }

    public void kisi_bilgi_al(String ad, String soyad, String telefon) {
        gelen_ad = ad;
        gelen_soyad = soyad;
        gelen_telefon = telefon;
        Log.i("denemead", ":" + ad);

    }

    public void Firebase_get_yolcu() {
        databaseReference.child("Yolcu").child(ilan_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String g_ad = dataSnapshot.child("name").getValue(String.class);
                String g_soyad = dataSnapshot.child("surname").getValue(String.class);
                String g_nereden = dataSnapshot.child("nereden").getValue(String.class);
                String g_nereye = dataSnapshot.child("nereye").getValue(String.class);
                String g_saat = dataSnapshot.child("saat").getValue(String.class);
                String g_tarih = dataSnapshot.child("tarih").getValue(String.class);
                String g_telefon = dataSnapshot.child("phone").getValue(String.class);
                String g_esya = dataSnapshot.child("esya").getValue(String.class);
                String g_aracinsi = dataSnapshot.child("arac_cinsi").getValue(String.class);
                String g_kisi = dataSnapshot.child("kisi").getValue(String.class);
                String g_statu = dataSnapshot.child("statu").getValue(String.class);
                String g_aciklama = dataSnapshot.child("aciklama").getValue(String.class);
                String g_fiyat = dataSnapshot.child("fiyat").getValue(String.class);
                String g_plaka = dataSnapshot.child("plaka").getValue(String.class);
                if (!g_aciklama.matches("")) {
                    aciklama.setText(g_aciklama);
                } else if (gelen_statu.equals("Yolcu")) {
                    esya.setText(g_esya);
                } else if (gelen_statu.equals("Şoför")) {
                    esya_bilgi.setText("Arac cinsi");
                    esya.setText(g_aracinsi);
                    aciklama.setText(g_plaka);
                }
                statu.setText(g_statu);
                ad.setText(g_ad);
                soyad.setText(g_soyad);
                nereden.setText(g_nereden);
                nereye.setText(g_nereye);
                saat.setText(g_saat);
                tarih.setText(g_tarih);
                telefon.setText(g_telefon);
                kisi.setText(g_kisi);
                fiyat.setText(g_fiyat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
