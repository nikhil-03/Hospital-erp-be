package com.nik.doctor.services.services.impl;

import com.nik.doctor.services.entities.Appointment;
import com.nik.doctor.services.entities.Doctor;
import com.nik.doctor.services.entities.DoctorVisitDay;
import com.nik.doctor.services.exceptions.ResourceNotFoundException;
import com.nik.doctor.services.repositories.DoctorRepository;
import com.nik.doctor.services.services.DoctorService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DoctorServiceImpl implements DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    @Transactional
    public Doctor saveDoctor(Doctor doctor) {
        String randomId = UUID.randomUUID().toString();
        doctor.setDoctorId(randomId);
        if (doctor.getAvailability() != null) {
            for (DoctorVisitDay doctorVisitDay : doctor.getAvailability()) {
                doctorVisitDay.setDoctor(doctor);
            }
        }

        if (doctor.getAppointments() != null) {
            for (Appointment appointment : doctor.getAppointments()) {
                if (appointment.getAppointmentId() == null) {
                    appointment.setAppointmentId(UUID.randomUUID().toString());
                }
                appointment.setDoctor(doctor);
            }
        }
        return doctorRepository.save(doctor);
    }
    @Override
    public List<Doctor> getAllDoctor() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor getDoctor(String doctorId) {
        return doctorRepository.findById(doctorId).orElseThrow(()->new ResourceNotFoundException("doctor with "+doctorId));
    }
}
