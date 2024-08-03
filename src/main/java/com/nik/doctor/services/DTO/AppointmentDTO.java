package com.nik.doctor.services.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDTO {
    private String appointmentId;
    private LocalDate date;
    private LocalTime time;
    private String description;
    private DoctorDTO doctor;
    private PatientDTO patient;

    // Constructors, getters, and setters
}

