package com.araba.cuma.araba.Fragment;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.araba.cuma.araba.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class YolcuFragment extends Fragment {

    private ImageView sofor_resim, yolcu_resim, kisi_image, esya_image;
    private TextView yolcu_tex, sofor_text;
    private Spinner kisi_spinner, esya_spinner, nereden_spinner, nereye_spinner;
    private TextView kisi_text, esya_text;
    private RadioGroup radioGroup;
    private RadioButton kisi, kisi_ve_esya, esya;
    private EditText aciklama;
    private Button tarih_sec, saat_sec, onayla;
    private ImageButton iki_tarih_arasi, iki_saat_arasi;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String user_id;
    private String uuid_String;
    private RadioButton radioButton;
    private String[] kisi_spin = {"Kişi Seçiniz", "2", "3", "4", "5"};
    private String[] esya_spin = {"Eşya Seçiniz", "Çanta", "Ev Eşyası"};
    private String[] nereden_spin = {"Nereden", "Çanta", "Ev Eşyası"};
    private String[] nereye_spin = {"Nereye", "Çanta", "Ev Eşyası"};
    private ArrayAdapter<String> adapter_kisi, adapter_esya, adapter_nereden, adapter_nereye;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    DatePicker datePicker;
    int birlikte = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_yolcu, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();//todo
        user = firebaseAuth.getCurrentUser();
        user_id = user.getUid();

        nereden_spinner = view.findViewById(R.id.nereden);
        nereye_spinner = view.findViewById(R.id.nereye);
        onayla = view.findViewById(R.id.onayla_yolcu);

        kisi_text = view.findViewById(R.id.kisi_text);
        kisi_image = view.findViewById(R.id.kisi_image);
        kisi_spinner = view.findViewById(R.id.kisi_spinner);

        esya_text = view.findViewById(R.id.esya_text);
        esya_image = view.findViewById(R.id.esya_image);
        esya_spinner = view.findViewById(R.id.esya_spinner);
        aciklama = view.findViewById(R.id.aciklama);
        tarih_sec = view.findViewById(R.id.tarih_sec);
        saat_sec = view.findViewById(R.id.saat_sec);
        iki_tarih_arasi = view.findViewById(R.id.iki_tarih_arasi);
        iki_saat_arasi = view.findViewById(R.id.iki_saat_arasi);


        radioGroup = view.findViewById(R.id.radioGroup);
        kisi = radioGroup.findViewById(R.id.radioButton_kisi);
        kisi_ve_esya = radioGroup.findViewById(R.id.radioButton_kisive_ev);
        esya = radioGroup.findViewById(R.id.radioButton_ev);

        kisi_esya();
        tarih_sec();
        zaman_sec();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {
                    case R.id.radioButton_kisi:
                        kisi_text.setVisibility(View.VISIBLE);
                        kisi_image.setVisibility(View.VISIBLE);
                        kisi_spinner.setVisibility(View.VISIBLE);
                        esya_text.setVisibility(View.INVISIBLE);
                        esya_image.setVisibility(View.INVISIBLE);
                        esya_spinner.setVisibility(View.INVISIBLE);
                        birlikte = 1;
                        break;
                    case R.id.radioButton_kisive_ev:
                        esya_text.setVisibility(View.VISIBLE);
                        esya_image.setVisibility(View.VISIBLE);
                        esya_spinner.setVisibility(View.VISIBLE);
                        kisi_text.setVisibility(View.VISIBLE);
                        kisi_image.setVisibility(View.VISIBLE);
                        kisi_spinner.setVisibility(View.VISIBLE);
                        birlikte = 2;
                        break;
                    case R.id.radioButton_ev:
                        kisi_text.setVisibility(View.INVISIBLE);
                        kisi_image.setVisibility(View.INVISIBLE);
                        kisi_spinner.setVisibility(View.INVISIBLE);
                        esya_text.setVisibility(View.VISIBLE);
                        esya_image.setVisibility(View.VISIBLE);
                        esya_spinner.setVisibility(View.VISIBLE);
                        birlikte = 3;
                        break;

                }
            }
        });

        onayla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ilan_kaydet();
            }
        });
        return view;
    }

    public void kisi_esya() {

        adapter_esya = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, esya_spin);
        adapter_kisi = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, kisi_spin);
        adapter_nereden = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, nereden_spin);
        adapter_nereye = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, nereden_spin);

        adapter_esya.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_kisi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_nereden.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_nereye.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        kisi_spinner.setAdapter(adapter_kisi);
        esya_spinner.setAdapter(adapter_esya);
        nereden_spinner.setAdapter(adapter_nereden);
        nereye_spinner.setAdapter(adapter_nereye);
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
    private void zaman_sec(){
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
                      saat_sec.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
    }

    public boolean kontrol() {
        if ((birlikte == 1) && (kisi_spinner.getSelectedItem().toString().equals("Kişi Seçiniz"))) {
            return false;
        } else if ((birlikte == 2) && (esya_spinner.getSelectedItem().toString().equals("Eşya Seçiniz"))) {
            return false;
        } else if ((birlikte == 2) && (esya_spinner.getSelectedItem().toString().equals("Kişi Seçiniz"))) {
            return false;
        } else if ((birlikte == 3) && (esya_spinner.getSelectedItem().toString().equals("Eşya Seçiniz"))) {
            return false;
        } else if (nereden_spinner.getSelectedItem().toString().equals("Nereden")) {
            return false;
        } else if (nereye_spinner.getSelectedItem().toString().equals("Nereye")) {
            return false;//Saat Seç
        } else if (tarih_sec.getText().toString().equals("Tarih seç")) {
            return false;
        } else if (saat_sec.getText().toString().equals("Saat Seç")) {
            return false;
        }
        else return true;
    }

    public void ilan_kaydet() {
        UUID uuıd = UUID.randomUUID();
        uuid_String = uuıd.toString();
        if (kontrol() == true) {
            databaseReference.child("Yolcu").child(user_id).child("nereden").setValue(nereden_spinner.getSelectedItem().toString());
            databaseReference.child("Yolcu").child(user_id).child("nereye").setValue(nereye_spinner.getSelectedItem().toString());
            databaseReference.child("Yolcu").child(user_id).child("kisi").setValue(kisi_spinner.getSelectedItem().toString());
            databaseReference.child("Yolcu").child(user_id).child("esya").setValue(esya_spinner.getSelectedItem().toString());
            databaseReference.child("Yolcu").child(user_id).child("tarih").setValue(tarih_sec.getText().toString());
            databaseReference.child("Yolcu").child(user_id).child("saat").setValue(saat_sec.getText().toString());
            databaseReference.child("Yolcu").child(user_id).child("saat").setValue(aciklama.getText().toString());

            databaseReference.child(user_id).child("nereden").setValue(nereden_spinner.getSelectedItem().toString());
            databaseReference.child(user_id).child("nereye").setValue(nereye_spinner.getSelectedItem().toString());
            databaseReference.child(user_id).child("kisi").setValue(kisi_spinner.getSelectedItem().toString());
            databaseReference.child(user_id).child("esya").setValue(esya_spinner.getSelectedItem().toString());
            databaseReference.child(user_id).child("tarih").setValue(tarih_sec.getText().toString());
            databaseReference.child(user_id).child("saat").setValue(saat_sec.getText().toString());
            databaseReference.child(user_id).child("saat").setValue(aciklama.getText().toString());
            databaseReference.child(user_id).child("durum").setValue("Yolcu");

        } else {
            Toast.makeText(getActivity(), "kontrol et", Toast.LENGTH_LONG).show();

        }

        //databaseReference.child("Yolcu").child(uuid_String).child("nereye").setValue(nereye.getText().toString());


    }

}
