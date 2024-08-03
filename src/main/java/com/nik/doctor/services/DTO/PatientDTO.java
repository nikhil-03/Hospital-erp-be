package com.nik.doctor.services.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientDTO {
    private String patientId;
    private String name;
    private int age;
    private String mobileNo;

    // Constructors, getters, and setters
}

