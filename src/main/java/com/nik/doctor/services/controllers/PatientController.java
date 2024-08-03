package com.nik.doctor.services.controllers;


import com.nik.doctor.services.entities.Patient;
import com.nik.doctor.services.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {
    @Autowired
    PatientService patientService;

    @PostMapping
    public ResponseEntity<Patient> addPatient(@RequestBody Patient patient){
        patientService.addPatient(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(patient);
    }
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatient(){
        List<Patient> p = patientService.getAllPatient();
        return ResponseEntity.ok(p);
    }
    @GetMapping("/{patientId}")
    public ResponseEntity<Patient> getPatientById(@PathVariable String patientId){
        Patient p = patientService.getPatientById(patientId);
        return ResponseEntity.ok(p);
    }

}
