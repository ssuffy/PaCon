package com.sofi.pacon.domain.model;

import java.util.Date;
import java.util.List;

public class TakingMedicationDay {

    private Date time;
    private List<TakingMedication> takingMedications;

    public TakingMedicationDay(Date time, List<TakingMedication> takingMedications) {
        this.time = time;
        this.takingMedications = takingMedications;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public List<TakingMedication> getTakingMedications() {
        return takingMedications;
    }

    public void setTakingMedications(List<TakingMedication> takingMedications) {
        this.takingMedications = takingMedications;
    }
}
