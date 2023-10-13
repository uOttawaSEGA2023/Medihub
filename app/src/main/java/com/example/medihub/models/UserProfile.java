package com.example.medihub.models;

import com.example.medihub.enums.UserRole;
import android.util.Patterns;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.medihub.interfaces.Model;

public class UserProfile implements Model {
    // INSTANCE VARIABLES
    private UserRole role;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;

    // STATIC VARIABLES
    private static final String POSTAL_CODE_REGEX = "^[A-Z][0-9][A-Z] [0-9][A-Z][0-9]$";


    // CONSTRUCTORS
    public UserProfile(UserRole role) { this.role = role; }

    public UserProfile(UserRole role, String firstName, String lastName, String address, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    // GETTERS
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getAddress() {
        return address;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public UserRole getRole() {
        return role;
    }

    // SETTERS
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // METHODS
    @Override
    public String toString() {
        return getRole().toString() + " User Profile: " +
                "\nName: " + getFirstName() + " " + getLastName() +
                "\nAddress: " + getAddress() +
                "\nPhone Number: " + getPhoneNumber();
    }


    public HashMap<String, String> validate() {
        HashMap<String, String> errors = new HashMap<>();

        if (phoneNumber == null || !Patterns.PHONE.matcher(phoneNumber).matches()) {
            errors.put("phoneNumber", "invalid phone number");
        }
        if (address == null || !address.matches(POSTAL_CODE_REGEX)) {
            errors.put("address", "invalid postal code address (format: A1A A1A)");
        }

        return errors;
    }
}
