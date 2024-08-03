package com.nik.doctor.services.repositories;

import com.nik.doctor.services.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor,String> {
}
