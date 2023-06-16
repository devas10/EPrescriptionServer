package com.Devas.BackendPrescription;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class BackendPrescriptionApplication{
	public static void main(String[] args) {
		SpringApplication.run(BackendPrescriptionApplication.class, args);
		System.out.println("E-Prescription back-end is up...");
	}
}