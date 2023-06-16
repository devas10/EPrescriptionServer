package com.Devas.BackendPrescription.controller;

import com.Devas.BackendPrescription.models.Doctor;
import com.Devas.BackendPrescription.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/register")
public class RegistrationController {

    private final DoctorService doctorService;
    @Autowired
    PasswordEncoder Bcrypt;     // this retrieves password encoder object from config

    public RegistrationController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    public ResponseEntity RegisterDoctor(@Valid @RequestBody Doctor newDoctor){
        newDoctor.setPassword(Bcrypt.encode(newDoctor.getPassword()));
        String accessToken = doctorService.insertDoctors(newDoctor);
        if (accessToken.equals("Username Exists")){
            Map map = new HashMap();
            map.put("Error","User Already Exists");
            return ResponseEntity.accepted().body(map);
        }
        Map map = new HashMap();
        map.put("accessToken",accessToken);
        map.put("doctorId",newDoctor.getId());
        return ResponseEntity.accepted().body(map);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
