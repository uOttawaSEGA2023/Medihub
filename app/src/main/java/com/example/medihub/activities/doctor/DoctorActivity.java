package com.example.medihub.activities.doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.medihub.R;
import com.example.medihub.activities.registrations.LoginActivity;
import com.example.medihub.activities.registrations.WelcomeActivity;
import com.example.medihub.models.DoctorProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class DoctorActivity extends AppCompatActivity {
    private DoctorProfile user;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDB;

    // UI elements
    private Button logoutButton;
    private Button upcomingAppointmentsButton, pastAppointmentsButton, pendingAppointmentsButton, declinedAppintmentsButton, btnShifts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);


        user = (DoctorProfile) getIntent().getSerializableExtra("current user");
        Log.d("key1", user.getKey() + "");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseDatabase.getInstance();

        if (user == null) {
            firebaseAuth.signOut();

            Intent loginActivity = new Intent(this, LoginActivity.class);
            loginActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginActivity);
            finish();
        }

        logoutButton = findViewById(R.id.logoutButton);
        upcomingAppointmentsButton = findViewById(R.id.btnUpcomingAppointments);
        pastAppointmentsButton = findViewById(R.id.btnPastAppointments);
        pendingAppointmentsButton = findViewById(R.id.btnPendingAppointments);
        declinedAppintmentsButton = findViewById(R.id.btnDeclinedAppointments);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        upcomingAppointmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorActivity.this, UpcomingAppointmentsActivity.class);
                intent.putExtra("current user", user);
                startActivity(intent);
            }
        });

        btnShifts = findViewById(R.id.btnShifts);
        btnShifts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DoctorActivity.this, ShiftActivity.class);
                intent.putExtra("current user", user);
                startActivity(intent);


            }
        });

        pastAppointmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorActivity.this, PastAppointmentsActivity.class);
                intent.putExtra("current user", user);
                startActivity(intent);
            }
        });

        pendingAppointmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorActivity.this, PendingAppointmentsActivity.class);
                intent.putExtra("current user", user);
                startActivity(intent);
            }
        });

        declinedAppintmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorActivity.this, DeclinedAppointmentsActivity.class);
                intent.putExtra("current user", user);
                startActivity(intent);
            }
        });
    }

    private void logout() {
        if (firebaseAuth.getCurrentUser() != null) {
            firebaseAuth.signOut();

            Intent loginIntent = new Intent(DoctorActivity.this, LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);

            DoctorActivity.this.finish();
        }
    }


}