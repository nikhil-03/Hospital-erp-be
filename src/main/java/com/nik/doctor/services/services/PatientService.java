package com.nik.doctor.services.services;

import com.nik.doctor.services.entities.Patient;

import java.util.List;

public interface PatientService {
    Patient addPatient(Patient patient);

    List<Patient> getAllPatient();

    Patient getPatientById(String id);
}
