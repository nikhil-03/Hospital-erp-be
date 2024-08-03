package com.nik.doctor.services.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {
    private String doctorId;
    private String name;
    private String specialization;
    // Add other fields as needed

    // Constructors, getters, and setters
}