package com.example.medihub;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.medihub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {

    private TextView textWelcome;
    private FirebaseAuth auth;
    private Button buttonLogOut;
    private FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        auth = FirebaseAuth.getInstance();
        buttonLogOut = findViewById(R.id.buttonLogOut);
        user = auth.getCurrentUser();

        //CHANGE TEXT AND DISPLAY ROLE AND WELCOME MESSAGE
        //textWelcome.setText("Welcome " + user.getRole());

        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();

                startLoginActivity();
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





