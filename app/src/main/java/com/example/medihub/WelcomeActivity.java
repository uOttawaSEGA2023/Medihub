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

    TextView textView;
    FirebaseAuth auth;
    Button button;
    FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.buttonLogOut);
        user = auth.getCurrentUser();

        //The user is not logged in
        if (user == null) {






        }

    }

    public void startLoginActivity()
    {
        Intent intent = new Intent(this, DoctorRegistrationActivity.class);
        startActivity(intent);
    }


}
