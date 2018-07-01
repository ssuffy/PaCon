package com.sofi.pacon.domain.model;

public class TakingMedication {

    private String name;
    private String medicationDose;
    private String measure;
    private int quantity;

    public TakingMedication(String name, String medicationDose, String measure, int quantity) {
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
