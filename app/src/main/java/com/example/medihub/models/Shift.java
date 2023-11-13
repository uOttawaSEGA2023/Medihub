package com.example.medihub.models;

import com.example.medihub.interfaces.Model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

public class Shift implements Model, Serializable {
    private String key;
    private String doctor_id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;



    public Shift() {}

    public Shift(String doctor_id, LocalDateTime startDate, LocalDateTime endDate) {
        this.doctor_id = doctor_id;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    public String getDoctor_id() {
        return doctor_id;
    }

    public String getStartDate() {
        return startDate.toString();
    }
    public String getEndDate() {
        return endDate.toString();
    }
    public LocalDateTime localStartDate() {
        return startDate;
    }
    public LocalDateTime localEndDate() {
        return startDate;
    }


    public void setStartDate(String startDate) { this.startDate = LocalDateTime.parse(startDate); }
    public void setEndDate(String endDate) { this.endDate = LocalDateTime.parse(endDate); }
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


        // TODO (maybe): validate that reference of doctor_id is a doctor

        HashMap<String, String> errors = new HashMap<>();
        return errors;
    }
}
