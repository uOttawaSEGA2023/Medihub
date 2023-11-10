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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.medihub.R;
import com.example.medihub.activities.admin.AdminActivity;
import com.example.medihub.activities.admin.PendingRequestsActivity;
import com.example.medihub.adapters.appointmentRecycleAdapter;
import com.example.medihub.adapters.recycleAdapter;
import com.example.medihub.database.AppointmentsReference;
import com.example.medihub.database.UsersReference;
import com.example.medihub.enums.RequestStatus;
import com.example.medihub.models.Appointment;
import com.example.medihub.models.DoctorProfile;
import com.example.medihub.models.PatientProfile;
import com.example.medihub.models.RegistrationRequest;
import com.example.medihub.models.UserProfile;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class UpcomingAppointmentsActivity extends AbstractAppointmentsActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UsersReference usersReference = new UsersReference();

        upcomingAppointmentsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointments.clear();

                if (snapshot.exists()) {
                    totalChildren = (int)snapshot.getChildrenCount();

                    for (DataSnapshot upcomingAppointment : snapshot.getChildren()) {
                        Appointment appointment = upcomingAppointment.getValue(Appointment.class);

                        // check if it's a valid upcoming appointment
                        if (appointment != null && appointment.localStartDate().isAfter(LocalDateTime.now()) && appointment.getStatus() == RequestStatus.approved) {
                            DatabaseReference patientRef = usersReference.get(appointment.getPatient_id());

                            patientRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    totalChildren--;

                                    if (snapshot.exists()) {
                                        appointment.setKey(upcomingAppointment.getKey());
                                        appointments.add(appointment);

                                        PatientProfile patient = snapshot.getValue(PatientProfile.class);
                                        patient.setKey(snapshot.getKey());
                                        patients.add(patient);
                                    }

                                    if (totalChildren == 0)
                                        setAdapter();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {
                            totalChildren--;
                            if (totalChildren == 0)
                                setAdapter();
                        }
                    }
                } else {
                    setAdapter();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void showRequestCard(int position) {
        ConstraintLayout request_window = findViewById(R.id.successConstraintLayout);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_appointment_card, request_window);

        Button authorizeButton = view.findViewById(R.id.buttonConfirm);
        Button denyButton = view.findViewById(R.id.buttonDeny);

        denyButton.setText("Cancel Appointment");
        authorizeButton.setVisibility(View.GONE);

        Appointment appointment = appointments.get(position);
        PatientProfile patient = patients.get(position);

        if (appointment != null && patient != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            TextView statusText = view.findViewById(R.id.appointment_card_status);
            TextView dateText = view.findViewById(R.id.appointment_card_date);

            TextView nameText = view.findViewById(R.id.appointment_card_name);
            TextView emailText = view.findViewById(R.id.appointment_card_email);
            TextView phoneText = view.findViewById(R.id.appointment_card_phone);
            TextView addressText = view.findViewById(R.id.appointment_card_address);
            TextView healthCardText = view.findViewById(R.id.appointment_card_health_card);

            statusText.append(appointment.getStatus().toString());
            dateText.append(appointment.localStartDate().format(formatter));
            nameText.append(patient.getFirstName() + " " + patient.getLastName());
            emailText.append(patient.getEmail());
            phoneText.append(patient.getPhoneNumber());
            addressText.append(patient.getAddress());
            healthCardText.append(patient.getHealthCardNumber());
        }

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

        // TODO: implement cancel functionality

        alertDialog.show();
    }
}