package com.sofi.pacon.domain.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TakingMedication {

    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    private String time;
    private String name;
    private String medicationDose;
    private String measure;
    private int quantity;

    public TakingMedication() {}

    public TakingMedication(Date time, String name, String medicationDose, String measure, int quantity) {
        if(time != null) {
            this.time = sdf.format(time);
        }
        this.name = name;
        this.medicationDose = medicationDose;
        this.measure = measure;
        this.quantity = quantity;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTimeWithDate(Date time) {
        this.time = sdf.format(time);
    }

    public String getMedicationDose() {
        return medicationDose;
    }

    public void setMedicationDose(String medicationDose) {
        this.medicationDose = medicationDose;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
