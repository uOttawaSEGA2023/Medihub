package com.example.medihub.models;

import com.example.medihub.enums.DoctorSpecialty;
import com.example.medihub.enums.UserRole;
import com.example.medihub.interfaces.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

public class DoctorProfile extends UserProfile implements Model, Serializable {
    // INSTANCE VARIABLES
    private String employeeNumber;
    private List<DoctorSpecialty> specialties;

    // CONSTRUCTORS
    public DoctorProfile() {
        super(UserRole.doctor);
        this.specialties = new ArrayList<>();
    }

    public DoctorProfile(String firstName, String lastName, String address, String phoneNumber, String employeeNumber, ArrayList<DoctorSpecialty> specialties) {
        super(UserRole.doctor, firstName, lastName, address, phoneNumber);
        this.employeeNumber = employeeNumber;
        this.specialties = specialties;
    }

    // GETTERS
    public String getEmployeeNumber() {
        return employeeNumber;
    }
    public List<DoctorSpecialty> getSpecialties() {
        return specialties;
    }

    // SETTERS
    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }
    public void setSpecialties(ArrayList<DoctorSpecialty> specialties) {
        this.specialties = specialties;
    }
    public void addSpecialty(DoctorSpecialty specialty) {
        if (!this.specialties.contains(specialty)) {
            this.specialties.add(specialty);
        }
    }
    public void removeSpecialty(DoctorSpecialty specialty) {
        this.specialties.remove(specialty);
    }

    // METHODS
    @Override
    public String toString() {
        return super.toString() +
                "\nEmployee Number: " + employeeNumber +
                "\nSpecialties: " + specialties.toString();
    }

    @Override
    public HashMap<String, String> validate() {
        HashMap<String, String> errors = super.validate();

        if (getRole() == null || getRole() != UserRole.doctor) {
            errors.put("role", "incorrect user role (should be doctor)");
        }

        // make sure there are 1 or more specialties
        if (specialties == null || specialties.isEmpty()) {
            errors.put("specialties", "invalid specialties (make sure there is at least one specialty)");
        }
        if (employeeNumber == null || employeeNumber.isEmpty()) {
            errors.put("employeeNumber", "employee number can't be blank");
        }

        return errors;
    }

    public String getKey() { return super.getKey(); }
    public void setKey(String key) { super.setKey(key); }
}
