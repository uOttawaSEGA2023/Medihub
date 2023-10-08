package com.example.medihub.models;

import com.example.medihub.enums.UserRole;

public class PatientProfile extends UserProfile {
    // CONSTRUCTORS

    public PatientProfile(UserRole role, String firstName, String lastName, String healthCardNumber, String address, String phoneNumber) {
        super(role, firstName, lastName, healthCardNumber, address, phoneNumber);
    }

    // METHODS

    @Override
    public String toString() {
        return super.toString();
    }

    // TODO: IMPLEMENT VALIDATE FUNCTION
    @Override
    public String[] validate() {
        return null;
    }
}
