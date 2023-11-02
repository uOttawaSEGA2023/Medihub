package com.example.medihub.models;

import com.example.medihub.interfaces.Model;

import java.util.Date;
import java.util.HashMap;

public class Shift implements Model {
    private String key;
    private String doctor_id;
    private Date startDate;
    private Date endDate;



    public Shift() {}

    public Shift(String doctor_id, Date startDate, Date endDate) {
        this.doctor_id = doctor_id;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    public String getDoctor_id() {
        return doctor_id;
    }
    public Date getStartDate() {
        return startDate;
    }
    public Date getEndDate() {
        return endDate;
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
        // TODO (maybe): validate that reference of doctor_id is a doctor

        HashMap<String, String> errors = new HashMap<>();
        return errors;
    }
}
