package com.example.medihub.activities.patient;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihub.R;
import com.example.medihub.adapters.appointmentRecycleAdapter;
import com.example.medihub.adapters.appointmentRecycleAdapterPatient;
import com.example.medihub.database.AppointmentsReference;
import com.example.medihub.database.UsersReference;
import com.example.medihub.enums.RequestStatus;
import com.example.medihub.models.Appointment;
import com.example.medihub.models.DoctorProfile;
import com.example.medihub.models.PatientProfile;
import com.example.medihub.models.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public abstract class AbstractAppointmentActivity extends AppCompatActivity {
    Button authorizeAllButton;
    View overlay;

    protected ArrayList<Appointment> appointments;
    protected ArrayList<DoctorProfile> doctors;
    protected RecyclerView recyclerView;
    protected appointmentRecycleAdapterPatient.RecyclerViewClickListener listener;
    protected UserProfile patient;
    protected Query appointmentsQuery;
    protected int totalChildren = 0;
    protected Button backButton;

    appointmentRecycleAdapterPatient adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_recycler);

        patient = (PatientProfile) getIntent().getSerializableExtra("current user");

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        appointments = new ArrayList<>();
        doctors = new ArrayList<>();
        AppointmentsReference appointmentsReference = new AppointmentsReference();
        appointmentsQuery = appointmentsReference.where("patient_id", uid);
        backButton = findViewById(R.id.backToHomePageFromInboxButton);

        recyclerView = findViewById(R.id.requestView);
        overlay = findViewById(R.id.overlay);
        authorizeAllButton = findViewById(R.id.buttonAuthorizeAll);

        authorizeAllButton.setVisibility(View.GONE);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent patientIntent = new Intent(AbstractAppointmentActivity.this, PatientActivity.class);
                patientIntent.putExtra("current user", patient);
                startActivity(patientIntent);
            }
        });
    }

    protected void setAdapter()
    {
        setOnClickListener();
        adapter = new appointmentRecycleAdapterPatient(appointments, listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    protected void setOnClickListener()
    {
        listener = new appointmentRecycleAdapterPatient.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                showOverlay();
                showRequestCard(position);
            }
        };
    }

    protected void showOverlay() {
        overlay.setVisibility(View.VISIBLE); // Show the overlay to dim the background
    }

    protected void hideOverlay() {
        overlay.setVisibility(View.GONE); // Hide the overlay to restore the original background
    }

    protected void hideAuthorizeAllButton(){
        authorizeAllButton.setVisibility(View.GONE);
    }

    protected abstract void showRequestCard(int position);
}
