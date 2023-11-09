package com.example.medihub.models;

import com.example.medihub.enums.RequestStatus;
import com.example.medihub.interfaces.Model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

public class Appointment implements Model, Serializable {
    private String key;
    private String patient_id;
    private String doctor_id;
    private String shift_id;
    private RequestStatus status;
    private LocalDateTime startDate;



    public Appointment() {}

    public Appointment(String patient_id, String doctor_id, String shift_id, RequestStatus status, LocalDateTime startDate) {
        this.patient_id = patient_id;
        this.doctor_id = doctor_id;
        this.shift_id = shift_id;
        this.status = status;
        this.startDate = startDate;
    }



    public String getPatient_id() { return patient_id; }
    public String getDoctor_id() {
        return doctor_id;
    }
    public String getShift_id() { return shift_id; }
    public RequestStatus getStatus() {
        return status;
    }
    public String getStartDate() {
        return startDate.toString();
    }
    public LocalDateTime localStartDate() {
        return startDate;
    }


    public void setStartDate(String startDate) { this.startDate = LocalDateTime.parse(startDate); }
    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public HashMap<String, String> validate() {
        // TODO: Make sure start date belongs to a shift belonging to the doctor
        // TODO (maybe): validate that reference of patient_id is a patient
        // TODO (maybe): validate that reference of doctor_id is a doctor
        // TODO (maybe): validate that reference of shift_id is a shift

        HashMap<String, String> errors = new HashMap<>();
        return errors;
    }
}
