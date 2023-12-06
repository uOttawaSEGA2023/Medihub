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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medihub.R;
import com.example.medihub.activities.doctor.AbstractAppointmentsActivity;
import com.example.medihub.activities.doctor.DoctorActivity;
import com.example.medihub.adapters.appointmentRecycleAdapter;
import com.example.medihub.adapters.registrationRequestRecycleAdapter;
import com.example.medihub.database.AppointmentsReference;
import com.example.medihub.database.ShiftsReference;
import com.example.medihub.database.UsersReference;
import com.example.medihub.enums.DoctorSpecialty;
import com.example.medihub.enums.RequestStatus;
import com.example.medihub.enums.UserRole;
import com.example.medihub.models.Appointment;
import com.example.medihub.models.DoctorProfile;
import com.example.medihub.models.PatientProfile;
import com.example.medihub.models.RegistrationRequest;
import com.example.medihub.models.Shift;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SelectAppointmentActivity extends AbstractBookingActivity{

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    View overlay; // for dimming effect

    private String selected_specialty;
    private ArrayList<String> doctors_matching_with_specialties;
    private ArrayList<String> all_valid_shifts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = currentUser.getUid();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);

        Intent intent = getIntent();
        selected_specialty = intent.getStringExtra("selected specialty");

        doctors_matching_with_specialties = new ArrayList<>();
        all_valid_shifts = new ArrayList<>();

        overlay = findViewById(R.id.overlay);
        recyclerView = findViewById(R.id.requestView);

//        TextView textView = findViewById(R.id.textView4);
//
//        textView.setText("Doctor");


        UsersReference usersReference = new UsersReference();

        // Query for doctors with selected specialties
        Query doctorsQuery = usersReference.where("role", UserRole.doctor.toString());

        doctorsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int numDoctors = (int)dataSnapshot.getChildrenCount();

                // fetch all doctors with specified specialties
                for (DataSnapshot doctorSnapshot : dataSnapshot.getChildren()) {
                    numDoctors--;

                    // Get the doctor's specialties
                    DataSnapshot specialtiesSnapshot = doctorSnapshot.child("specialties");

                    // Check if specialties exist
                    if (specialtiesSnapshot.exists()) {
                        // Iterate through specialties
                        for (DataSnapshot specialtySnapshot : specialtiesSnapshot.getChildren()) {
                            String specialtyString = specialtySnapshot.getValue(String.class).toString().replaceAll("_", " ");
                            // Check if the doctor has the selected specialty
                            if (selected_specialty.equals(specialtyString)) {
//                                Log.d("Doctor Specialty", "Doctor ID: " + doctorSnapshot.getKey() + ", Specialty: " + specialty);
                                // add doctor id to arraylist
                                doctors_matching_with_specialties.add(doctorSnapshot.getKey());
                                break;
                            }
                        }
                    }

                    // Parse all available appointments
                    if (numDoctors == 0) {
                        parseShifts();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase", "loadPost:onCancelled", databaseError.toException());
            }
        });

        hideAuthorizeAllButton();
    }

    public void parseShifts() {
        ShiftsReference shiftsRef = new ShiftsReference();
        for (String doctor_id : doctors_matching_with_specialties)
        {
            shiftsRef.where("doctor_id", doctor_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int numShifts = (int)snapshot.getChildrenCount();

                    for (DataSnapshot shiftSnapshot : snapshot.getChildren()) {
                        numShifts--;

                        Shift shift = shiftSnapshot.getValue(Shift.class);
                        if (LocalDateTime.now().isAfter(shift.localStartDate())) {
                            if (numShifts == 0) {
                                parseAppointments(doctor_id);
                            }
                            continue;
                        }

                        all_valid_shifts.add(shiftSnapshot.getKey());

                        if (numShifts == 0) {
                            parseAppointments(doctor_id);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void parseAppointments(String doctor_id) {
        AppointmentsReference appointmentsReference = new AppointmentsReference();
        UsersReference usersReference = new UsersReference();

        usersReference.get(doctor_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                DoctorProfile doctor = snapshot1.getValue(DoctorProfile.class);
                doctor.setKey(snapshot1.getKey());

                for (String shift_id : all_valid_shifts) {
                    appointmentsReference.where("shift_id", shift_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int numAppointments = (int)snapshot.getChildrenCount();

                            for (DataSnapshot appointmentSnapshot : snapshot.getChildren()) {
                                numAppointments--;

                                Appointment app = appointmentSnapshot.getValue(Appointment.class);
                                Log.i("app: ", String.valueOf(app.isBooked()));
                                app.setKey(appointmentSnapshot.getKey());
                                if (app.isBooked()) {
                                    if (numAppointments == 0) {
                                        Collections.sort(appointments);
                                        setAdapter();
                                    }
                                    continue;
                                }

                                appointments.add(app);
                                doctors.add(doctor);

                                if (numAppointments == 0) {
                                    Collections.sort(appointments);
                                    setAdapter();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
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

        denyButton.setText("       Back       ");
        authorizeButton.setText("Book Appointment");
//        authorizeButton.setVisibility(View.GONE);

        Appointment appointment = appointments.get(position);
        DoctorProfile doctor = doctors.get(position);

        if (appointment != null && doctor != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            TextView patientInfoText = view.findViewById(R.id.title2);
            patientInfoText.setText("DOCTOR INFO");
            TextView statusText = view.findViewById(R.id.appointment_card_status);
            statusText.setText("Status: Available");
            TextView dateText = view.findViewById(R.id.appointment_card_date);

            TextView nameText = view.findViewById(R.id.appointment_card_name);
            TextView emailText = view.findViewById(R.id.appointment_card_email);
            TextView phoneText = view.findViewById(R.id.appointment_card_phone);
            TextView addressText = view.findViewById(R.id.appointment_card_address);
            TextView healthCardText = view.findViewById(R.id.appointment_card_health_card);
            String specString = doctor.getSpecialties().toString();
            healthCardText.setText("Specialties: " + specString.substring(1,specString.length()-1));

//            statusText.append(appointment.getStatus().toString());
            dateText.append(appointment.localStartDate().format(formatter));
            nameText.append(doctor.getFirstName() + " " + doctor.getLastName());
            emailText.append(doctor.getEmail());
            phoneText.append(doctor.getPhoneNumber());
            addressText.append(doctor.getAddress());
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

        denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                alertDialog.dismiss();
                hideOverlay();
            }
        });

        authorizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppointmentsReference appointmentsReference = new AppointmentsReference();

                // retrieve the selected appointment
                Appointment selectedAppointment = appointments.get(position);

                appointmentsReference.patch(selectedAppointment.getKey(), new HashMap<String, Object>(){{
                    put("booked", true);
                    put("patient_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                }});

                alertDialog.dismiss();
                hideOverlay();

                Toast.makeText(getApplicationContext(), "Successfully booked appointment", Toast.LENGTH_LONG);
            }
        });

        alertDialog.show();
    }
}