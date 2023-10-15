package com.example.medihub;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medihub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomeActivity extends AppCompatActivity {

    private TextView textWelcome;
    private FirebaseAuth auth;
    private Button buttonLogOut;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDB;
    private Button homeButton;
    private Intent homeIntent = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        auth = FirebaseAuth.getInstance();
        buttonLogOut = findViewById(R.id.buttonLogOut);
        user = auth.getCurrentUser();
        firebaseDB = FirebaseDatabase.getInstance();
        textWelcome = findViewById(R.id.textWelcome);
        homeButton = findViewById(R.id.homeButton);

        String userId = auth.getUid();
        DatabaseReference dbReference = firebaseDB.getReference("users").child(userId);

        ValueEventListener userValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String role = dataSnapshot.child("role").getValue(String.class);
                    String name = dataSnapshot.child("firstName").getValue(String.class);

                    Log.d("role", role + "");
                    Log.d("name", name + "");

                    // Welcome to MediHub! You are logged in as a <role>
                    textWelcome.setText("Welcome to MediHub " + name + "! You are logged in as "
                            + (role.equals("admin") ? "an " + role : "a " + role));

                    switch (role) {
                        case "admin":
                            homeIntent = new Intent(WelcomeActivity.this, AdminActivity.class);
                            break;
                        case "patient":
                            homeIntent = new Intent(WelcomeActivity.this, PatientActivity.class);
                            break;
                        case "doctor":
                            homeIntent = new Intent(WelcomeActivity.this, DoctorActivity.class);
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.e("Firebase", "Error fetching user data: " + databaseError.getMessage());
            }
        };


        dbReference.addListenerForSingleValueEvent(userValueEventListener);

        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();

                startLoginActivity();
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (homeIntent != null) {
                    startActivity(homeIntent);
                } else {
                    Toast.makeText(WelcomeActivity.this, "Redirect failed, please try again later.", Toast.LENGTH_LONG);
                }
            }
        });
    }

    public void startLoginActivity()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    }





