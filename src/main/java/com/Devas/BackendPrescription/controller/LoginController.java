package com.Devas.BackendPrescription.controller;

import com.Devas.BackendPrescription.models.Doctor;
import com.Devas.BackendPrescription.security.JwtUtils;
import com.Devas.BackendPrescription.services.DoctorService;
import com.Devas.BackendPrescription.models.LoginRequest;
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
@RequestMapping("api/login")
public class LoginController {
    private final DoctorService doctorService;

    @Autowired
    PasswordEncoder Bcrypt;    // this retrieves password encoder object from config

    public LoginController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    public ResponseEntity LoginDoctor(@RequestBody @Valid LoginRequest loginRequest) {
        Map<String, String> errors = new HashMap<>();
        Doctor DatabaseDoctor;
        DatabaseDoctor = doctorService.loadByUsername(loginRequest.getUsername());
        if(DatabaseDoctor == null){
            errors.put("Error","Wrong Crendentials");
            return ResponseEntity.ok().body(errors);
        }
        if (Bcrypt.matches(loginRequest.getPassword(),DatabaseDoctor.getPassword())) {
            String loginToken = new JwtUtils().GenerateJwtToken(loginRequest.getUsername());
            Map map = new HashMap();
            map.put("accessToken",loginToken);
            map.put("doctorId",DatabaseDoctor.getId());
            return ResponseEntity.accepted().body(map);
        }
        errors.put("Error","Wrong Crendentials");
        return ResponseEntity.ok().body(errors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,?> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
