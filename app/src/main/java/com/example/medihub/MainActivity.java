package com.example.medihub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medihub.enums.UserRole;
import com.example.medihub.models.DoctorProfile;
import com.example.medihub.models.PatientProfile;
import com.example.medihub.models.UserProfile;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.util.Log;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDB;
    private UserProfile currentUserProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize the firebase app
        FirebaseApp.initializeApp(this);

        // initialize firebase auth & db
        mAuth = FirebaseAuth.getInstance();
        firebaseDB = FirebaseDatabase.getInstance();

        // fetch the current user and check if they are signed in (null if not signed in)
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.i("Current User", currentUser == null ? "null" : currentUser.toString());

        // redirect user to login page if they are not signed in
        if (currentUser == null) {
            // create a new login intent and start it
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);

            finish(); // prevent the user from being able to navigate back
        }

        // fetch user profile information
        else {
            String userId = mAuth.getCurrentUser().getUid();
            DatabaseReference dbReference = firebaseDB.getReference("users").child(userId);

            dbReference.addListenerForSingleValueEvent(oneTimeListener);
            dbReference.addValueEventListener(listener);
        }
    }

    private ValueEventListener oneTimeListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                UserRole role = UserRole.valueOf(snapshot.child("role").getValue(String.class));

                Intent newIntent;

                if (role == UserRole.patient) {
                    currentUserProfile = snapshot.getValue(PatientProfile.class);
                    newIntent = new Intent(MainActivity.this, PatientActivity.class);
                } else if (role == UserRole.doctor) {
                    currentUserProfile = snapshot.getValue(DoctorProfile.class);
                    newIntent = new Intent(MainActivity.this, DoctorActivity.class);
                } else {
                    currentUserProfile = snapshot.getValue(UserProfile.class);
                    newIntent = new Intent(MainActivity.this, AdminActivity.class);
                }

                startActivity(newIntent);
                finish();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.i("ran on cancelled", error.toString());
        }
    };

    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                UserRole role = UserRole.valueOf(snapshot.child("role").getValue(String.class));

                if (role == UserRole.patient) {
                    currentUserProfile = snapshot.getValue(PatientProfile.class);
                } else if (role == UserRole.doctor) {
                    currentUserProfile = snapshot.getValue(DoctorProfile.class);
                } else {
                    currentUserProfile = snapshot.getValue(UserProfile.class);
                }

                Log.i("current user profile: ", currentUserProfile == null ? "null" : currentUserProfile.toString());
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.i("ran on cancelled", error.toString());
        }
    };
}