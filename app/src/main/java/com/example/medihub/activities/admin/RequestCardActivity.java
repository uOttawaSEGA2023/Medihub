package com.example.medihub.activities.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medihub.R;
import com.example.medihub.models.RegistrationRequest;

public class RequestCardActivity extends AppCompatActivity {

    Button show_card_button;
    View overlay; // for dimming effect

    RegistrationRequest request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_card);

        show_card_button = findViewById(R.id.buttonAccessCards);
        overlay = findViewById(R.id.overlay);

        // Retrieve the RegistrationRequest from the intent
        request = getIntent().getParcelableExtra("request");

        show_card_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showOverlay();
                showRequestCard();
            }
        });
    }

    private void showOverlay() {
        overlay.setVisibility(View.VISIBLE); // Show the overlay to dim the background
    }

    private void hideOverlay() {
        overlay.setVisibility(View.GONE); // Hide the overlay to restore the original background
    }

    private void showRequestCard() {
        ConstraintLayout request_window = findViewById(R.id.successContraintLayout);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_card, request_window);


        if (request!=null)
        {
            TextView role = view.findViewById(R.id.preview_card_role);
            String role1;
            if (request.isPatient())
                role1 = "Patient";
            else
                role1 = "Doctor";
            role.setText(role1);

            TextView name = view.findViewById(R.id.preview_card_name);
            String name1 = request.getFirstName() + " " + request.getFirstName();
            name.setText(name1);

            TextView email = view.findViewById(R.id.preview_card_email);
            String email1 = "testingtesting@gmail.com";
            email.setText(email1);

            TextView status = view.findViewById(R.id.preview_card_status);
            String status1 = request.getStatus().toString();
            status.setText(status1);
        }

        Button authorize = view.findViewById(R.id.buttonConfirm);
        Button deny = view.findViewById(R.id.buttonDeny);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        authorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                hideOverlay(); // Hide the overlay when the Confirm button is clicked
                Toast.makeText(RequestCardActivity.this, "AUTHORIZED", Toast.LENGTH_SHORT).show();
            }
        });

        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                hideOverlay(); // Hide the overlay when the Confirm button is clicked
                Toast.makeText(RequestCardActivity.this, "DENIED", Toast.LENGTH_SHORT).show();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}