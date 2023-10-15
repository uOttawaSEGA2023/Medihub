package com.example.medihub.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.medihub.R;

public class RegistrationOptionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_option);
        Button doctor_button = findViewById(R.id.buttonDoc);
        Button patient_button = findViewById(R.id.buttonPatient);

        doctor_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDocRegistration();
            }
        });

        patient_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPatientRegistration();
            }
        });

    }

    public void startDocRegistration()
    {
        Intent intent = new Intent(this, DoctorRegistrationActivity.class);
        startActivity(intent);
    }

    public void startPatientRegistration()
    {
        Intent intent = new Intent(this, PatientRegistrationActivity.class);
        startActivity(intent);
    }
}
