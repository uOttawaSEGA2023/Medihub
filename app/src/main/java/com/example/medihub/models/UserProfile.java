package com.example.medihub.models;

import com.example.medihub.enums.UserRole;

public class UserProfile {
    // INSTANCE VARIABLES

    private UserRole role;
    private String firstName;
    private String lastName;
    private String healthCardNumber;
    private String address;
    private String phoneNumber;

    // CONSTRUCTORS

    public UserProfile(UserRole role, String firstName, String lastName, String healthCardNumber, String address, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.healthCardNumber = healthCardNumber;
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
    public String getHealthCardNumber() {
        return healthCardNumber;
    }
    public String getAddress() {
        return address;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getRole() {
        return role.toString();
    }

    // SETTERS

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setHealthCardNumber(String healthCardNumber) {
        this.healthCardNumber = healthCardNumber;
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
        return getRole() + " User Profile: " +
                "\nName: " + getFirstName() + " " + getLastName() +
                "\nHealth Card Number: " + getHealthCardNumber() +
                "\nAddress: " + getAddress() +
                "\nPhone Number: " + getPhoneNumber();
    }

    // TODO: IMPLEMENT VALIDATE FUNCTION
    public String[] validate() {
        return null;
    }
}
