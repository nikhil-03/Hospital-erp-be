package com.nik.doctor.services.controllers;

import com.nik.doctor.services.entities.Doctor;
import com.nik.doctor.services.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @PostMapping
    public ResponseEntity<Doctor> addDoctor(@RequestBody Doctor doctor){
        Doctor d1 = doctorService.saveDoctor(doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(d1);
    }
    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors(){
        List<Doctor> d = doctorService.getAllDoctor();
        return ResponseEntity.ok(d);
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable String doctorId){
        Doctor d = doctorService.getDoctor(doctorId);
        return ResponseEntity.ok(d);
    }
}
