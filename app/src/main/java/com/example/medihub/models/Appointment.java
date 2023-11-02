package com.example.medihub.models;

import com.example.medihub.enums.RequestStatus;
import com.example.medihub.interfaces.Model;

import java.util.Date;
import java.util.HashMap;

public class Appointment implements Model {
    private String key;
    private String patient_id;
    private String doctor_id;
    private RequestStatus status;
    private Date startDate;
    private Date endDate;



    public Appointment() {}

    public Appointment(String patient_id, String doctor_id, RequestStatus status, Date startDate, Date endDate) {
        this.patient_id = patient_id;
        this.doctor_id = doctor_id;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }




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
        // TODO: Make sure start and end dates are same day and that end is not before start
        // TODO (maybe): validate that reference of patient_id is a patient
        // TODO (maybe): validate that reference of doctor_id is a doctor

        HashMap<String, String> errors = new HashMap<>();
        return errors;
    }
}
