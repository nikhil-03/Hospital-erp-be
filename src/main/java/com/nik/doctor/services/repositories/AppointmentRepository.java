package com.nik.doctor.services.repositories;

import com.nik.doctor.services.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment,String> {
}
