package com.example.medihub.models;

import com.example.medihub.enums.UserRole;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.medihub.interfaces.Model;

public class PatientProfile extends UserProfile implements Model, Serializable {
    // INSTANCE VARIABLES
    private String healthCardNumber;

    // health card number regex obtained from ontario gov website:
    // https://health.gov.on.ca/en/pro/publications/ohip/ebs_hcv_specs.aspx#:~:text=The%20health%20number%20is%20a,face%20of%20every%20health%20card.&text=Version%20code%20is%20an%20alphabetic,red%20and%20white%E2%80%9D)%20cards.
    protected static final String HEALTH_CARD_REGEX = "[1-9]\\d{8}";

    // CONSTRUCTORS
    public PatientProfile() {
        super(UserRole.patient);
    }

    public PatientProfile(String firstName, String lastName, String address, String phoneNumber, String email, String healthCardNumber) {
        super(UserRole.patient, firstName, lastName, address, phoneNumber, email);
        this.healthCardNumber = healthCardNumber;
    }

    // GETTERS
    public String getHealthCardNumber() {
        return healthCardNumber;
    }

    // SETTERS
    public void setHealthCardNumber(String healthCardNumber) {
        this.healthCardNumber = healthCardNumber;
    }

    // METHODS

    @Override
    public String toString() {
        return super.toString() +
                "\nHealth Card Number: " + healthCardNumber;
    }

    @Override
    public HashMap<String, String> validate() {
        HashMap<String, String> errors = super.validate();

        if (getRole() == null || getRole() != UserRole.patient) {
            errors.put("role", "incorrect user role (should be patient)");
        }
        if (healthCardNumber == null || !healthCardNumber.matches(HEALTH_CARD_REGEX)) {
            errors.put("healthCardNumber", "invalid health card number (format: only digits no dashes)");
        }

        return errors;
    }

    public String getKey() { return super.getKey(); }
    public void setKey(String key) { super.setKey(key); }
}
