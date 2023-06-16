package com.Devas.BackendPrescription.services;

import com.Devas.BackendPrescription.mail.EmailService;
import com.Devas.BackendPrescription.models.Doctor;
import com.Devas.BackendPrescription.models.Prescription;
import com.Devas.BackendPrescription.repositories.DoctorRepository;
import com.Devas.BackendPrescription.repositories.PrescriptionRepository;
import com.Devas.BackendPrescription.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PrescriptionServices {
    @Autowired
    DoctorService doctorService;

    @Autowired
    PrescriptionRepository prescriptionRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    EmailService emailService;

    public ResponseEntity<List<Prescription>> getByAccessToken(String accessToken) {
        Doctor doc = doctorService.getDoctorByAccessToken(accessToken);
        if(doc != null){
            return getAllPrescriptionByDoctorId(doc.getId());
        }
        return ResponseEntity.accepted().body(null);
    }

    public ResponseEntity<Prescription> insertPrescription(Prescription newPrescription, String AccessToken){
        System.out.println("new Prescription added " + newPrescription.getPatientEmailId());
        Optional<Doctor> doc = doctorRepository.findById(newPrescription.getDoctorId());
        if(doc.isPresent() && new JwtUtils().validateJwt(AccessToken)) {
            prescriptionRepository.save(newPrescription);

            emailService.sendAttachmentMail(newPrescription.getPatientEmailId(),"Get your Prescription",newPrescription,doc.get());
            return ResponseEntity.accepted().body(newPrescription);
        }
        return ResponseEntity.badRequest().body(newPrescription);
    }

    public ResponseEntity<List<Prescription>> getAllPrescriptionByDoctorId(String doctorId){
        List<Prescription> all =  prescriptionRepository.findAllByDoctorId(doctorId);
        Optional<Doctor> doc = doctorRepository.findById(doctorId);
        if(doc.isPresent()){
            return ResponseEntity.accepted().body(all);
        }
        return ResponseEntity.badRequest().body(null);
    }

    public List<Prescription> getAllPrescriptions(){
        List<Prescription> p = prescriptionRepository.findAll();
        for(Prescription pr : p){
            System.out.println(pr.getMedicines());
        }
        return p;
    }
}
