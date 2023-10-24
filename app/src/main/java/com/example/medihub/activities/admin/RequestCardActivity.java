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
import android.widget.Toast;

import com.example.medihub.R;

public class RequestCardActivity extends AppCompatActivity {

    Button show_card_button;
    View overlay; // for dimming effect

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_card);

        show_card_button = findViewById(R.id.buttonAccessCards);
        overlay = findViewById(R.id.overlay);


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
        Button confirmDone = view.findViewById(R.id.buttonConfirm);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();
        confirmDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                hideOverlay(); // Hide the overlay when the Confirm button is clicked
                Toast.makeText(RequestCardActivity.this, "confirm", Toast.LENGTH_SHORT).show();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}