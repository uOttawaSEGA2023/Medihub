package com.example.medihub.activities.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.medihub.R;
import com.example.medihub.activities.admin.AdminActivity;
import com.example.medihub.activities.admin.PendingRequestsActivity;
import com.example.medihub.adapters.appointmentRecycleAdapter;
import com.example.medihub.adapters.recycleAdapter;
import com.example.medihub.database.AppointmentsReference;
import com.example.medihub.enums.RequestStatus;
import com.example.medihub.models.Appointment;
import com.example.medihub.models.DoctorProfile;
import com.example.medihub.models.RegistrationRequest;
import com.example.medihub.models.UserProfile;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class UpcomingAppointmentsActivity extends AppCompatActivity {
    Button backButton;
    View overlay;

    private ArrayList<Appointment> appointments;
    private RecyclerView recyclerView;
    private recycleAdapter.RecyclerViewClickListener listener;
    private UserProfile doctor;
    private Query upcomingAppointmentsQuery;
    appointmentRecycleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_recycler);

        doctor = (DoctorProfile) getIntent().getSerializableExtra("current user");

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        appointments = new ArrayList<>();
        AppointmentsReference appointmentsReference = new AppointmentsReference();
        upcomingAppointmentsQuery = appointmentsReference.where("doctor_id", uid);

        upcomingAppointmentsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointments.clear();

                if (snapshot.exists()) {
                    for (DataSnapshot upcomingAppointment : snapshot.getChildren()) {
                        Appointment appointment = upcomingAppointment.getValue(Appointment.class);

                        if (appointment != null && appointment.localStartDate().isAfter(LocalDateTime.now()) && appointment.getStatus() == RequestStatus.approved) {
                            appointment.setKey(upcomingAppointment.getKey());
                            appointments.add(appointment);
                        }
                    }
                }

                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView = findViewById(R.id.requestView);
        overlay = findViewById(R.id.overlay);
        backButton = findViewById(R.id.backToHomePageFromInboxButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent = new Intent(UpcomingAppointmentsActivity.this, DoctorActivity.class);
                backIntent.putExtra("current user", doctor);
                startActivity(backIntent);
            }
        });
    }

    private void setAdapter()
    {
        setOnClickListener();
        adapter = new appointmentRecycleAdapter(appointments, listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setOnClickListener()
    {
        listener = new recycleAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                showOverlay();
                showRequestCard(position);
            }
        };
    }

    private void showOverlay() {
        overlay.setVisibility(View.VISIBLE); // Show the overlay to dim the background
    }

    private void hideOverlay() {
        overlay.setVisibility(View.GONE); // Hide the overlay to restore the original background
    }

    private void showRequestCard(int position) {
        ConstraintLayout request_window = findViewById(R.id.successContraintLayout);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_appointment_card, request_window);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                hideOverlay(); // Hide the overlay when the dialog is canceled
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();

        Appointment appointment = appointments.get(position);
        Log.i("Appointment Key:", appointment.getKey());
    }
}