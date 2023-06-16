package com.Devas.BackendPrescription.repositories;

import com.Devas.BackendPrescription.models.Prescription;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PrescriptionRepository extends MongoRepository<Prescription,String> {
    Optional<Prescription> findById(String s);
    Optional<Prescription> existsByDoctorId(String DoctorName);
    Optional<Prescription> existsByPatientName(String PatientName);
    List<Prescription> findAllByDoctorId(String doctorId);
    List<Prescription> findAll();
}
