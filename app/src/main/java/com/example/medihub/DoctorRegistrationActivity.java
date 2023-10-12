package com.example.medihub;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.example.medihub.enums.DoctorSpecialty;

public class DoctorRegistrationActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);

        // get specialties layout
        LinearLayout specialtiesLayout = findViewById(R.id.specialties);

        // dynamically add doctor specialties as checkboxes to specialties layout
        for (DoctorSpecialty spec : DoctorSpecialty.values()) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(spec.toString().replaceAll("_", " "));
            specialtiesLayout.addView(checkBox);
        }
    }
}
