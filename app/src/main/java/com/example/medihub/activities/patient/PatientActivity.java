package com.example.medihub.activities.patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.medihub.R;
import com.example.medihub.activities.doctor.DoctorActivity;
import com.example.medihub.activities.doctor.UpcomingAppointmentsActivity;
import com.example.medihub.activities.registrations.LoginActivity;
import com.example.medihub.models.PatientProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class PatientActivity extends AppCompatActivity {
    private PatientProfile user;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDB;

    // UI elements
    private Button logoutButton;
    private Button searchAppointButton;
    private Button upcoming;
    private Button past;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        user = (PatientProfile) getIntent().getSerializableExtra("current user");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseDatabase.getInstance();

        if (user == null) {
            firebaseAuth.signOut();

            Intent loginActivity = new Intent(this, LoginActivity.class);
            loginActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginActivity);
            finish();
        }

        // button handling
        logoutButton = findViewById(R.id.logoutButton);
        searchAppointButton = findViewById(R.id.btnBookAppointments);
        upcoming = findViewById(R.id.btnUpcomingAppointmentsPatient);
        past = findViewById(R.id.btnPastAppointmentsPatient);

        searchAppointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientActivity.this, SearchAppointmentsActivity.class);
                intent.putExtra("current user", user);
                startActivity(intent);
            }
        });

        upcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientActivity.this, ViewAppointments.class);
                intent.putExtra("current user", user);
                startActivity(intent);
            }
        });

        past.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientActivity.this, ViewPastAppointments.class);
                intent.putExtra("current user", user);
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    private void logout() {
        if (firebaseAuth.getCurrentUser() != null) {
            firebaseAuth.signOut();

            Intent loginIntent = new Intent(PatientActivity.this, LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);

            PatientActivity.this.finish();
        }
    }
}