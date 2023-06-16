package com.Devas.BackendPrescription.services;

import com.Devas.BackendPrescription.models.Doctor;
import com.Devas.BackendPrescription.repositories.DoctorRepository;

import com.Devas.BackendPrescription.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    @Autowired
    DoctorRepository doctorRepository;

    public List<Doctor> getAllDoctors(){
        return doctorRepository.findAll();
    }

    public String insertDoctors(Doctor newDoctor) {
        if(doctorRepository.existsByUsername(newDoctor.getUsername()) || doctorRepository.existsByEmail(newDoctor.getEmail())) {
            return "Username Exists";
        }
        System.out.println("added new Doctor  "+newDoctor.getName());
        doctorRepository.insert(newDoctor);
        String usernameToken = new JwtUtils().GenerateJwtToken(newDoctor.getUsername());
        return usernameToken;
    }

    public Doctor loadByUsername(String username){
        Doctor doctor = doctorRepository.findByUsername(username);
        if(doctor == null){
            doctor = doctorRepository.findByEmail(username);
        }
        if(doctor != null){
            return doctor;
        }
        return null;
    }

    public Boolean existsByUsername(String username) {
        return doctorRepository.existsByUsername((username));
    }

    public Doctor deleteByUsername(String username){
        Doctor doctorToBeDeleted = loadByUsername(username);
        if(doctorToBeDeleted != null) {
            doctorRepository.delete(doctorToBeDeleted);
            doctorRepository.deleteByUsername(doctorToBeDeleted.getUsername());
        }
        return doctorToBeDeleted;
    }

    public Doctor getDoctorByAccessToken(String AccessToken){
        String user = new JwtUtils().DecodeJwtToken(AccessToken).getSubject();
        Doctor doc = doctorRepository.findByUsername(user);
        doc.setPassword("");
        return doc;
    }
}
