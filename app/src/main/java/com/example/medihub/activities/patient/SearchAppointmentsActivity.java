package com.example.medihub.activities.patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.medihub.R;
import com.example.medihub.enums.DoctorSpecialty;

import java.util.ArrayList;

public class SearchAppointmentsActivity extends AppCompatActivity {

    private LinearLayout specialtiesLayout;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_appointments);

        this.specialtiesLayout = findViewById(R.id.specialtyLayout);
        this.searchButton = findViewById(R.id.searchButton);

        for (DoctorSpecialty spec : DoctorSpecialty.values()) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(spec.toString().replaceAll("_", " "));
            this.specialtiesLayout.addView(checkBox);
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Integer> checkedIndexes = checkCheckBoxes();


//                Log.d("asdasd", checkedIndexes.toString());
//                // Display a Toast with the checked indexes
//                StringBuilder message = new StringBuilder("Checked Indexes: ");
//                for (Integer index : checkedIndexes) {
//                    message.append(index).append(", ");
//                }
//
//                if (checkedIndexes.size() > 0) {
//                    // Remove the trailing comma and space
//                    message.delete(message.length() - 2, message.length());
//                } else {
//                    message.append("None");
//                }
//
//                Toast.makeText(SearchAppointmentsActivity.this, message.toString(), Toast.LENGTH_SHORT).show();
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