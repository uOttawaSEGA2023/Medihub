package com.example.medihub;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Intent;
import android.util.Log;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize the firebase app
        FirebaseApp.initializeApp(this);

        // initialize firebase auth
        mAuth = FirebaseAuth.getInstance();

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
    }
}