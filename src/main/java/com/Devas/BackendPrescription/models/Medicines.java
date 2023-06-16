package com.Devas.BackendPrescription.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Medicines{

    @NotBlank
    private String medicineName;

    @NotNull
    private int dosage;

    @NotNull
    private int [] timings;

    // getters and setters

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public int getDosage() {
        return dosage;
    }

    public void setDosage(int dosage) {
        this.dosage = dosage;
    }

    public int[] getTimings() {
        return timings;
    }

    public void setTimings(int[] timings) {
        this.timings = timings;
    }

    public Medicines(String medicineName, int dosage, int[] timings) {
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.timings = timings;
    }
}
