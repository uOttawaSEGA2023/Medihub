package com.example.medihub.models;

import android.util.Patterns;

import com.example.medihub.enums.DoctorSpecialty;
import com.example.medihub.enums.RegistrationStatus;
import com.example.medihub.enums.UserRole;
import com.example.medihub.interfaces.Model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class RegistrationRequest implements Model, Serializable {
    private String key;
    private boolean patient;
    private RegistrationStatus status;
    private String firstName, lastName, address, phoneNumber, healthCardNumber, employeeNumber, email;
    private ArrayList<DoctorSpecialty> specialties;

    public RegistrationRequest() {}

    public RegistrationRequest(boolean isPatient) {
        this.patient = isPatient;
    }

    public RegistrationRequest(boolean isPatient, String firstName, String lastName, String address,
                               String phoneNumber, String email, String healthCardNumber, String employeeNumber,
                               ArrayList<DoctorSpecialty> specialties) {
        this.patient = isPatient;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.status = RegistrationStatus.pending;

        if (isPatient) {
            this.healthCardNumber = healthCardNumber;
        } else {
            this.employeeNumber = employeeNumber;
            this.specialties = specialties;
        }
    }

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
    public String getEmail() { return email; }
    public String getHealthCardNumber() {
        return healthCardNumber;
    }
    public String getEmployeeNumber() {
        return employeeNumber;
    }
    public ArrayList<DoctorSpecialty> getSpecialties() {
        return specialties;
    }
    public RegistrationStatus getStatus() {
        return status;
    }
    public boolean isPatient() {
        return patient;
    }

    public boolean approve() {
        if (status != RegistrationStatus.approved) {
            status = RegistrationStatus.approved;
            return true;
        }

        return false;
    }

    public boolean decline() {
        if (status == RegistrationStatus.pending) {
            status = RegistrationStatus.declined;
            return true;
        }

        return false;
    }

    @Override
    public HashMap<String, String> validate() {
        HashMap<String, String> errors = new HashMap<>();

        if (status == null)
            errors.put("status", "registration status must be present");

        if (firstName == null || firstName.isEmpty())
            errors.put("firstName", "first name can't be empty");

        if (lastName == null || lastName.isEmpty())
            errors.put("lastName", "last name can't be empty");

        if (address == null || !address.matches(UserProfile.POSTAL_CODE_REGEX))
            errors.put("address", "address can't be empty");

        if (phoneNumber == null || !Patterns.PHONE.matcher(phoneNumber).matches())
            errors.put("phoneNumber", "invalid phone number");

        if (email == null || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
            errors.put("email", "invalid email");

        if (patient && (healthCardNumber == null || !healthCardNumber.matches(PatientProfile.HEALTH_CARD_REGEX)))
            errors.put("healthCardNumber", "invalid health card number (format: only digits no dashes)");

        if (!patient && (specialties == null || specialties.isEmpty()))
            errors.put("specialties", "invalid specialties (make sure there is at least one specialty)");

        if (!patient && (employeeNumber == null || employeeNumber.isEmpty()))
            errors.put("employeeNumber", "employee number can't be blank");

        return errors;
    }

    public UserProfile createUser() {
        UserProfile user = null;

        if (validate().isEmpty()) {
            UserRole role = patient ? UserRole.patient : UserRole.doctor;

            if (role == UserRole.patient) {
                user = new PatientProfile(firstName, lastName, address, phoneNumber, email, healthCardNumber);
            } else {
                user = new DoctorProfile(firstName, lastName, address, phoneNumber, email, employeeNumber, specialties);
            }
        }

        return user;
    }

    public void setStatus(RegistrationStatus new_status)
    {
        this.status = new_status;
    }

    @Override
    public String toString() {
        return "RegistrationRequest: " +
                "\nStatus: " + getStatus().toString() +
                "\nPatient: " + isPatient() +
                "\nName: " + getFirstName() + " " + getLastName() +
                "\nAddress: " + getAddress() +
                "\nPhone Number: " + getPhoneNumber() +
                "\nEmail: " + getEmail() +
                "\nHealth Card Number: " + getHealthCardNumber() +
                "\nSpecialties: " + getSpecialties() +
                "\nEmployee Number: " + getEmployeeNumber();
    }
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
}
