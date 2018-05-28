package com.sofi.pacon.domain.model;

import java.util.Date;

public class TakingDrug {

    private Date time;
    private String label;
    private String drugDose;
    private String measure;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDrugDose() {
        return drugDose;
    }

    public void setDrugDose(String drugDose) {
        this.drugDose = drugDose;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }
}
