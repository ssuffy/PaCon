package com.sofi.pacon.domain.model;

import java.util.List;

public class Dosage {

    public Dosage() {
    }

    private String unit;
    private List<String> values;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
