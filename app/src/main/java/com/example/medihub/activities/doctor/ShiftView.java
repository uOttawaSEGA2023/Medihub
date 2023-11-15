package com.example.medihub.activities.doctor;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.medihub.R;
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

public class ShiftView extends AbstractShiftActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shiftQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shifts.clear();

                if (snapshot.exists()) {
                    totalChildren = (int)snapshot.getChildrenCount();

                    // fetch shifts
                    for (DataSnapshot shiftSnapshot : snapshot.getChildren()) {
                        Shift shift = shiftSnapshot.getValue(Shift.class);

                        // check if it's not null
                        if (shift != null) {
                            // add shift to list
                            shift.setKey(shiftSnapshot.getKey());
                            shifts.add(shift);
                        }

                        // check if all appointments have been fetched
                        totalChildren--;
                        if (totalChildren == 0) {
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
        View view = LayoutInflater.from(this).inflate(R.layout.activity_shift_card, request_window);

        Button delete = view.findViewById(R.id.buttonDelete);

        Shift shift = shifts.get(position);

        if (shift != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            TextView startDate = view.findViewById(R.id.startDate);
            TextView endDate = view.findViewById(R.id.endDate);

            startDate.append(shift.localStartDate().format(formatter));
            endDate.append(shift.localEndDate().format(formatter));

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
                ShiftsReference shiftReference = new ShiftsReference();

                shiftReference.delete(shift.getKey());

                alertDialog.dismiss();
                hideOverlay();
                Toast.makeText(getApplicationContext(), "Shift deleted", Toast.LENGTH_LONG).show();
            }
        });

        alertDialog.show();
    }
}
