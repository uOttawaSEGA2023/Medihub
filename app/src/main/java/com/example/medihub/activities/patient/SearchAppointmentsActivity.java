package com.example.medihub.activities.patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.medihub.R;
import com.example.medihub.enums.DoctorSpecialty;
import com.example.medihub.models.PatientProfile;

import java.util.ArrayList;

public class SearchAppointmentsActivity extends AppCompatActivity {

    private LinearLayout specialtiesLayout;
    private Button searchButton;
    private Button backButton;
    private RadioGroup radioGroup;
    private PatientProfile user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_appointments);

        this.specialtiesLayout = findViewById(R.id.specialtyLayout);
        this.searchButton = findViewById(R.id.searchButton);
        this.backButton = findViewById(R.id.backButton);
        this.radioGroup = findViewById(R.id.specialtiesRadioGroup);

        user = (PatientProfile) getIntent().getSerializableExtra("current user");

        for (DoctorSpecialty spec : DoctorSpecialty.values()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(spec.toString().replaceAll("_", " "));
            this.radioGroup.addView(radioButton);
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checkedIndex = checkCheckBoxes();

                Intent intent = new Intent(SearchAppointmentsActivity.this, SelectAppointmentActivity.class);
                intent.putExtra("selected specialties", checkedIndex);
                startActivity(intent);

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchAppointmentsActivity.this, PatientActivity.class);
                intent.putExtra("current user", user);
                startActivity(intent);
            }
        });
    }

    private int checkCheckBoxes() {

        for (int i = 0; i < specialtiesLayout.getChildCount(); i++)
        {
            View childView = radioGroup.getChildAt(i);

            // Exclude TextView from consideration
            if (childView instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) childView;

                // Check the state of each CheckBox
                if (radioButton.isChecked()) {
                    if (i-1>=0) {
                        return i-1;
                    }
                }
            }
        }

        return -1;
    }
}