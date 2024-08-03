package com.nik.doctor.services.controllers;

import com.nik.doctor.services.DTO.AppointmentDTO;
import com.nik.doctor.services.entities.Appointment;
import com.nik.doctor.services.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    @Autowired
    AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<Appointment> addAppointment(@RequestBody Appointment appointment){
        Appointment a =  appointmentService.addAppointment(appointment);
        return ResponseEntity.status(HttpStatus.CREATED).body(a);
    }
    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        List<AppointmentDTO> a = appointmentService.getAllAppointment();
        return ResponseEntity.ok(a);
    }
    @GetMapping("/{Id}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable String Id){
        AppointmentDTO a = appointmentService.getAppointmentById(Id);
        return ResponseEntity.ok(a);
    }
}
