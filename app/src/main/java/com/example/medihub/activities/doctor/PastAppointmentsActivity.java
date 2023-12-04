package com.example.medihub.activities.doctor;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.medihub.R;
import com.example.medihub.database.UsersReference;
import com.example.medihub.enums.RequestStatus;
import com.example.medihub.models.Appointment;
import com.example.medihub.models.PatientProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

public class PastAppointmentsActivity extends AbstractAppointmentsActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UsersReference usersReference = new UsersReference();

        appointmentsQuery.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointments.clear();

                if (snapshot.exists()) {
                    totalChildren = (int)snapshot.getChildrenCount();

                    // fetch appointments
                    for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                        Appointment appointment = appointmentSnapshot.getValue(Appointment.class);

                        // check if it's a valid past appointment
                        if (appointment != null && appointment.isBooked() && LocalDateTime.now().isAfter(appointment.localEndDate()) && appointment.getStatus() == RequestStatus.approved) {
                            // add appointment to list
                            appointment.setKey(appointmentSnapshot.getKey());
                            appointments.add(appointment);
                        }

                        // check if all appointments have been fetched
                        totalChildren--;
                        if (totalChildren == 0) {
                            totalChildren = appointments.size();

                            if (totalChildren == 0) {
                                setAdapter();
                                return;
                            }

                            // sort appointments by closest date to today
                            Collections.sort(appointments);

                            // fetch patients from appointments
                            for (Appointment appointment1 : appointments) {
                                DatabaseReference patientRef = usersReference.get(appointment1.getPatient_id());

                                patientRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot patientSnapshot) {
                                        if (snapshot.exists()) {
                                            PatientProfile patient = patientSnapshot.getValue(PatientProfile.class);
                                            patient.setKey(patientSnapshot.getKey());
                                            patients.add(patient);
                                        }

                                        totalChildren--;
                                        if (totalChildren == 0) {
                                            setAdapter();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                            break;
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

        hideAuthorizeAllButton();
    }

    @Override
    protected void showRequestCard(int position) {
        ConstraintLayout request_window = findViewById(R.id.successConstraintLayout);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_appointment_card, request_window);

        Button authorizeButton = view.findViewById(R.id.buttonConfirm);
        Button denyButton = view.findViewById(R.id.buttonDeny);

        authorizeButton.setVisibility(View.GONE);
        denyButton.setVisibility(View.GONE);

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

        alertDialog.show();
    }
}
