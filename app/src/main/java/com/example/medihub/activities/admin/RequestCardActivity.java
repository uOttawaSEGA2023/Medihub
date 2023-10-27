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
import android.widget.Toast;

import com.example.medihub.R;
import com.example.medihub.enums.RegistrationStatus;
import com.example.medihub.models.RegistrationRequest;
import com.example.medihub.models.UserProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestCardActivity extends AppCompatActivity
{

    Button backButton;
    View overlay; // for dimming effect

    private ArrayList<RegistrationRequest> pendingRequests;
    private RecyclerView recyclerView;
    private recycleAdapter.RecyclerViewClickListener listener;
    private UserProfile admin;
    private DatabaseReference dbReference;
    private Query pendingRequestsQuery;

    recycleAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_recycler);

        pendingRequests = new ArrayList<>();
        dbReference = FirebaseDatabase.getInstance().getReference();
        pendingRequestsQuery = dbReference.child("registration_requests").orderByChild("status").equalTo(RegistrationStatus.pending.toString());

        pendingRequestsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot requestSnapshot : snapshot.getChildren()) {
                    RegistrationRequest rq = requestSnapshot.getValue(RegistrationRequest.class);
                    pendingRequests.add(rq);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backButton = findViewById(R.id.backToHomePageFromInboxButton);
        overlay = findViewById(R.id.overlay);

        // BELOW IS FOR TESTING
//        request = new ArrayList<RegistrationRequest>();
//        request.add(new RegistrationRequest(true , "fefty", "wacky", "A1A 1A1",
//                "123456789", "123456789", "123", new ArrayList<DoctorSpecialty>()));
//        request.add(new RegistrationRequest(true));

        // Retrieve current user from intent
        admin = (UserProfile) getIntent().getSerializableExtra("current user");

        recyclerView = findViewById(R.id.requestView);

        if (pendingRequests !=null)
        {
            setAdapter();
        }
        else
            Log.d("regis req", "request arraylist is null");


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent = new Intent(RequestCardActivity.this, AdminActivity.class);
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
        View view = LayoutInflater.from(this).inflate(R.layout.activity_card, request_window);


        if (pendingRequests.get(position)!=null)
        {
            TextView role = view.findViewById(R.id.preview_card_role);
            String role1;
            if (pendingRequests.get(position).isPatient())
                role1 = "Patient";
            else
                role1 = "Doctor";
            role.append(role1);

            TextView name = view.findViewById(R.id.preview_card_name);
            String name1 = pendingRequests.get(position).getFirstName() + " " + pendingRequests.get(position).getLastName();
            name.append(name1);

            TextView email = view.findViewById(R.id.preview_card_email);
            String email1 = "testingtesting@gmail.com";
            email.append(email1);

            setStatus(position, view);
        }

        Button authorize = view.findViewById(R.id.buttonConfirm);
        Button deny = view.findViewById(R.id.buttonDeny);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();



        authorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pendingRequests.get(position).approve();
                adapter.updateStatus(position, RegistrationStatus.approved);
                alertDialog.dismiss();
                hideOverlay(); // Hide the overlay when the Confirm button is clicked
                Toast.makeText(RequestCardActivity.this, "Approved Registration", Toast.LENGTH_SHORT).show();
            }
        });

        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pendingRequests.get(position).getStatus()!=RegistrationStatus.approved)
                {
                    adapter.updateStatus(position, RegistrationStatus.declined);
                    pendingRequests.get(position).decline();
                    alertDialog.dismiss();
                    hideOverlay(); // Hide the overlay when the Confirm button is clicked
                    Toast.makeText(RequestCardActivity.this, "Denied Registration", Toast.LENGTH_SHORT).show();
                }
                else // DON'T ALLOW DENYING APPROVED REQUESTS
                {
                    alertDialog.dismiss();
                    hideOverlay(); // Hide the overlay when the Confirm button is clicked
                    Toast.makeText(RequestCardActivity.this, "CANNOT DENY APPROVED REQUEST", Toast.LENGTH_SHORT).show();
                }
            }
        });

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