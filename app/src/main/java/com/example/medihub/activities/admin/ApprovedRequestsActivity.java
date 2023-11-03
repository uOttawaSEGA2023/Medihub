package com.example.medihub.activities.admin;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.medihub.R;
import com.example.medihub.adapters.recycleAdapter;
import com.example.medihub.database.RegistrationRequestsReference;
import com.example.medihub.enums.RequestStatus;
import com.example.medihub.models.RegistrationRequest;
import com.example.medihub.models.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ApprovedRequestsActivity extends AppCompatActivity
{

    Button backButton;
    View overlay; // for dimming effect

    private ArrayList<RegistrationRequest> pendingRequests;
    private RecyclerView recyclerView;
    private recycleAdapter.RecyclerViewClickListener listener;
    private UserProfile admin;
    private Query pendingRequestsQuery;
    private FirebaseAuth mAuth;

    private FirebaseDatabase firebaseDB;

    recycleAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_recycler);

        pendingRequests = new ArrayList<>();
        RegistrationRequestsReference registrationRequestsReference = new RegistrationRequestsReference();
        pendingRequestsQuery = registrationRequestsReference.where("status", RequestStatus.approved.toString());

        mAuth = FirebaseAuth.getInstance();

        pendingRequestsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pendingRequests.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot requestSnapshot : snapshot.getChildren()) {
                        RegistrationRequest rq = requestSnapshot.getValue(RegistrationRequest.class);

                        if (rq != null) {
                            rq.setKey(requestSnapshot.getKey());
                            pendingRequests.add(rq);
                        }
                    }
                }
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backButton = findViewById(R.id.backToHomePageFromInboxButton);
        overlay = findViewById(R.id.overlay);

        // Retrieve current user from intent
        admin = (UserProfile) getIntent().getSerializableExtra("current user");

        recyclerView = findViewById(R.id.requestView);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent = new Intent(ApprovedRequestsActivity.this, AdminActivity.class);
                backIntent.putExtra("current user", admin);
                startActivity(backIntent);
            }
        });
    }

    private void setAdapter()
    {
        setOnClickListener();
        adapter = new recycleAdapter(pendingRequests, listener);
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
        View view = LayoutInflater.from(this).inflate(R.layout.activity_request_card, request_window);

        RegistrationRequest rq = pendingRequests.get(position);
        Log.i("RQ KEY:", rq.getKey());

        if (rq != null)
        {
            TextView role = view.findViewById(R.id.preview_card_role);
            String role1;
            if (rq.isPatient()) {
                role1 = "Patient";

                TextView extra1 = view.findViewById(R.id.preview_card_extra);
                TextView extra2 = view.findViewById(R.id.preview_card_extra2);

                extra1.setText("Health Card Number: " + rq.getHealthCardNumber());
                extra2.setText("");
            }
            else {
                role1 = "Doctor";

                TextView extra1 = view.findViewById(R.id.preview_card_extra);
                TextView extra2 = view.findViewById(R.id.preview_card_extra2);

                extra1.setText("Employee Number: " + rq.getEmployeeNumber());
                String specString = rq.getSpecialties().toString();
                extra2.setText("Specialties: " + specString.substring(1,specString.length()-1));
            }
            role.append(role1);

            TextView name = view.findViewById(R.id.preview_card_name);
            String name1 = rq.getFirstName() + " " + rq.getLastName();
            name.append(name1);

            TextView email = view.findViewById(R.id.preview_card_email);
            String email1 = rq.getEmail();
            email.append(email1);

            TextView phone = view.findViewById(R.id.preview_card_phone);
            String phone1 = rq.getPhoneNumber();
            phone.append(phone1);

            TextView address = view.findViewById(R.id.preview_card_address);
            String address1 = rq.getAddress();
            address.append(address1);

            setStatus(position, view);
        }

        Button authorize = view.findViewById(R.id.buttonConfirm);
        Button deny = view.findViewById(R.id.buttonDeny);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        authorize.setVisibility(View.GONE);
        deny.setVisibility(View.GONE);


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

    private void setStatus(int position, View view)
    {
        if (pendingRequests.get(position).getStatus()!=null) {
            TextView status = view.findViewById(R.id.preview_card_status);
            String status1 = pendingRequests.get(position).getStatus().toString();
            status.append(status1);
        }
    }
}