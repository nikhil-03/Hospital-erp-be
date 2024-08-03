package com.nik.doctor.services.repositories;

import com.nik.doctor.services.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient,String> {
}
