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
import com.example.medihub.adapters.appointmentRecycleAdapter;
import com.example.medihub.adapters.registrationRequestRecycleAdapter;
import com.example.medihub.database.AppointmentsReference;
import com.example.medihub.database.UsersReference;
import com.example.medihub.enums.DoctorSpecialty;
import com.example.medihub.enums.RequestStatus;
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

public class SelectAppointmentActivity extends AbstractBookingActivity{

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    View overlay; // for dimming effect

    private ArrayList<Integer> selected_specialties;
    private ArrayList<String> doctors_matching_with_specialties;
    private ArrayList<Appointment> all_open_shifts_for_booking;

    private PatientProfile currPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = currentUser.getUid();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);

        Intent intent = getIntent();
        selected_specialties = intent.getIntegerArrayListExtra("selected specialties");

        doctors_matching_with_specialties = new ArrayList<>();
        all_open_shifts_for_booking = new ArrayList<>();

        overlay = findViewById(R.id.overlay);
        recyclerView = findViewById(R.id.requestView);

//        TextView textView = findViewById(R.id.textView4);
//
//        textView.setText("Doctor");


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersReference = databaseReference.child("users");

        // Query for doctors with selected specialties
        Query doctorsQuery = usersReference.orderByChild("role").equalTo("doctor");

        doctorsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot doctorSnapshot : dataSnapshot.getChildren()) {
                    // Get the doctor's specialties
                    DataSnapshot specialtiesSnapshot = doctorSnapshot.child("specialties");

                    // Check if specialties exist
                    if (specialtiesSnapshot.exists()) {
                        // Iterate through specialties
                        for (DataSnapshot specialtySnapshot : specialtiesSnapshot.getChildren()) {
                            String specialtyString = specialtySnapshot.getValue(String.class);

                            // Convert specialty from String to DoctorSpeciality enum
                            DoctorSpecialty specialty = DoctorSpecialty.valueOf(specialtyString);

                            // Check if the doctor has the selected specialty
                            if (selected_specialties.contains(specialty.ordinal())) {
//                                Log.d("Doctor Specialty", "Doctor ID: " + doctorSnapshot.getKey() + ", Specialty: " + specialty);
                                // add doctor id to arraylist
                                doctors_matching_with_specialties.add(doctorSnapshot.getKey());
                            }
                        }
                    }
                }

                // parsing available appointments
                for (String doctor_id : doctors_matching_with_specialties)
                {
                    DatabaseReference shiftsRef = database.getReference("shifts");

                    shiftsRef.orderByChild("doctor_id").equalTo(doctor_id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot shiftSnapshot : dataSnapshot.getChildren()) {
                                // Access shift data for the specific doctor
                                String shift_id = shiftSnapshot.getKey();
                                LocalDateTime start = LocalDateTime.parse(shiftSnapshot.child("startDate").getValue(String.class));
                                LocalDateTime end = LocalDateTime.parse(shiftSnapshot.child("endDate").getValue(String.class));
                                Shift shift = new Shift(doctor_id, start, end);
                                LocalDateTime currentDateTime = start;
                                while (currentDateTime.isBefore(end)) {
                                    LocalDateTime blockEndDateTime = currentDateTime.plusMinutes(30);

                                    // Create an Appointment object for the current 30-minute block
                                    Appointment app = new Appointment(currentUser.getUid(), doctor_id, shift_id, RequestStatus.pending, blockEndDateTime);

                                    // Add the appointment to the list
                                    all_open_shifts_for_booking.add(app);

                                    // Move to the next 30-minute block
                                    currentDateTime = blockEndDateTime;
                                }

                            }
                            // removing appointments that have already been booked / are pending
                            DatabaseReference appointmentsRef = database.getReference("appointments");

                            Iterator<Appointment> iterator = all_open_shifts_for_booking.iterator();

                            while (iterator.hasNext())
                            {
                                Appointment app = iterator.next();
                                appointmentsRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot appointmentSnapshot : dataSnapshot.getChildren()) {
                                            // Get appointment details
                                            String appointmentId = appointmentSnapshot.getKey();
                                            String doctorId = appointmentSnapshot.child("doctor_id").getValue(String.class);
                                            String patientId = appointmentSnapshot.child("patient_id").getValue(String.class);
                                            String shiftId = appointmentSnapshot.child("shift_id").getValue(String.class);
                                            String startDate = appointmentSnapshot.child("startDate").getValue(String.class);
                                            String status = appointmentSnapshot.child("status").getValue(String.class);

                                            LocalDateTime start_time = LocalDateTime.parse(startDate);

                                            // check if current app is already booked
                                            if (app.localStartDate().isAfter(start_time) && app.localStartDate().isBefore(start_time.plusMinutes(30)))
                                            {
                                                iterator.remove();
                                            }

                                        }

                                        appointments = all_open_shifts_for_booking;
                                        Collections.sort(appointments);

                                        // doctor parsing
                                        for (int i = 0; i < appointments.size(); i++) {
                                            String doctor_id = appointments.get(i).getDoctor_id();
                                            DatabaseReference doctorsRef = FirebaseDatabase.getInstance().getReference().child("users").child(doctor_id);

                                            doctorsRef.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    // Retrieve doctor data
                                                    if (dataSnapshot.exists()) {
                                                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                                                        String lastName = dataSnapshot.child("lastName").getValue(String.class);

                                                        PatientProfile currDoc = new PatientProfile(firstName, lastName, null, null, null, null);

                                                        patients.add(currDoc);
                                                        Log.d("asdasd", patients.toString());

                                                        // Check if this is the last doctor data retrieval
                                                        if (patients.size() == appointments.size()) {
                                                            // All data retrieved, update the adapter

                                                            setAdapter();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    // Handle error
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        System.err.println("Error getting appointments: " + databaseError.getMessage());
                                    }
                                });
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.err.println("Error getting shifts: " + databaseError.getMessage());
                        }
                    });
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase", "loadPost:onCancelled", databaseError.toException());
            }
        });


        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Retrieve user data
                if (dataSnapshot.exists()) {
                    String address = dataSnapshot.child("address").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                    String healthCardNumber = dataSnapshot.child("healthCardNumber").getValue(String.class);
                    String lastName = dataSnapshot.child("lastName").getValue(String.class);
                    String phoneNumber = dataSnapshot.child("phoneNumber").getValue(String.class);
                    String role = dataSnapshot.child("role").getValue(String.class);

                    currPatient = new PatientProfile(firstName, lastName, address, phoneNumber, email, healthCardNumber);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
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

        denyButton.setText("       Back       ");
        authorizeButton.setText("Book Appointment");
//        authorizeButton.setVisibility(View.GONE);

        Appointment appointment = appointments.get(position);
        PatientProfile patient = currPatient;

        if (appointment != null && patient != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            TextView statusText = view.findViewById(R.id.appointment_card_status);
            statusText.setText("Status: Available");
            TextView dateText = view.findViewById(R.id.appointment_card_date);

            TextView nameText = view.findViewById(R.id.appointment_card_name);
            TextView emailText = view.findViewById(R.id.appointment_card_email);
            TextView phoneText = view.findViewById(R.id.appointment_card_phone);
            TextView addressText = view.findViewById(R.id.appointment_card_address);
            TextView healthCardText = view.findViewById(R.id.appointment_card_health_card);

//            statusText.append(appointment.getStatus().toString());
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
                // retrieve the selected appointment
                Appointment selectedAppointment = appointments.get(position);

                // Get a reference to the "appointments" node in the database
                DatabaseReference appointmentsRef = database.getReference("appointments");

                // Push the new appointment to the database
                DatabaseReference newAppointmentRef = appointmentsRef.push();
                newAppointmentRef.setValue(selectedAppointment);

                alertDialog.dismiss();
                hideOverlay();
                Toast.makeText(getApplicationContext(), "Appointment Request Sent", Toast.LENGTH_LONG).show();
            }
        });

        alertDialog.show();
    }
}