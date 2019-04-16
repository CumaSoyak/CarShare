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

import java.util.Calendar;
import java.util.UUID;

import static com.araba.cuma.araba.Fragment.LocationFragment.CITY;
import static com.araba.cuma.araba.Fragment.LocationFragment.TRAVELER_FROM_CITY;
import static com.araba.cuma.araba.Fragment.LocationFragment.TRAVELER_TO_CITY;

/**
 * A simple {@link Fragment} subclass.
 */
public class TravelerFragment extends Fragment implements View.OnClickListener {

    private TextView yolcu_tex, sofor_text;
    private Spinner personSpinner, materialSpinner;
    private TextView kisi_text, esya_text;
    private RadioGroup radioGroup;
    private RadioButton kisi, kisi_ve_esya, esya;
    private EditText description, price;
    private Button selectDate, selectTime, post, from, to;
    private ImageButton iki_tarih_arasi, iki_saat_arasi;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String userId;
    private String uuidString;
    private RadioButton radioButton;
    private String[] personList = {"Kişi Seçiniz", "1", "2", "3", "4"};
    private String[] materialList = {"Eşya Seçiniz", "Valiz", "Ev Eşyası", "Kırılacak Eşya", "Beyaz Eşya"};

    private ArrayAdapter<String> adapterPerson, adapterMaterial;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    int birlikte = 0;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_traveler, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();//todo
        user = firebaseAuth.getCurrentUser();
        userId = user.getUid();

        initView();
        initSpinner();
        selectDate();
        selectTime();
        /*
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {
                    case R.id.radioButton_person:
                        kisi_text.setVisibility(View.VISIBLE);
                        kisi_image.setVisibility(View.VISIBLE);
                        personSpinner.setVisibility(View.VISIBLE);
                        esya_text.setVisibility(View.INVISIBLE);
                        esya_image.setVisibility(View.INVISIBLE);
                        materialSpinner.setVisibility(View.INVISIBLE);
                        birlikte = 1;
                        break;
                    case R.id.radioButton_person_and_material:
                        esya_text.setVisibility(View.VISIBLE);
                        esya_image.setVisibility(View.VISIBLE);
                        materialSpinner.setVisibility(View.VISIBLE);
                        kisi_text.setVisibility(View.VISIBLE);
                        kisi_image.setVisibility(View.VISIBLE);
                        personSpinner.setVisibility(View.VISIBLE);
                        birlikte = 2;
                        break;
                    case R.id.radioButton_material:
                        kisi_text.setVisibility(View.INVISIBLE);
                        kisi_image.setVisibility(View.INVISIBLE);
                        personSpinner.setVisibility(View.INVISIBLE);
                        esya_text.setVisibility(View.VISIBLE);
                        esya_image.setVisibility(View.VISIBLE);
                        materialSpinner.setVisibility(View.VISIBLE);
                        birlikte = 3;
                        break;

                }
            }
        });
        */

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chechkEmpty()) {
                    postSend();
                }

            }
        });
        return view;
    }

    private void initView() {
        post = view.findViewById(R.id.ok_passenger);
        personSpinner = view.findViewById(R.id.person__passenger);
        materialSpinner = view.findViewById(R.id.material_passenger);
        description = view.findViewById(R.id.description_passenger);
        selectDate = view.findViewById(R.id.date_passenger);
        selectTime = view.findViewById(R.id.hour_passenger);
        iki_tarih_arasi = view.findViewById(R.id.two_date_between_date_passenger);
        iki_saat_arasi = view.findViewById(R.id.two_between_hour_passenger);
        price = view.findViewById(R.id.price_passenger);
        radioGroup = view.findViewById(R.id.radioGroup);
        kisi = radioGroup.findViewById(R.id.radioButton_person);
        kisi_ve_esya = radioGroup.findViewById(R.id.radioButton_person_and_material);
        esya = radioGroup.findViewById(R.id.radioButton_material);
        from=view.findViewById(R.id.from_traveler);
        to=view.findViewById(R.id.to_traveler);
        from.setOnClickListener(this);
        to.setOnClickListener(this);

    }

    public void initSpinner() {

        adapterMaterial = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, materialList);
        adapterPerson = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, personList);
        adapterMaterial.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterPerson.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        personSpinner.setAdapter(adapterPerson);
        materialSpinner.setAdapter(adapterMaterial);

    }

    private void selectDate() {
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

    private void selectTime() {
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

    @Override
    public void onClick(View view) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        switch (view.getId()) {
            case R.id.from_traveler:
                args.putString(CITY, TRAVELER_FROM_CITY);
                break;
            case R.id.to_traveler:
                args.putString(CITY, TRAVELER_TO_CITY);
                break;
        }
        fragment.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.main_framelayout, fragment).commit();
    }
    public boolean chechkEmpty() {
        if ((birlikte == 1) && (personSpinner.getSelectedItem().toString().equals("Kişi Seçiniz"))) {
            Toast.makeText(getActivity(), "Kişi Seçiniz", Toast.LENGTH_LONG).show();
            return false;
        } else if ((birlikte == 2) && (materialSpinner.getSelectedItem().toString().equals("Eşya Seçiniz"))) {
            Toast.makeText(getActivity(), "Eşya veya Kişi Seçiniz", Toast.LENGTH_LONG).show();
            return false;
        } else if ((birlikte == 2) && (personSpinner.getSelectedItem().toString().equals("Kişi Seçiniz"))) {
            Toast.makeText(getActivity(), "Eşya veya Kişi Seçiniz", Toast.LENGTH_LONG).show();
            return false;
        } else if ((birlikte == 3) && (materialSpinner.getSelectedItem().toString().equals("Eşya Seçiniz"))) {
            Toast.makeText(getActivity(), "Kişi Seçiniz", Toast.LENGTH_LONG).show();
            return false;
        }
        /*else if (nereden_spinner.getSelectedItem().toString().equals("Nereden")) {
            Toast.makeText(getActivity(), "Sehir Seçiniz", Toast.LENGTH_LONG).show();
            return false;
        } else if (nereye_spinner.getSelectedItem().toString().equals("Nereye")) {
            Toast.makeText(getActivity(), "Sehir Seçiniz", Toast.LENGTH_LONG).show();
            return false;//Saat Seç
        }
        else if (nereden_spinner.getSelectedItem().toString().equals(nereye_spinner.getSelectedItem().toString())) {
            Toast.makeText(getActivity(), "Aynı şehri seçtiniz", Toast.LENGTH_LONG).show();
            return false;
        }
        */
        else if (selectDate.getText().toString().equals("Tarih seç")) {
            Toast.makeText(getActivity(), "Tarih Seçiniz", Toast.LENGTH_LONG).show();
            return false;
        } else if (selectTime.getText().toString().equals("Saat Seç")) {
            Toast.makeText(getActivity(), "Saat Seçiniz", Toast.LENGTH_LONG).show();
            return false;
        } else if (price.getText().toString().equals("Fiyat Seç")) {
            Toast.makeText(getActivity(), "Fiyat Seçiniz", Toast.LENGTH_LONG).show();
            return false;
        } else return true;
    }

    public void postSend() {
        UUID uuıd = UUID.randomUUID();
        uuidString = uuıd.toString();
        databaseReference.child("Yolcu").child(uuidString).child("kisi").setValue(personSpinner.getSelectedItem().toString());
        databaseReference.child("Yolcu").child(uuidString).child("esya").setValue(materialSpinner.getSelectedItem().toString());
        databaseReference.child("Yolcu").child(uuidString).child("tarih").setValue(selectDate.getText().toString());
        databaseReference.child("Yolcu").child(uuidString).child("saat").setValue(selectTime.getText().toString());
        databaseReference.child("Yolcu").child(uuidString).child("description").setValue(description.getText().toString());
        databaseReference.child("Yolcu").child(uuidString).child("kullanicipuan").setValue(4);
        databaseReference.child("Yolcu").child(uuidString).child("price").setValue(price.getText().toString());
        databaseReference.child("Yolcu").child(uuidString).child("statu").setValue("Yolcu");
        databaseReference.child("Yolcu").child(uuidString).child("id").setValue(uuidString);
        databaseReference.child("Yolcu").child(uuidString).child("userid").setValue(userId);





    }

}
