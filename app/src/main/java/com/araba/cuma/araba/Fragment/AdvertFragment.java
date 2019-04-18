package com.araba.cuma.araba.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.araba.cuma.araba.Class.Advert;
import com.araba.cuma.araba.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.araba.cuma.araba.Fragment.LocationFragment.ADVERT_FROM_CITY;
import static com.araba.cuma.araba.Fragment.LocationFragment.ADVERT_TO_CITY;
import static com.araba.cuma.araba.Fragment.LocationFragment.CITY;
import static com.araba.cuma.araba.Fragment.LocationFragment.SEARCH_FROM_CITY;
import static com.araba.cuma.araba.Fragment.LocationFragment.SEARCH_TO_CITY;

public class AdvertFragment extends Fragment implements View.OnClickListener {
    View view;
    private LinearLayout linearLayoutTraveler, linearLayoutDriver, layoutPlate;
    private ImageView driverImage, travelerImage;
    private TextView travelerText, driverText;
    private Spinner travelerPersonSpinner, materialSpinner, driverPersonSpinner, carModelSpinner;
    private EditText description, price, plate;
    private Button selectDate, selectTime, post, fromCity, toCity;
    private ImageButton iki_tarih_arasi, iki_saat_arasi;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String userId;
    private String uuidString;
    private RelativeLayout layoutCar, layoutMaterial, layoutPersonDriver, layoutPersonTraveler;
    Activity activity = getActivity();

    private List<String> personListTraveler;
    private List<String> materialList;
    private List<String> personListDriver;
    private List<String> carModelList;

    private ArrayAdapter<String> adapterPersonTraveler, adapterMaterial;
    private ArrayAdapter<String> adapterPersonDriver, adapterCarModel;

    private DatePickerDialog.OnDateSetListener dateSetListener;

    private String cityInfoFrom = null, cityInfoTo = null;
    private SharedPreferences preferences;
    private String statusString = "Traveler";
    private String travelerString = "Traveler";
    private String driverString = "Driver";


    public static AdvertFragment newInstance(String param1, String param2) {
        AdvertFragment fragment = new AdvertFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_advert, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();//todo
        user = firebaseAuth.getCurrentUser();
        userId = user.getUid();

        preferences = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);


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
                        travelerPersonSpinner.setVisibility(View.VISIBLE);
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
                        travelerPersonSpinner.setVisibility(View.VISIBLE);
                        birlikte = 2;
                        break;
                    case R.id.radioButton_material:
                        kisi_text.setVisibility(View.INVISIBLE);
                        kisi_image.setVisibility(View.INVISIBLE);
                        travelerPersonSpinner.setVisibility(View.INVISIBLE);
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
        if (!preferences.getString(SEARCH_FROM_CITY, "0").equals("0")) {

            fromCity.setText(preferences.getString(ADVERT_FROM_CITY, "1"));
        }

        if (!preferences.getString(SEARCH_TO_CITY, "0").equals("0")) {

            toCity.setText(preferences.getString(ADVERT_TO_CITY, "1"));
        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cityInfoFrom = getArguments().getString(ADVERT_FROM_CITY);
            cityInfoTo = getArguments().getString(ADVERT_TO_CITY);
            SharedPreferences preferences = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            if (cityInfoFrom != null) {
                editor.putString(ADVERT_FROM_CITY, cityInfoFrom);
                editor.commit();
            }
            if (cityInfoTo != null) {
                editor.putString(ADVERT_TO_CITY, cityInfoTo);
                editor.commit();

            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.traveler_layout:
                linearLayoutDriver.setBackgroundResource(R.drawable.anasayfa_secenek_right_tikla);
                driverImage.setImageResource(R.drawable.ic_car_blue);
                travelerImage.setImageResource(R.drawable.ic_traveler_white);
                linearLayoutTraveler.setBackgroundResource(R.drawable.anasayfa_secenek_left);
                travelerText.setTextColor(ContextCompat.getColor(getActivity(), R.color.beyaz));
                driverText.setTextColor(ContextCompat.getColor(getActivity(), R.color.mavi));
                layoutPlate.setVisibility(View.GONE);
                layoutCar.setVisibility(View.GONE);
                layoutPersonDriver.setVisibility(View.GONE);
                layoutMaterial.setVisibility(View.VISIBLE);
                layoutPersonTraveler.setVisibility(View.VISIBLE);
                statusString = travelerString;
                break;
            case R.id.driver_layout:
                linearLayoutTraveler.setBackgroundResource(R.drawable.anasayfa_secenek_left_tikla);
                travelerImage.setImageResource(R.drawable.ic_traveler_blue);
                driverImage.setImageResource(R.drawable.ic_car_white);
                linearLayoutDriver.setBackgroundResource(R.drawable.anasayfa_secenek_right);
                travelerText.setTextColor(ContextCompat.getColor(getActivity(), R.color.mavi));
                driverText.setTextColor(ContextCompat.getColor(getActivity(), R.color.beyaz));
                layoutPlate.setVisibility(View.VISIBLE);
                layoutCar.setVisibility(View.VISIBLE);
                layoutPersonDriver.setVisibility(View.VISIBLE);
                layoutMaterial.setVisibility(View.GONE);
                layoutPersonTraveler.setVisibility(View.GONE);
                statusString = driverString;
                break;
        }
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        switch (view.getId()) {
            case R.id.from:
                args.putString(CITY, ADVERT_FROM_CITY);
                fragment.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.main_framelayout, fragment).commit();
                break;
            case R.id.to:
                args.putString(CITY, ADVERT_TO_CITY);
                fragment.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.main_framelayout, fragment).commit();

                break;
        }

    }


    private void initView() {
        personListTraveler = Arrays.asList(getResources().getStringArray(R.array.person_traveler_default_spin_array));
        personListDriver = Arrays.asList(getResources().getStringArray(R.array.person_driver_default_spin_array));
        materialList = Arrays.asList(getResources().getStringArray(R.array.material_default_spin_array));
        carModelList = Arrays.asList(getResources().getStringArray(R.array.car_model_default_spin_array));
        linearLayoutTraveler = view.findViewById(R.id.traveler_layout);
        linearLayoutDriver = view.findViewById(R.id.driver_layout);
        travelerImage = view.findViewById(R.id.traveler_image);
        driverImage = view.findViewById(R.id.deriver_image);
        travelerText = view.findViewById(R.id.traveler_text);
        driverText = view.findViewById(R.id.driver_text);

        layoutPlate = view.findViewById(R.id.layout_plate);
        layoutCar = view.findViewById(R.id.layout_car);
        layoutMaterial = view.findViewById(R.id.layout_material);
        layoutPersonDriver = view.findViewById(R.id.layout_person_driver);
        layoutPersonTraveler = view.findViewById(R.id.layout_person_traveler);

        linearLayoutTraveler.setOnClickListener(this);
        linearLayoutDriver.setOnClickListener(this);
        linearLayoutTraveler.performClick();

        fromCity = view.findViewById(R.id.from);
        toCity = view.findViewById(R.id.to);
        fromCity.setOnClickListener(this);
        toCity.setOnClickListener(this);

        post = view.findViewById(R.id.post);
        travelerPersonSpinner = view.findViewById(R.id.person_traveler);
        materialSpinner = view.findViewById(R.id.material_traveler);
        carModelSpinner = view.findViewById(R.id.car_driver);
        driverPersonSpinner = view.findViewById(R.id.person_driver);
        description = view.findViewById(R.id.description);
        selectDate = view.findViewById(R.id.date);
        selectTime = view.findViewById(R.id.hour);
        iki_tarih_arasi = view.findViewById(R.id.two_date_between_date);
        iki_saat_arasi = view.findViewById(R.id.two_between_hour);
        price = view.findViewById(R.id.price);
        plate = view.findViewById(R.id.plate_driver);
        /*
        radioGroup = view.findViewById(R.id.radioGroup);
        kisi = radioGroup.findViewById(R.id.radioButton_person);
        kisi_ve_esya = radioGroup.findViewById(R.id.radioButton_person_and_material);
        esya = radioGroup.findViewById(R.id.radioButton_material);

        */

    }

    public void initSpinner() {

        adapterMaterial = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, materialList);
        adapterPersonTraveler = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, personListTraveler);
        adapterCarModel = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, carModelList);
        adapterPersonDriver = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, personListDriver);


        adapterMaterial.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterPersonTraveler.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterCarModel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterPersonDriver.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        travelerPersonSpinner.setAdapter(adapterPersonTraveler);
        materialSpinner.setAdapter(adapterMaterial);

        carModelSpinner.setAdapter(adapterCarModel);
        driverPersonSpinner.setAdapter(adapterPersonDriver);

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

    public boolean chechkEmpty() {
        if (fromCity.getText().equals("")) {
            Toast.makeText(getActivity(), "Sehir Seçiniz", Toast.LENGTH_LONG).show();
            return false;
        } else if (toCity.getText().equals("")) {
            Toast.makeText(getActivity(), "Sehir Seçiniz", Toast.LENGTH_LONG).show();
            return false;
        }
        if (statusString.equals(driverString)) {
            if (carModelSpinner.getSelectedItem().toString()
                    .equals(getResources().getString(R.string.car_model_default_spin))) {
                Toast.makeText(getActivity(), "Araç cinsi seçiniz ", Toast.LENGTH_LONG).show();
                return false;
            } else if (driverPersonSpinner.getSelectedItem().toString().
                    equals(getResources().getString(R.string.person_driver_default_spin))) {
                Toast.makeText(getActivity(), "Kişi Seçiniz Araba", Toast.LENGTH_LONG).show();
                return false;
            } else if (plate.getText().toString().matches("")) {
                Toast.makeText(getActivity(), "Plaka seçiniz", Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }
        if (statusString.equals(travelerString)) {
            if (travelerPersonSpinner.getSelectedItem().toString()
                    .equals(getResources().getString(R.string.person_traveler_default_spin))) {
                Toast.makeText(getActivity(), "Kişi Seçiniz", Toast.LENGTH_LONG).show();
                return false;
            } else if (materialSpinner.getSelectedItem().toString()
                    .equals(getResources().getString(R.string.material_default_spin))) {
                Toast.makeText(getActivity(), "Eşya veya Kişi Seçiniz", Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        } else if (selectDate.getText().toString().equals(getResources().getString(R.string.select_date))) {
            Toast.makeText(getActivity(), "Tarih Seçiniz", Toast.LENGTH_LONG).show();
            return false;
        } else if (selectTime.getText().toString().equals(getResources().getString(R.string.select_time))) {
            Toast.makeText(getActivity(), "Saat Seçiniz", Toast.LENGTH_LONG).show();
            return false;
        } else if (price.getText().toString().equals("Fiyat Seç")) {
            Toast.makeText(getActivity(), "Fiyat Seçiniz", Toast.LENGTH_LONG).show();
            return false;
        } else return true;
    }

    public void postSend() {
        /*

    private String advertId;
    private String userId;
    private String nameSurname;
    private String fromCity;
    private String toCity;
    private String material;
    private String travelerPerson;
    private String carModel;
    private String driverPerson;
    private String plate;
    private String date;
    private String time;
    private String description;
    private String price;
     */
        UUID uuıd = UUID.randomUUID();
        uuidString = uuıd.toString();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference newAdvert = firebaseFirestore.collection("ilan").document();
        Advert advert = new Advert();
        advert.setAdvertId(uuidString);
        advert.setUserId(userId);
        advert.setNameSurname("cuma Soyak");
        advert.setFromCity(fromCity.getText().toString());
        advert.setToCity(toCity.getText().toString());
        if (statusString.equals(travelerString)) {
            advert.setMaterial(materialSpinner.getSelectedItem().toString());
            advert.setTravelerPerson(travelerPersonSpinner.getSelectedItem().toString());
        }
        if (statusString.equals(driverString)) {
            advert.setCarModel(carModelSpinner.getSelectedItem().toString());
            advert.setDriverPerson(driverPersonSpinner.getSelectedItem().toString());
            advert.setPlate(plate.getText().toString());
        }
        advert.setDate(selectDate.getText().toString());
        advert.setTime(selectTime.getText().toString());
        advert.setDescription(description.getText().toString());
        advert.setPrice(price.getText().toString());
        firebaseFirestore.collection("ilan").document().set(advert).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), "Paylaşılmıştır", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }


}
