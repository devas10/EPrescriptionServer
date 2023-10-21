package com.Devas.BackendPrescription.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Document
public class Prescription{
    @Id
    private String id;

    private String doctorId;

    private String patientName;

    @NotBlank
    private String patientEmailId;

    @NotNull
    private int duration;

    private String symptoms;
    @NotNull
    private List<Medicines> medicines;

    private String advice;

    private int scheduledCourse;
    //getters and setters

    Prescription(){
      this.advice  = "Stay safe and get well soon";
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prescription that = (Prescription) o;
        return duration == that.duration && Objects.equals(id, that.id) && Objects.equals(doctorId, that.doctorId) && Objects.equals(patientName, that.patientName) && Objects.equals(patientEmailId, that.patientEmailId) && Objects.equals(medicines, that.medicines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, doctorId, patientName, patientEmailId, duration, medicines, advice ,symptoms);
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientEmailId() {
        return patientEmailId;
    }

    public void setPatientEmailId(String patientEmailId) {
        this.patientEmailId = patientEmailId;
    }

    public int getDuration() {
        return duration;
    }

    public List<Medicines> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<Medicines> medicines) {
        this.medicines = medicines;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }


    public int getScheduledCourse() {
        return scheduledCourse;
    }

    public void setScheduledCourse(int scheduledCourse) {
        this.scheduledCourse = scheduledCourse;
    }

    public Prescription(String doctorId, String patientName, String patientEmailId, int duration,
                        List<Medicines> medicines ,String symptoms, String advice) {
        this.scheduledCourse = duration;
        this.medicines = medicines;
        this.doctorId = doctorId;
        this.patientName = patientName;
        this.patientEmailId = patientEmailId;
        this.symptoms = symptoms;
        this.advice = advice;
        this.duration = duration;
    }
}