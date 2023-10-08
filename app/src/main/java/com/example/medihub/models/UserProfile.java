package com.example.medihub.models;

import com.example.medihub.enums.UserRole;
import android.util.Patterns;
import java.util.ArrayList;

public class UserProfile {
    // INSTANCE VARIABLES

    private UserRole role;
    private String firstName;
    private String lastName;
    private String healthCardNumber;
    private String address;
    private String phoneNumber;

    // STATIC VARIABLES

    // health card number regex obtained from ontario gov website:
    // https://health.gov.on.ca/en/pro/publications/ohip/ebs_hcv_specs.aspx#:~:text=The%20health%20number%20is%20a,face%20of%20every%20health%20card.&text=Version%20code%20is%20an%20alphabetic,red%20and%20white%E2%80%9D)%20cards.
    private static final String HEALTH_CARD_REGEX = "[1-9]\\d{9}";
    private static final String NAME_REGEX = "^[A-Za-z-']{1,30}$";


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

    public ArrayList<String> validate() {
        ArrayList<String> errors = new ArrayList<>();

        if (role == null) {
            errors.add("invalid user role");
        }
        if (phoneNumber == null || !Patterns.PHONE.matcher(phoneNumber).matches()) {
            errors.add("invalid phone number");
        }
        if (healthCardNumber == null || !healthCardNumber.matches(HEALTH_CARD_REGEX)) {
            errors.add("invalid health card number (format: only digits no dashes)");
        }
        // TODO: (maybe remove name validations)
        if (firstName == null || !firstName.matches(NAME_REGEX)) {
            errors.add("invalid first name (no special characters allowed)");
        }
        if (lastName == null || !lastName.matches(NAME_REGEX)) {
            errors.add("invalid last name (no special characters allowed)");
        }
        // TODO: (maybe, if required) add address validation

        return errors;
    }
}
