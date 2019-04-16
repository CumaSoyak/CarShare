package com.araba.cuma.araba.Fragment;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class DriverFragment extends Fragment {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String uuidString;
    private String userId;
    private RadioButton radioButton;
    private Button selectDate, selectTime, approve;
    private EditText description, plate, priceDvider;
    private Spinner nereye_spinner, nereden_spinner;
    private ImageButton iki_tarih_arasi, iki_saat_arasi;
    private Spinner kisi_spinner, arac_cinsi_spinner;

    private String[] kisi_spin = {"Kişi seç", "1", "2", "3", "4", "5"};
    private String[] arac_cinsi_spin = {"Araç Cinsi", "Otomobil", "Minibüs", "Otobüs", "Kamyonet", "Motosiklet"};
    private String[] nereden_spin = {"NEREDEN",
            "ADANA",
            "ADIYAMAN",
            "AFYONKARAHİSAR",
            "AĞRI"};
    private String[] nereye_spin = {"NEREYE",
            "ADANA",
            "ADIYAMAN"};
    private ArrayAdapter<String> adapter_kisi, adapter_esya, adapter_nereden, adapter_nereye;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    View view;
    private String name;
    private String surname;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_driver, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();//todo
        user = firebaseAuth.getCurrentUser();
        userId = user.getUid();
        initView();
        kisi_arac_cinsi();
        tarih_sec();
        zaman_sec();
        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kontrol()) {
                    ilan_kaydet();
                    SystemClock.sleep(3000);
                } else {
                    Toast.makeText(getActivity(), "chechkEmpty et", Toast.LENGTH_LONG).show();

                }
            }
        });


        return view;
    }

    private void initView() {
        selectDate = view.findViewById(R.id.date_driver);
        selectTime = view.findViewById(R.id.hour_driver);
        approve = view.findViewById(R.id.ok_driver);
        description = view.findViewById(R.id.description_driver);
        plate = view.findViewById(R.id.plate_driver);
        priceDvider = view.findViewById(R.id.price_driver);
        nereden_spinner = view.findViewById(R.id.from_driver);
        nereye_spinner = view.findViewById(R.id.to__driver);
        iki_tarih_arasi = view.findViewById(R.id.two_date_between_date_driver);
        iki_saat_arasi = view.findViewById(R.id.two_between_hour__driver);
        kisi_spinner = view.findViewById(R.id.person_driver);
        arac_cinsi_spinner = view.findViewById(R.id.car_driver);
    }

    public void ilan_kaydet() {
        /*
        *
         DocumentReference newUser = firebaseFirestore.collection("Yolcu").document("cuma").
                                    collection(uuidString).document("ilan");
       */
        UUID uuıd = UUID.randomUUID();
        uuidString = uuıd.toString();
        Map <String ,Object> driverAdvert=new HashMap<>();
        driverAdvert.put(name,"");
        driverAdvert.put(surname,"");
        databaseReference.child("Yolcu").child(uuidString).child("nereden").setValue(nereden_spinner.getSelectedItem().toString());
        databaseReference.child("Yolcu").child(uuidString).child("nereye").setValue(nereye_spinner.getSelectedItem().toString());
        databaseReference.child("Yolcu").child(uuidString).child("kisi").setValue(kisi_spinner.getSelectedItem().toString());
        databaseReference.child("Yolcu").child(uuidString).child("arac_cinsi").setValue(arac_cinsi_spinner.getSelectedItem().toString());
        databaseReference.child("Yolcu").child(uuidString).child("tarih").setValue(selectDate.getText().toString());
        databaseReference.child("Yolcu").child(uuidString).child("saat").setValue(selectTime.getText().toString());
        databaseReference.child("Yolcu").child(uuidString).child("fiyat").setValue(priceDvider.getText().toString());
        databaseReference.child("Yolcu").child(uuidString).child("plate").setValue(plate.getText().toString());
        databaseReference.child("Yolcu").child(uuidString).child("statu").setValue("Şoför");
        databaseReference.child("Yolcu").child(uuidString).child("description").setValue(description.getText().toString());
        databaseReference.child("Yolcu").child(uuidString).child("kullanicipuan").setValue(4);
        databaseReference.child("Yolcu").child(uuidString).child("id").setValue(uuidString);
        databaseReference.child("Yolcu").child(uuidString).child("userid").setValue(userId);

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
        selectDate.setOnClickListener(new View.OnClickListener() {
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
                selectDate.setText(date);
            }
        };


    }

    private void zaman_sec() {
        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        selectTime.setText(selectedHour + ":" + selectedMinute);
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
        } else if (selectDate.getText().toString().equals("Tarih seç")) {
            Toast.makeText(getActivity(), "Tarih Seçiniz", Toast.LENGTH_LONG).show();
            return false;
        } else if (selectTime.getText().toString().equals("Saat Seç")) {
            Toast.makeText(getActivity(), "Saat Seçiniz", Toast.LENGTH_LONG).show();
            return false;
        } else if (priceDvider.getText().toString().matches("")) {
            Toast.makeText(getActivity(), "Fiyat Giriniz", Toast.LENGTH_LONG).show();
            return false;
        } else if (plate.getText().toString().matches("") && plate.getText().toString().length() < 7) {
            Toast.makeText(getActivity(), "Plaka bilgisi yanlış veya eksik", Toast.LENGTH_LONG).show();
            return false;
        } else if (nereden_spinner.getSelectedItem().toString().equals(nereye_spinner.getSelectedItem().toString())) {
            Toast.makeText(getActivity(), "Aynı şehri seçtiniz", Toast.LENGTH_LONG).show();
            return false;
        } else return true;

    }
}
