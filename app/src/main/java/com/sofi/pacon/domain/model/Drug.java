package com.sofi.pacon.domain.model;

import java.util.Date;

/**
 * Created by sofi on 04/01/18.
 */

public class Drug {
    private Date date;
    private String label;
    private int dosage;
    private String measure;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getDosage() {
        return dosage;
    }

    public void setDosage(int dosage) {
        this.dosage = dosage;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }
}
