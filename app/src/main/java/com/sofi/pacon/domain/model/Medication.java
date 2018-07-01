package com.sofi.pacon.domain.model;

public class Medication {

    public Medication() {
    }

    private String name;
    private Dosage dosage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Dosage getDosage() {
        return dosage;
    }

    public void setDosage(Dosage dosage) {
        this.dosage = dosage;
    }

}
