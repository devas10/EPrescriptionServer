package com.Devas.BackendPrescription.controller;

import com.Devas.BackendPrescription.models.Doctor;
import com.Devas.BackendPrescription.models.Prescription;
import com.Devas.BackendPrescription.security.JwtUtils;
import com.Devas.BackendPrescription.services.DoctorService;
import com.Devas.BackendPrescription.services.PrescriptionServices;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/test")
public class TestControllers {

    private final DoctorService doctorService;
    private final PrescriptionServices prescriptionService;

    public TestControllers(DoctorService doctorService, PrescriptionServices prescriptionService) {
        this.doctorService = doctorService;
        this.prescriptionService = prescriptionService;
    }


    @GetMapping("/username")
    public Boolean existsByUsername(@RequestParam String username){
        return doctorService.existsByUsername(username);
    }

    @GetMapping("/all")
    public List<Doctor> fetchAllDoctors(){
        System.out.println("Fetched All Doctors");
        return doctorService.getAllDoctors();
    }

    @GetMapping("/deleteByUsername")
    public Doctor deleteByUsername(@RequestParam String username){
        return doctorService.deleteByUsername(username);
    }

    @GetMapping("/validateToken")
    public Boolean ValidateToken(@RequestParam String validateToken){
        return new JwtUtils().validateJwt(validateToken);
    }

    @GetMapping("/allPrescriptions")
    public List<Prescription> fetchAllPrescription(){
        return prescriptionService.getAllPrescriptions();
    }

    @GetMapping("/doctor/{AccessToken}")
    public Doctor getDoctor(@PathVariable String AccessToken){
        return doctorService.getDoctorByAccessToken(AccessToken);
    }



}
