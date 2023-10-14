package com.example.medihub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medihub.enums.UserRole;
import com.example.medihub.models.PatientProfile;
import com.example.medihub.models.UserProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PatientRegistrationActivity extends AppCompatActivity  {

    private Button register;
    private EditText last_name;
    private EditText first_name;
    private EditText email;
    private EditText password;
    private EditText confirm_password;
    private EditText phone;
    private EditText address;
    private EditText healthcard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration);
        register = findViewById(R.id.buttonRegister);
        last_name = findViewById(R.id.firstLastText);
        first_name = findViewById(R.id.firstNameText);
        email = findViewById(R.id.emailText);
        password = findViewById(R.id.passwordText);
        confirm_password = findViewById(R.id.passwordConfirmationText);
        phone = findViewById(R.id.phoneText);
        address = findViewById(R.id.addressText);
        healthcard = findViewById(R.id.healthCardNumber);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasErrors = false;

                // USER INPUT VALIDATION

                if (password.getText().toString().length() < UserProfile.MIN_PASSWORD_LENGTH) {
                    password.setError("password must be at least " + UserProfile.MIN_PASSWORD_LENGTH + " characters long");
                    hasErrors = true;
                }

                if (!password.getText().toString().equals(confirm_password.getText().toString())) {
                    password.setError(password.getError()+"\npassword must match password confirmation");
                    confirm_password.setError("password must match password confirmation");
                    hasErrors = true;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    email.setError("email is invalid");
                    hasErrors = true;
                }

                UserProfile user = new PatientProfile(first_name.getText().toString(),
                        last_name.getText().toString(),
                        address.getText().toString(),
                        phone.getText().toString(),
                        healthcard.getText().toString());

                HashMap<String, String> errors = user.validate();
                if (!errors.isEmpty()) {
                    hasErrors = true;

                    if (errors.containsKey("firstName")) {
                        first_name.setError(errors.get("firstName"));
                    }
                    if (errors.containsKey("lastName")) {
                        last_name.setError(errors.get("lastName"));
                    }
                    if (errors.containsKey("phoneNumber")) {
                        phone.setError(errors.get("phoneNumber"));
                    }
                    if (errors.containsKey("address")) {
                        address.setError(errors.get("address"));
                    }
                    if (errors.containsKey("healthCardNumber")) {
                        healthcard.setError(errors.get("healthCardNumber"));
                    }
                    if (errors.containsKey("role")) {
                        Toast.makeText(PatientRegistrationActivity.this, errors.get("role"), Toast.LENGTH_LONG).show();
                    }
                }

                if (!hasErrors)
                    startLogin();
            }
        });


    }
    public void startLogin()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}