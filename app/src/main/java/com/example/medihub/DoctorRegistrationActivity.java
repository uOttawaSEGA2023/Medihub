package com.example.medihub;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.medihub.enums.DoctorSpecialty;
import com.example.medihub.models.DoctorProfile;
import com.example.medihub.models.UserProfile;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;

public class DoctorRegistrationActivity extends AppCompatActivity  {
    private FirebaseAuth firebaseAuth;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;
    private EditText passwordConfirmation;
    private EditText phone;
    private EditText address;
    private EditText employeeNumber;
    private LinearLayout specialtiesLayout;
    private Button registerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);

        // get specialties layout
        this.specialtiesLayout = findViewById(R.id.specialties);

        // get UI form elements
        this.firstName = findViewById(R.id.firstNameText);
        this.lastName = findViewById(R.id.lastNameText);
        this.email = findViewById(R.id.emailText);
        this.password = findViewById(R.id.passwordText);
        this.passwordConfirmation = findViewById(R.id.passwordConfirmationText);
        this.phone = findViewById(R.id.phoneText);
        this.address = findViewById(R.id.addressText);
        this.employeeNumber = findViewById(R.id.employeeNumberText);
        this.registerButton = findViewById(R.id.registerButton);

        // dynamically add doctor specialties as checkboxes to specialties layout
        for (DoctorSpecialty spec : DoctorSpecialty.values()) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(spec.toString().replaceAll("_", " "));
            this.specialtiesLayout.addView(checkBox);
        }

        // listen for create account click event
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("REGISTER EVENT", "user clicked register button");
                boolean hasErrors = false;

                if (password.getText().toString().length() < UserProfile.MIN_PASSWORD_LENGTH) {
                    password.setError("password must be at least " + UserProfile.MIN_PASSWORD_LENGTH + " characters long");
                    hasErrors = true;
                }

                // validate password is same as password confirmation
                if (!password.getText().toString().equals(passwordConfirmation.getText().toString())) {
                    password.setError(password.getError()+"\npassword must match password confirmation");
                    passwordConfirmation.setError("password must match password confirmation");
                    hasErrors = true;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    email.setError("email is invalid");
                    hasErrors = true;
                }

                // build DoctorProfile record
                DoctorProfile doctorProfile = new DoctorProfile();
                doctorProfile.setFirstName(firstName.getText().toString());
                doctorProfile.setLastName(lastName.getText().toString());
                doctorProfile.setPhoneNumber(phone.getText().toString());
                doctorProfile.setAddress(address.getText().toString());
                doctorProfile.setEmployeeNumber(employeeNumber.getText().toString());

                // add specialties to record
                for (View v : specialtiesLayout.getTouchables()) {
                    if (v instanceof CheckBox && ((CheckBox)v).isChecked()) {
                       String specialtyString = ((CheckBox)v).getText().toString().replaceAll(" ", "_");
                       doctorProfile.addSpecialty(DoctorSpecialty.valueOf(specialtyString));
                    }
                }

                // validate DoctorProfile model
                HashMap<String, String> userProfileErrors = doctorProfile.validate();
                if (!userProfileErrors.isEmpty()) {
                    hasErrors = true;

                    if (userProfileErrors.containsKey("firstName")) {
                        firstName.setError(userProfileErrors.get("firstName"));
                    }
                    if (userProfileErrors.containsKey("lastName")) {
                        lastName.setError(userProfileErrors.get("lastName"));
                    }
                    if (userProfileErrors.containsKey("employeeNumber")) {
                        employeeNumber.setError(userProfileErrors.get("employeeNumber"));
                    }
                    if (userProfileErrors.containsKey("phoneNumber")) {
                        phone.setError(userProfileErrors.get("phoneNumber"));
                    }
                    if (userProfileErrors.containsKey("address")) {
                        address.setError(userProfileErrors.get("address"));
                    }
                    if (userProfileErrors.containsKey("role")) {
                        Toast.makeText(DoctorRegistrationActivity.this, userProfileErrors.get("role"), Toast.LENGTH_LONG).show();
                    }
                    if (userProfileErrors.containsKey("specialties")) {
                        // set error message for all checkboxes
                        for (View v : specialtiesLayout.getTouchables()) {
                            if (v instanceof CheckBox) {
                                ((CheckBox)v).setError(userProfileErrors.get("specialties"));
                            }
                        }
                    } else if (!userProfileErrors.containsKey("specialties")) {
                        // clear error message for all checkboxes
                        for (View v : specialtiesLayout.getTouchables()) {
                            if (v instanceof CheckBox) {
                                ((CheckBox)v).setError(null);
                            }
                        }
                    }
                }
                Log.i("validation: ", userProfileErrors.toString());

                if (!hasErrors) {
                    // TODO: add user to firebase
                    Log.i("validation: ", "successful");
                } else {
                    Log.i("validation: ", "failed");
                }
            }
        });
    }
}
