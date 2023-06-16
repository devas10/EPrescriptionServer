package com.Devas.BackendPrescription.repositories;

import com.Devas.BackendPrescription.models.Doctor;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface DoctorRepository extends MongoRepository<Doctor, String> {

    Optional<Doctor> findById(String s);
    List<Doctor> findAll();
    Doctor findByUsername (String username);
    Boolean existsByUsername (String username);
    Boolean existsByEmail(String email);
    Doctor findByEmail(String username);
    Doctor deleteByUsername(String username);
    void delete(Doctor entity);
}
