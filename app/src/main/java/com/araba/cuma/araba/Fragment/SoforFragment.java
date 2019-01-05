package com.araba.cuma.araba.Fragment;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.araba.cuma.araba.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.UUID;


public class SoforFragment extends Fragment {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String uuid_String;
    private String user_id;
    private RadioButton radioButton;
    public static String gelen_ad;
    public static String gelen_soyad;
    public static String gelen_telefon;

    private Button tarih_sec, saat_sec, onayla;
    private EditText aciklama, plaka, fiyat_sofor;
    private Spinner nereye_spinner, nereden_spinner;
    private ImageButton iki_tarih_arasi, iki_saat_arasi;
    private Spinner kisi_spinner, arac_cinsi_spinner;

    private String[] kisi_spin = {"Kişi seç", "1", "2", "3", "4", "5"};
    private String[] arac_cinsi_spin = {"Araç Cinsi", "Kamyon", "Otomobil", "Tren"};
    private String[] nereden_spin = {"Nereden", "Adana", "Adıyaman", "Afyon", "Ağrı", "Amasya", "Ankara", "Antalya", "Artvin",
            "Bitlis", "Bolu", "Denizli", "Gümüşhane", "Bayburt", "Kilis"};
    private String[] nereye_spin = {"Nereye", "Adana", "Adıyaman", "Afyon", "Ağrı", "Amasya", "Ankara", "Antalya", "Artvin",
            "Bitlis", "Bolu", "Denizli", "Gümüşhane", "Bayburt", "Kilis"};
    private ArrayAdapter<String> adapter_kisi, adapter_esya, adapter_nereden, adapter_nereye;
    private DatePickerDialog.OnDateSetListener dateSetListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sofor, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();//todo
        user = firebaseAuth.getCurrentUser();
        user_id = user.getUid();

        tarih_sec = view.findViewById(R.id.tarih_sec);
        saat_sec = view.findViewById(R.id.saat_sec);
        onayla = view.findViewById(R.id.onayla);
        aciklama = view.findViewById(R.id.aciklama);
        plaka = view.findViewById(R.id.plaka);
        fiyat_sofor = view.findViewById(R.id.fiyat_sofor);
        nereden_spinner = view.findViewById(R.id.nereden);
        nereye_spinner = view.findViewById(R.id.nereye);
        iki_tarih_arasi = view.findViewById(R.id.iki_tarih_arasi);
        iki_saat_arasi = view.findViewById(R.id.iki_saat_arasi);
        kisi_spinner = view.findViewById(R.id.kisi_spinner);
        arac_cinsi_spinner = view.findViewById(R.id.arac_cinci_spinner);
        kisi_arac_cinsi();
        tarih_sec();
        zaman_sec();
        onayla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kontrol() == true) {
                    ilan_kaydet();
                } else {
                    Toast.makeText(getActivity(), "kontrol et", Toast.LENGTH_LONG).show();

                }
            }
        });


        return view;
    }

    public void ilan_kaydet() {
        UUID uuıd = UUID.randomUUID();
        uuid_String = uuıd.toString();
        databaseReference.child("Yolcu").child(uuid_String).child("ad").setValue(gelen_ad);
        databaseReference.child("Yolcu").child(uuid_String).child("soyad").setValue(gelen_soyad);
        databaseReference.child("Yolcu").child(uuid_String).child("telefon").setValue(gelen_telefon);
        databaseReference.child("Yolcu").child(uuid_String).child("nereden").setValue(nereden_spinner.getSelectedItem().toString());
        databaseReference.child("Yolcu").child(uuid_String).child("nereye").setValue(nereye_spinner.getSelectedItem().toString());
        databaseReference.child("Yolcu").child(uuid_String).child("kisi").setValue(kisi_spinner.getSelectedItem().toString());
        databaseReference.child("Yolcu").child(uuid_String).child("arac_cinsi").setValue(arac_cinsi_spinner.getSelectedItem().toString());
        databaseReference.child("Yolcu").child(uuid_String).child("tarih").setValue(tarih_sec.getText().toString());
        databaseReference.child("Yolcu").child(uuid_String).child("saat").setValue(saat_sec.getText().toString());
        databaseReference.child("Yolcu").child(uuid_String).child("fiyat").setValue(fiyat_sofor.getText().toString());
        databaseReference.child("Yolcu").child(uuid_String).child("statu").setValue("Şoför");
        databaseReference.child("Yolcu").child(uuid_String).child("aciklama").setValue(aciklama.getText().toString());
        databaseReference.child("Yolcu").child(uuid_String).child("kullanicipuan").setValue(4);
        databaseReference.child("Yolcu").child(uuid_String).child("id").setValue(uuid_String);
        databaseReference.child("Yolcu").child(uuid_String).child("userid").setValue(user_id);

    }

    public void kisi_bilgi_al(String ad, String soyad, String telefon) {
        gelen_ad = ad;
        gelen_soyad = soyad;
        gelen_telefon = telefon;
    }

    public void kisi_arac_cinsi() {

        adapter_esya = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arac_cinsi_spin);
        adapter_kisi = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, kisi_spin);
        adapter_nereden = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, nereden_spin);
        adapter_nereye = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, nereye_spin);

        adapter_esya.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_kisi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_nereden.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_nereye.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        kisi_spinner.setAdapter(adapter_kisi);
        arac_cinsi_spinner.setAdapter(adapter_esya);
        nereye_spinner.setAdapter(adapter_nereye);
        nereden_spinner.setAdapter(adapter_nereden);
    }

    private void tarih_sec() {
        tarih_sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int gun = calendar.get(Calendar.DAY_OF_MONTH);
                int ay = calendar.get(Calendar.MONTH);
                int yil = calendar.get(Calendar.YEAR);
                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        yil, ay, gun);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = year + "-" + month + "-" + day;
                tarih_sec.setText(date);
            }
        };


    }

    private void zaman_sec() {
        saat_sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        saat_sec.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
    }

    private boolean kontrol() {
        if (nereden_spinner.getSelectedItem().toString().equals("Nereden")) {
            Toast.makeText(getActivity(), "Sehir Seçiniz", Toast.LENGTH_LONG).show();
            return false;
        } else if (nereye_spinner.getSelectedItem().toString().equals("Nereye")) {
            Toast.makeText(getActivity(), "Sehir Seçiniz", Toast.LENGTH_LONG).show();
            return false;
        } else if (arac_cinsi_spinner.getSelectedItem().toString().equals("Araç Cinsi")) {
            Toast.makeText(getActivity(), "Araç cinsi seçiniz", Toast.LENGTH_LONG).show();
            return false;
        } else if (kisi_spinner.getSelectedItem().toString().equals("Kişi seç")) {
            Toast.makeText(getActivity(), "Kişi Seçiniz", Toast.LENGTH_LONG).show();
            return false;
        } else if (tarih_sec.getText().toString().equals("Tarih seç")) {
            Toast.makeText(getActivity(), "Tarih Seçiniz", Toast.LENGTH_LONG).show();
            return false;
        } else if (saat_sec.getText().toString().equals("Saat Seç")) {
            Toast.makeText(getActivity(), "Saat Seçiniz", Toast.LENGTH_LONG).show();
            return false;
        } else if (fiyat_sofor.getText().toString().matches("")) {
            Toast.makeText(getActivity(), "Fiyat Giriniz", Toast.LENGTH_LONG).show();
            return false;
        } else if (plaka.getText().toString().matches("") && plaka.getText().toString().length() < 7) {
            Toast.makeText(getActivity(), "Plaka bilgisi yanlış veya eksik", Toast.LENGTH_LONG).show();
            return false;
        } else return true;

    }
}
