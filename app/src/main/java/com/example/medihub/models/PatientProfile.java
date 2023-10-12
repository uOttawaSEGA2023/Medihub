package com.example.medihub.models;

import com.example.medihub.enums.UserRole;

import java.util.ArrayList;

public class PatientProfile extends UserProfile {
    // INSTANCE VARIABLES
    private String healthCardNumber;

    // health card number regex obtained from ontario gov website:
    // https://health.gov.on.ca/en/pro/publications/ohip/ebs_hcv_specs.aspx#:~:text=The%20health%20number%20is%20a,face%20of%20every%20health%20card.&text=Version%20code%20is%20an%20alphabetic,red%20and%20white%E2%80%9D)%20cards.
    private static final String HEALTH_CARD_REGEX = "[1-9]\\d{9}";


    // CONSTRUCTORS
    public PatientProfile(UserRole role, String firstName, String lastName, String address, String phoneNumber, String healthCardNumber) {
        super(role, firstName, lastName, address, phoneNumber);
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
    public ArrayList<String> validate() {
        ArrayList<String> errors = super.validate();

        if (healthCardNumber == null || !healthCardNumber.matches(HEALTH_CARD_REGEX)) {
            errors.add("invalid health card number (format: only digits no dashes)");
        }

        return errors;
    }
}
