package com.example.medihub;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button buttonCreateAccount;
    private Button buttonLogin;
    private EditText textEmail;
    private EditText textPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);
        textEmail = findViewById(R.id.textEmail);
        textPassword = findViewById(R.id.textPassword);

        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openRegistrationOptionActivity();
            }
        });

        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userEmail = textEmail.getText().toString();
                String userPassword = textPassword.getText().toString();

                //ADD AUTHENTICATION HERE

                //ASSUME LOGIN WORKS

               openWelcomeActivity();



            }
        });
    }


    public void openRegistrationOptionActivity() {
        Intent intent = new Intent(this, RegistrationOptionActivity.class);
        startActivity(intent);
    }

    public void openWelcomeActivity() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }
}
