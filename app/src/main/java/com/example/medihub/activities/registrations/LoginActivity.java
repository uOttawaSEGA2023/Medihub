package com.example.medihub.activities.registrations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medihub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


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

                if (userEmail.isEmpty() && userPassword.isEmpty() == false) {

                    Toast.makeText(LoginActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;

                } else if (userPassword.isEmpty() && userEmail.isEmpty() == false) {

                    Toast.makeText(LoginActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;

                } else if (userPassword.isEmpty() && userEmail.isEmpty()) {

                    Toast.makeText(LoginActivity.this, "Enter email and password", Toast.LENGTH_SHORT).show();
                    return;

                }

                mAuth = FirebaseAuth.getInstance();
                mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                    openWelcomeActivity();
                                } else {

                                    Toast.makeText(LoginActivity.this, "Email or Password is Incorrect",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });





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
        finish();
    }
}
