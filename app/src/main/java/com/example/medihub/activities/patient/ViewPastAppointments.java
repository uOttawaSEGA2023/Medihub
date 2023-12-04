package com.example.medihub.activities.patient;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.time.temporal.ChronoUnit;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.medihub.R;
import com.example.medihub.activities.doctor.AbstractShiftActivity;
import com.google.firebase.database.Query;
import com.example.medihub.activities.admin.AdminActivity;
import com.example.medihub.activities.admin.PendingRequestsActivity;
import com.example.medihub.database.AppointmentsReference;
import com.example.medihub.database.ShiftsReference;
import com.example.medihub.database.UsersReference;
import com.example.medihub.enums.RequestStatus;
import com.example.medihub.models.Appointment;
import com.example.medihub.models.PatientProfile;
import com.example.medihub.models.Shift;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.BlockingDeque;

public class ViewPastAppointments extends AbstractAppointmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appointmentsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointments.clear();

                if (snapshot.exists()) {
                    totalChildren = (int)snapshot.getChildrenCount();

                    // fetch appointments
                    for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                        Appointment appointment = appointmentSnapshot.getValue(Appointment.class);

                        // check if it's not null
                        if (appointment != null) {

                            //CURRENT TIME & DATE
                            LocalDateTime currentDateTime = LocalDateTime.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                            String formattedDateTime = currentDateTime.format(formatter);

                            LocalDateTime dateTime1 = LocalDateTime.parse(formattedDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                            LocalDateTime dateTime2 = LocalDateTime.parse(appointment.getStartDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

                            Log.d("Current", dateTime1.toString());
                            Log.d("Appointment", dateTime2.toString());

                            if (dateTime2.isBefore(dateTime1)) {

                                appointment.setKey(appointmentSnapshot.getKey());
                                appointments.add(appointment);

                            }

                        }

                        // check if all appointments have been fetched
                        totalChildren--;
                        if (totalChildren == 0) {
                            Collections.sort(appointments);
                            setAdapter();
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

    }

    @Override
    protected void showRequestCard(int position) {
        ConstraintLayout request_window = findViewById(R.id.successConstraintLayout);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_appointment_card_patient, request_window);

        Button one = view.findViewById(R.id.button1Star);
        Button two = view.findViewById(R.id.button2Star);
        Button three = view.findViewById(R.id.button3Star);
        Button four = view.findViewById(R.id.button4Star);
        Button five = view.findViewById(R.id.button5Star);

        Appointment appointment = appointments.get(position);

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Rated 1 star!", Toast.LENGTH_LONG).show();
                appointment.setPatientRating(1);

            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Rated 2 stars!", Toast.LENGTH_LONG).show();
                appointment.setPatientRating(2);

            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Rated 3 stars!", Toast.LENGTH_LONG).show();
                appointment.setPatientRating(3);

            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Rated 4 stars!", Toast.LENGTH_LONG).show();
                appointment.setPatientRating(4);

            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Rated 5 stars!", Toast.LENGTH_LONG).show();
                appointment.setPatientRating(5);

            }
        });


        Button delete = view.findViewById(R.id.buttonDelete);
        delete.setVisibility(View.GONE);

        if (appointment != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            TextView startDate = view.findViewById(R.id.startDate);
            TextView endDate = view.findViewById(R.id.endDate);

            startDate.append(appointment.localStartDate().format(formatter));
            endDate.append(appointment.localEndDate().format(formatter));

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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LocalDateTime currentDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                String formattedDateTime = currentDateTime.format(formatter);

                LocalDateTime dateTime1 = LocalDateTime.parse(formattedDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                LocalDateTime dateTime2 = LocalDateTime.parse(appointment.getStartDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

                boolean isSameDate = dateTime1.toLocalDate().isEqual(dateTime2.toLocalDate());

                // Compare the time part if the date is the same
                long minutesDifference = Math.abs(dateTime1.until(dateTime2, java.time.temporal.ChronoUnit.MINUTES));

                // Check if the time is within 60 minutes
                if (minutesDifference <= 60 && isSameDate == true) {

                    Toast.makeText(getApplicationContext(), "Appointment within 60 minutes. Can't be deleted!", Toast.LENGTH_LONG).show();

                } else {

                    AppointmentsReference appointmentReference = new AppointmentsReference();
                    AppointmentsReference appointmentsReference = new AppointmentsReference();

                    Query appointmentsQuery = appointmentsReference.where("appointment_id", appointment.getKey());
                    appointmentsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getChildrenCount() == 0) {
                                appointmentReference.delete(appointment.getKey());
                                Toast.makeText(getApplicationContext(), "Appointment deleted", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Appointment can't be deleted (has appointments)", Toast.LENGTH_LONG).show();
                            }

                            alertDialog.dismiss();
                            hideOverlay();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }



            }

        });

        alertDialog.show();
    }

}
