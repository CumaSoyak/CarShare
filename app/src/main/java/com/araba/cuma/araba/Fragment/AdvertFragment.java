package com.araba.cuma.araba.Fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.araba.cuma.araba.DateParse;
import com.araba.cuma.araba.Model.Advert;
import com.araba.cuma.araba.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.UUID;

import static com.araba.cuma.araba.Constant.*;


public class AdvertFragment extends Fragment implements View.OnClickListener {
    View view;
    private RelativeLayout linearLayoutTraveler, linearLayoutDriver;
    private ImageView driverImage, travelerImage;
    private TextView travelerText, driverText;
    private TextView travelerPersonSpinner, materialSpinner, driverPersonSpinner, carModelSpinner;
    private EditText description;
    private TextView selectDate, selectTime, post, fromCity, toCity;
    private ImageButton iki_tarih_arasi, iki_saat_arasi;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private String userId;
    private String uuidString;
    private LinearLayout layoutCar, layoutMaterial, layoutPersonDriver, layoutPersonTraveler;


    private DatePickerDialog.OnDateSetListener dateSetListener;
    private BottomSheetDialog bottomSheetDialog;

    private SharedPreferences preferences;
    private static String statusString;
    private String userNameSurname = null;
    private String userPhoto = null;
    private static final String LAYOUT_CHOOSE = "LAYOUT_CHOOSE";
    private View lineOne, lineTwo, lineThree, lineFour;
    private TextView menuOne, menuTwo, menuThree, menuFour;
    private String bottomSheetStatus;
    private View mBottomViewTraveler, mBottomViewDriver;
    private ProgressBar progressBar;


    public static AdvertFragment newInstance(String param1, String param2) {
        return new AdvertFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_advert, container, false);

        firebaseAuth = FirebaseAuth.getInstance();


        user = firebaseAuth.getCurrentUser();
        userId = user.getUid();
        preferences = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);


        initView();
        bottomSheetSetup();
        selectDate();
        selectTime();
        getNameAndPhoto();
        getLayoutChoose();
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chechkEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    postSend(userNameSurname, userPhoto);
                } else
                    Toast.makeText(getActivity(), "Bilgileri kontrol ediniz", Toast.LENGTH_SHORT).show();

            }
        });

        fromCity.setText(FROM);
        toCity.setText(TO);


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        statusString = getResources().getString(R.string.passenger);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.traveler_layout:
                linearLayoutTraveler.setBackgroundResource(R.color.beyaz);
                travelerImage.setImageResource(R.drawable.ic_traveler_blue);
                travelerText.setTextColor(ContextCompat.getColor(getActivity(), R.color.mavi));
                mBottomViewTraveler.setBackgroundResource(R.color.mavi);
                linearLayoutDriver.setBackgroundResource(R.color.gri);
                driverImage.setImageResource(R.drawable.ic_car);
                driverText.setTextColor(ContextCompat.getColor(getActivity(),
                        R.color.text_color));
                mBottomViewDriver.setBackgroundResource(R.color.gri);
                layoutCar.setVisibility(View.GONE);
                layoutPersonDriver.setVisibility(View.GONE);
                layoutMaterial.setVisibility(View.VISIBLE);
                layoutPersonTraveler.setVisibility(View.VISIBLE);
                lineTwo.setVisibility(View.VISIBLE);
                lineFour.setVisibility(View.VISIBLE);
                lineOne.setVisibility(View.GONE);
                lineThree.setVisibility(View.GONE);
                statusString = getResources().getString(R.string.passenger);
                saveLayoutChoose("0");
                break;
            case R.id.driver_layout:

                linearLayoutDriver.setBackgroundResource(R.color.beyaz);
                driverImage.setImageResource(R.drawable.ic_car_blue);
                driverText.setTextColor(ContextCompat.getColor(getActivity(), R.color.mavi));
                mBottomViewDriver.setBackgroundResource(R.color.mavi);

                linearLayoutTraveler.setBackgroundResource(R.color.gri);
                travelerImage.setImageResource(R.drawable.ic_traveler);
                travelerText.setTextColor(ContextCompat.getColor(getActivity(),
                        R.color.text_color));
                mBottomViewTraveler.setBackgroundResource(R.color.gri);

                layoutCar.setVisibility(View.VISIBLE);
                layoutPersonDriver.setVisibility(View.VISIBLE);
                layoutMaterial.setVisibility(View.GONE);
                layoutPersonTraveler.setVisibility(View.GONE);
                lineOne.setVisibility(View.VISIBLE);
                lineThree.setVisibility(View.VISIBLE);
                lineTwo.setVisibility(View.GONE);
                lineFour.setVisibility(View.GONE);
                statusString = getResources().getString(R.string.chauffeur);
                saveLayoutChoose("1");
                break;
        }
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        switch (view.getId()) {
            case R.id.from:
                args.putString(CITY, ADVERT_FROM_CITY);
                fragment.setArguments(args);
                getFragmentManager().beginTransaction().setCustomAnimations(
                        R.anim.search_in_down, R.anim.search_out_up).replace(R.id.main_framelayout, fragment).addToBackStack(null).commit();
                break;
            case R.id.to:
                args.putString(CITY, ADVERT_TO_CITY);
                fragment.setArguments(args);
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.search_in_down, R.anim.search_out_up).replace(R.id.main_framelayout, fragment).addToBackStack(null).commit();

                break;
        }
        switch (view.getId()) {
            case R.id.menu_one:
                printSpinner(menuOne.getText().toString());
                break;
            case R.id.menu_two:
                printSpinner(menuTwo.getText().toString());
                break;
            case R.id.menu_three:
                printSpinner(menuThree.getText().toString());
                break;
            case R.id.menu_four:
                printSpinner(menuFour.getText().toString());

                break;
        }
        switch (view.getId()) {
            case R.id.person_traveler:
                menuOne.setText("1 kişiyiz");
                menuTwo.setText("2 kişiyiz");
                menuThree.setText("4 veya daha fazlası");
                menuFour.setText("Sadece eşya göndereceğim");
                bottomSheetStatus = "person_traveler";
                bottomSheetDialog.show();
                break;
            case R.id.material_traveler:
                menuOne.setText("Mutfak eşyası");
                menuTwo.setText("Ev eşyası");
                menuThree.setText("Kişisel eşya");
                menuFour.setText("Eşyam yok sadece ben/biz");
                bottomSheetStatus = "material_traveler";
                bottomSheetDialog.show();
                break;
            case R.id.car_driver:
                menuOne.setText("Otomobil");
                menuTwo.setText("Kamyon");
                menuThree.setText("Motosiklet");
                menuFour.setText("Ticari");
                bottomSheetStatus = "car_driver";

                bottomSheetDialog.show();
                break;
            case R.id.person_driver:
                menuOne.setText("1 kişi götürebilirim");
                menuTwo.setText("2 kişi götürebilirim");
                menuThree.setText("4 veya daha  fazlası");
                menuFour.setText("Sadece eşya götürebilirim");
                bottomSheetStatus = "person_driver";

                bottomSheetDialog.show();
                break;
        }

    }

    private void printSpinner(String menu) {
        bottomSheetDialog.show();
        if (bottomSheetStatus.equals("person_traveler"))
            travelerPersonSpinner.setText(menu);
        if (bottomSheetStatus.equals("material_traveler"))
            materialSpinner.setText(menu);
        if (bottomSheetStatus.equals("car_driver"))
            carModelSpinner.setText(menu);
        if (bottomSheetStatus.equals("person_driver"))
            driverPersonSpinner.setText(menu);

        bottomSheetDialog.dismiss();

    }

    private void bottomSheetSetup() {
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        View bottomSheetDialogView = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        bottomSheetDialog.setContentView(bottomSheetDialogView);
        menuOne = bottomSheetDialogView.findViewById(R.id.menu_one);
        menuTwo = bottomSheetDialogView.findViewById(R.id.menu_two);
        menuThree = bottomSheetDialogView.findViewById(R.id.menu_three);
        menuFour = bottomSheetDialogView.findViewById(R.id.menu_four);
        travelerPersonSpinner.setOnClickListener(this);
        materialSpinner.setOnClickListener(this);
        carModelSpinner.setOnClickListener(this);
        driverPersonSpinner.setOnClickListener(this);

        menuOne.setOnClickListener(this);
        menuTwo.setOnClickListener(this);
        menuThree.setOnClickListener(this);
        menuFour.setOnClickListener(this);
    }

    private void initView() {


        linearLayoutTraveler = view.findViewById(R.id.traveler_layout);
        linearLayoutDriver = view.findViewById(R.id.driver_layout);
        travelerImage = view.findViewById(R.id.traveler_image);
        driverImage = view.findViewById(R.id.deriver_image);
        travelerText = view.findViewById(R.id.traveler_text);
        driverText = view.findViewById(R.id.driver_text);

        mBottomViewDriver = view.findViewById(R.id.driver_bottom_view);
        mBottomViewTraveler = view.findViewById(R.id.traveler_bottom_view);

        layoutCar = view.findViewById(R.id.layout_car);
        layoutMaterial = view.findViewById(R.id.layout_material);
        layoutPersonDriver = view.findViewById(R.id.layout_person_driver);
        layoutPersonTraveler = view.findViewById(R.id.layout_person_traveler);

        linearLayoutTraveler.setOnClickListener(this);
        linearLayoutDriver.setOnClickListener(this);

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


        lineOne = view.findViewById(R.id.line_one);
        lineTwo = view.findViewById(R.id.line_two);
        lineThree = view.findViewById(R.id.line_theree);
        lineFour = view.findViewById(R.id.line_four);

        progressBar = view.findViewById(R.id.progressBar_advert);


    }

    private void saveLayoutChoose(String choose) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LAYOUT_CHOOSE, choose);
        editor.commit();

    }

    private void getLayoutChoose() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String layoutChoose = sharedPreferences.getString(LAYOUT_CHOOSE, "0");
        if (layoutChoose.equals("0")) {
            linearLayoutTraveler.performClick();
        } else if (layoutChoose.equals("1")) {
            linearLayoutDriver.performClick();
        }
    }

    private void selectDate() {
        final DateParse dataParse = new DateParse();
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
                dataParse.dateParse(month);
                String date = day + " " + dataParse.dateParse(month);
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
                mTimePicker.show();

            }
        });
    }

    public boolean chechkEmpty() {
        if (fromCity.getText().equals(getString(R.string.nereden))) {
            return false;
        } else if (fromCity.getText().toString().equals(toCity.getText().toString()))
            return false;
        else if (toCity.getText().equals(getString(R.string.nereye))) {
            return false;
        } else if (statusString.equals(getResources().getString(R.string.chauffeur)) &&
                carModelSpinner.getText().toString().equals(getResources().getString(R.string.car_select)))
            return false;
        else if (statusString.equals(getResources().getString(R.string.chauffeur)) &&
                driverPersonSpinner.getText().toString().equals(getResources().getString(R.string.person_driver_select))) {
            return false;
        } else if (statusString.equals(getResources().getString(R.string.passenger)) &&
                travelerPersonSpinner.getText().toString().equals(getResources().getString(R.string.person_traveler_select)))
            return false;
        else if (statusString.equals(getResources().getString(R.string.passenger)) &&
                materialSpinner.getText().toString().equals(getResources().getString(R.string.material_select)))
            return false;
        else if (selectDate.getText().toString().equals(getResources().getString(R.string.select_date)))
            return false;
        else if (selectTime.getText().toString().equals(getResources().getString(R.string.select_time)))
            return false;
        else return true;
    }

    private void getNameAndPhoto() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userPhoto = sharedPreferences.getString(CURRENT_PHOTO_URL, "");
        userNameSurname = sharedPreferences.getString(CURRENT_NAME, "");
    }

    public void postSend(String name, String photo) {

        UUID uuıd = UUID.randomUUID();
        uuidString = uuıd.toString();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference newAdvert = firebaseFirestore.collection("ilan").document(uuidString);
        Advert advert = new Advert();
        advert.setAdvertId(uuidString);
        advert.setUserId(userId);
        advert.setImageUrl(photo);
        advert.setNameSurname(name);
        advert.setFromCity(fromCity.getText().toString());
        advert.setToCity(toCity.getText().toString());
        if (statusString.equals(getResources().getString(R.string.passenger))) {
            advert.setStatus(getResources().getString(R.string.passenger));
            advert.setMaterial(materialSpinner.getText().toString());
            advert.setTravelerPerson(travelerPersonSpinner.getText().toString());
        }
        if (statusString.equals(getResources().getString(R.string.chauffeur))) {
            advert.setStatus(getResources().getString(R.string.chauffeur));
            advert.setCarModel(carModelSpinner.getText().toString());
            advert.setDriverPerson(driverPersonSpinner.getText().toString());

        }
        advert.setDate(selectDate.getText().toString());
        advert.setTime(selectTime.getText().toString());
        advert.setDescription(description.getText().toString());
        newAdvert.set(advert).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressBar.setVisibility(View.GONE);
                Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                        "Yolculuk paylaşılmıştır . İyi yolculuklar :)", Snackbar.LENGTH_LONG);
                snackBar.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar snackBar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                        "! Hata Tekrar deneyiniz", Snackbar.LENGTH_LONG);
                snackBar.show();
            }
        });

    }

}
