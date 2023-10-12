package com.example.medihub.models;

import com.example.medihub.enums.DoctorSpecialty;
import com.example.medihub.enums.UserRole;
import com.example.medihub.interfaces.Model;

import java.util.ArrayList;
import java.util.EnumSet;

public class DoctorProfile extends UserProfile implements Model {
    // INSTANCE VARIABLES
    private String employeeNumber;
    private EnumSet<DoctorSpecialty> specialties;

    // CONSTRUCTORS
    public DoctorProfile(UserRole role, String firstName, String lastName, String address, String phoneNumber, String employeeNumber, EnumSet<DoctorSpecialty> specialties) {
        super(role, firstName, lastName, address, phoneNumber);
        this.employeeNumber = employeeNumber;
        this.specialties = specialties;
    }

    // GETTERS
    public String getEmployeeNumber() {
        return employeeNumber;
    }
    public EnumSet<DoctorSpecialty> getSpecialties() {
        return specialties;
    }

    // SETTERS
    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }
    public void setSpecialties(EnumSet<DoctorSpecialty> specialties) {
        this.specialties = specialties;
    }
    public void addSpecialty(DoctorSpecialty specialty) {
        this.specialties.add(specialty);
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
    public ArrayList<String> validate() {
        ArrayList<String> errors = super.validate();

        // make sure there are 1 or more specialties
        if (specialties == null || specialties.isEmpty()) {
            errors.add("invalid specialties (make sure there is at least one specialty)");
        }

        // TODO: Add employee number validation if required

        return errors;
    }
}
