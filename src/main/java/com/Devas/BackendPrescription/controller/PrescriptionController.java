package com.Devas.BackendPrescription.controller;

import com.Devas.BackendPrescription.models.Prescription;
import com.Devas.BackendPrescription.services.PrescriptionServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/login/prescription")
public class PrescriptionController {
    private final PrescriptionServices prescriptionService;

    public PrescriptionController(PrescriptionServices prescriptionService) {this.prescriptionService = prescriptionService;}

    @PostMapping("/take/{accessToken}")
    public ResponseEntity<Prescription> TakePrescription(@Valid @RequestBody Prescription newPrescription,@PathVariable String accessToken){
        return prescriptionService.insertPrescription(newPrescription,accessToken);
    }

    @GetMapping("/get/{doctorId}")
    public ResponseEntity<List<Prescription>> GetAllPrescription(@PathVariable String doctorId){
        return prescriptionService.getAllPrescriptionByDoctorId(doctorId);
    }

    @GetMapping("/getPrescription/{accessToken}")
    public ResponseEntity<List<Prescription>> GetPrescriptionByAccessToken(@PathVariable String accessToken){
        return prescriptionService.getByAccessToken(accessToken);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
