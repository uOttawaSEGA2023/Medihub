package com.example.medihub.activities.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.medihub.R;
import com.example.medihub.activities.registrations.LoginActivity;
import com.example.medihub.models.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class AdminActivity extends AppCompatActivity {
    private UserProfile user;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDB;

    // UI elements
    private Button logoutButton;
    private Button approve;
    private Button pending;
    private Button declined;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        user = (UserProfile) getIntent().getSerializableExtra("current user");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseDatabase.getInstance();

        if (user == null) {
            firebaseAuth.signOut();

            Intent loginActivity = new Intent(this, LoginActivity.class);
            loginActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginActivity);
            finish();
        }

        logoutButton = findViewById(R.id.logoutButton);
        approve = findViewById(R.id.btnApproved);
        pending = findViewById(R.id.btnPending);
        declined = findViewById(R.id.btnDeclined);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inboxIntent = new Intent(AdminActivity.this, PendingRequestsActivity.class);
                inboxIntent.putExtra("current user", user);
                startActivity(inboxIntent);
            }
        });

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inboxIntent = new Intent(AdminActivity.this, ApprovedRequestsActivity.class);
                inboxIntent.putExtra("current user", user);
                startActivity(inboxIntent);
            }
        });

        declined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inboxIntent = new Intent(AdminActivity.this, DeclinedRequestsActivity.class);
                inboxIntent.putExtra("current user", user);
                startActivity(inboxIntent);
            }
        });
    }

    private void logout() {
        if (firebaseAuth.getCurrentUser() != null) {
            firebaseAuth.signOut();

            Intent loginIntent = new Intent(AdminActivity.this, LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);

            AdminActivity.this.finish();
        }
    }
}