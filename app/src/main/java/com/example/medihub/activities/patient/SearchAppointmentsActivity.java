package com.example.medihub.activities.patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.example.medihub.R;
import com.example.medihub.enums.DoctorSpecialty;
import com.example.medihub.models.DoctorProfile;
import com.example.medihub.models.PatientProfile;

import java.util.ArrayList;

public class SearchAppointmentsActivity extends AppCompatActivity {

    private LinearLayout specialtiesLayout;
    private Button searchButton;
    private Button backButton;
    private PatientProfile user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_appointments);

        this.specialtiesLayout = findViewById(R.id.specialtyLayout);
        this.searchButton = findViewById(R.id.searchButton);
        this.backButton = findViewById(R.id.backButton);

        user = (PatientProfile) getIntent().getSerializableExtra("current user");

        for (DoctorSpecialty spec : DoctorSpecialty.values()) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(spec.toString().replaceAll("_", " "));
            this.specialtiesLayout.addView(checkBox);
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Integer> checkedIndexes = checkCheckBoxes();

                Intent intent = new Intent(SearchAppointmentsActivity.this, SelectAppointmentActivity.class);
                intent.putExtra("selected specialties", checkedIndexes);
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

    private ArrayList<Integer> checkCheckBoxes() {
        ArrayList<Integer> checkedIndexesList = new ArrayList<>();

        for (int i = 0; i < specialtiesLayout.getChildCount(); i++)
        {
            View childView = specialtiesLayout.getChildAt(i);

            // Exclude TextView from consideration
            if (childView instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) childView;

                // Check the state of each CheckBox
                if (checkBox.isChecked()) {
                    if (i-1>=0)
                        checkedIndexesList.add(i-1);
                }
            }
        }

        return checkedIndexesList;
    }
}