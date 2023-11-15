package com.example.medihub.activities.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medihub.R;
import com.example.medihub.models.DoctorProfile;

public class ShiftMenu extends AppCompatActivity {

    private DoctorProfile user;
    private Button create;
    private Button view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_menu);

        user = (DoctorProfile) getIntent().getSerializableExtra("current user");

        view = findViewById(R.id.btnView);
        create = findViewById(R.id.btnCreateShift);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ShiftMenu.this, ShiftView.class);
                intent.putExtra("current user", user);
                startActivity(intent);

            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ShiftMenu.this,ShiftActivity.class);
                intent.putExtra("current user", user);
                startActivity(intent);


            }
        });


    }
}
