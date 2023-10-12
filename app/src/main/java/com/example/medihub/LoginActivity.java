package com.example.medihub;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import android.util.Log;


public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button button5;
    private Button buttonLogin;
    private EditText textEmail;
    private EditText textPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        button5 = findViewById(R.id.button5);
        textEmail = findViewById(R.id.textEmail);
        textPassword = findViewById(R.id.textPassword);

        button5.setOnClickListener(new View.OnClickListener() {
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


            }
        });
    }


    public void openRegistrationOptionActivity() {
        Intent intent = new Intent(this, RegistrationOptionActivity.class);
        startActivity(intent);
    }
}
