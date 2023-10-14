package com.example.medihub;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;

public class DoctorRegistrationActivity extends AppCompatActivity  {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDB;
    private DoctorProfile doctorProfile;

    // UI ELEMENTS
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

                // validate email & password & other form fields
                boolean hasErrors = validateEmailAndPassword();
                hasErrors = validateDoctorProfile() || hasErrors;

                if (!hasErrors) {  // create user
                    Log.i("validation:", "successful");

                    // Validation was successful, try adding user to firebase auth and database
                    addDoctorToAuthentication(email.getText().toString(), password.getText().toString());
                } else {  // validation failed
                    Log.i("validation:", "failed");
                }
            }
        });
    }

    /*
     * Validates that the user's email & password is correct
     */
    private boolean validateEmailAndPassword() {
        String passwordStr = password.getText().toString();
        String passwordConfirmationStr = passwordConfirmation.getText().toString();
        String emailStr = email.getText().toString();

        boolean hasErrors = false;

        // validate password length
        if (passwordStr.length() < UserProfile.MIN_PASSWORD_LENGTH) {
            password.setError("password must be at least " + UserProfile.MIN_PASSWORD_LENGTH + " characters long");
            hasErrors = true;
        }

        // validate password is same as password confirmation
        if (!passwordStr.equals(passwordConfirmationStr)) {
            password.setError(password.getError()+"\npassword must match password confirmation");
            passwordConfirmation.setError("password must match password confirmation");
            hasErrors = true;
        }

        // validate email
        if (!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            email.setError("email is invalid");
            hasErrors = true;
        }

        return hasErrors;
    }

    /*
     * Validates that the user's doctorProfile object is valid
     */
    private boolean validateDoctorProfile() {
        boolean hasErrors = false;

        // build DoctorProfile record
        doctorProfile = new DoctorProfile();
        doctorProfile.setFirstName(firstName.getText().toString());
        doctorProfile.setLastName(lastName.getText().toString());
        doctorProfile.setPhoneNumber(phone.getText().toString());
        doctorProfile.setAddress(address.getText().toString());
        doctorProfile.setEmployeeNumber(employeeNumber.getText().toString());
        for (View v : specialtiesLayout.getTouchables()) {  // add specialties to record
            if (v instanceof CheckBox && ((CheckBox)v).isChecked()) {
                String specialtyString = ((CheckBox)v).getText().toString().replaceAll(" ", "_");
                doctorProfile.addSpecialty(DoctorSpecialty.valueOf(specialtyString));
            }
        }

        // validate DoctorProfile model & check for errors
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
            }
        }

        if (!userProfileErrors.containsKey("specialties")) {
            // clear error message for all checkboxes
            for (View v : specialtiesLayout.getTouchables()) {
                if (v instanceof CheckBox) {
                    ((CheckBox)v).setError(null);
                }
            }
        }

        Log.i("userProfileErrors: ", userProfileErrors.toString());

        return hasErrors;
    }

    /*
     * Takes an email & password and adds it to firebase authentication
     */
    private void addDoctorToAuthentication(String email, String password) {
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(DoctorRegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // add & link user profile information to firebase database
                        if (task.isSuccessful()) {
                            Log.i("Firebase Authentication: ", "successfully created firebase user");
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            Log.i("current user:", user.toString());

                            if (user.getUid() != null) {
                                addDoctorToDatabase(user.getUid());

                                // redirect to welcome page
                                Intent welcomeIntent = new Intent(DoctorRegistrationActivity.this, WelcomeActivity.class);
                                startActivity(welcomeIntent);

                                finish();
                            } else {
                                Toast.makeText(DoctorRegistrationActivity.this, "Authentication failed, please try again later", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Log.w("Firebase Authentication: ", "failed to create firebase user " + task.getException().toString());
                            Toast.makeText(DoctorRegistrationActivity.this, "Authentication failed, please try again later", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /*
     * Adds the doctorProfile object to firebase database given the FirebaseUser id
     */
    private void addDoctorToDatabase(String userId) {
        firebaseDB = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = firebaseDB.getReference("users");

        usersRef.child(userId).setValue(doctorProfile.toMap());
    }
}
