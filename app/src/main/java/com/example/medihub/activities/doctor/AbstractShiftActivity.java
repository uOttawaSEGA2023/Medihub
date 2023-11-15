package com.example.medihub.activities.doctor;

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
import com.example.medihub.adapters.shiftAdapter;
import com.example.medihub.database.AppointmentsReference;
import com.example.medihub.database.ShiftsReference;
import com.example.medihub.database.UsersReference;
import com.example.medihub.enums.RequestStatus;
import com.example.medihub.models.Appointment;
import com.example.medihub.models.DoctorProfile;
import com.example.medihub.models.PatientProfile;
import com.example.medihub.models.Shift;
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

public abstract class AbstractShiftActivity extends AppCompatActivity {
    View overlay;

    protected ArrayList<Shift> shifts;
    protected RecyclerView recyclerView;
    protected shiftAdapter.RecyclerViewClickListener listener;
    protected Button backButton;
    protected UserProfile doctor;
    protected Query shiftQuery;
    protected int totalChildren = 0;
    shiftAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_recycler);

        doctor = (DoctorProfile) getIntent().getSerializableExtra("current user");

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        shifts = new ArrayList<>();
        ShiftsReference shiftsReference = new ShiftsReference();
        shiftQuery = shiftsReference.where("doctor_id", uid);

        recyclerView = findViewById(R.id.requestView);
        overlay = findViewById(R.id.overlay);
        Button approve = findViewById(R.id.buttonAuthorizeAll);
        approve.setVisibility(View.GONE);

        backButton = findViewById(R.id.backToHomePageFromInboxButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent = new Intent(AbstractShiftActivity.this, DoctorActivity.class);
                backIntent.putExtra("current user", doctor);
                startActivity(backIntent);
            }
        });
    }

    protected void setAdapter()
    {
        setOnClickListener();
        adapter = new shiftAdapter(shifts, listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    protected void setOnClickListener()
    {
        listener = new shiftAdapter.RecyclerViewClickListener() {
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

    protected abstract void showRequestCard(int position);
}
