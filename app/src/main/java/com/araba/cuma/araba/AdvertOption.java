package com.araba.cuma.araba;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdvertOption {
    Context context;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    Map<String, Object> optionAdvert = new HashMap<>();

    public AdvertOption(Context context) {
        this.context = context;
    }

    public void optionAdvert(final String dbName, String advertId, String userId, String status, String imageUrl, String nameSurname,
                             String fromCity, String toCity, String date, String time, String description, String driverPerson,
                             String travelerPerson, String carModel, String material, String savedUserId) {
        optionAdvert.put("advertId", advertId);
        optionAdvert.put("userId", userId);
        optionAdvert.put("status", status);
        optionAdvert.put("imageUrl", imageUrl);
        optionAdvert.put("nameSurname", nameSurname);
        optionAdvert.put("fromCity", fromCity);
        optionAdvert.put("toCity", toCity);
        optionAdvert.put("date", date);
        optionAdvert.put("time", time);
        optionAdvert.put("description", description);
        optionAdvert.put("driverPerson", driverPerson);
        optionAdvert.put("travelerPerson", travelerPerson);
        optionAdvert.put("carModel", carModel);
        optionAdvert.put("material", material);
        optionAdvert.put("savedUserId", savedUserId);
        firebaseFirestore.collection(dbName).document(advertId)
                .set(optionAdvert).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (dbName.equals("ComplaintAdvert"))
                    Toast.makeText(context, "Kısa süre içerisinde değerlendirilecektir", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, "İlan kaydedildi", Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }
}
