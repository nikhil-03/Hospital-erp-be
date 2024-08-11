package com.nik.doctor.services.services.impl;

import com.nik.doctor.services.DTO.AppointmentDoctorDTO;
import com.nik.doctor.services.DTO.AppointmentPatientDTO;
import com.nik.doctor.services.entities.Appointment;
import com.nik.doctor.services.entities.Doctor;
import com.nik.doctor.services.entities.Patient;
import com.nik.doctor.services.exceptions.ResourceNotFoundException;
import com.nik.doctor.services.repositories.PatientRepository;
import com.nik.doctor.services.services.PatientService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    PatientRepository patientRepository;

    @Override
    @Transactional
    public Patient addPatient(Patient patient) {
        String id = UUID.randomUUID().toString();
        patient.setPatientId(id);
        if (patient.getAppointments() != null) {
            for (Appointment appointment : patient.getAppointments()) {
                if (appointment.getAppointmentId() == null) {
                    appointment.setAppointmentId(UUID.randomUUID().toString());
                }
                appointment.setPatient(patient);
            }
        }
        return patientRepository.save(patient);
    }

    @Override
    public List<Patient> getAllPatient() {
        return patientRepository.findAll();
    }

    @Override
    public Patient getPatientById(String id) {

        return patientRepository.findById((id)).orElseThrow(()->new ResourceNotFoundException("Patient with "+id+" not found"));
    }

    @Override
    public List<AppointmentPatientDTO> getAllPatientForAppointment() {
        List<Patient> p = patientRepository.findAll();
        return p.stream().map(this::convertToAppointmentPatient).collect(Collectors.toList());

    }

    public AppointmentPatientDTO convertToAppointmentPatient(Patient patient){
        AppointmentPatientDTO appointmentPatientDTO = new AppointmentPatientDTO();
        appointmentPatientDTO.setPatientId(patient.getPatientId());
        appointmentPatientDTO.setName(patient.getName());
        appointmentPatientDTO.setAge(patient.getAge());
        appointmentPatientDTO.setAdharNo(patient.getAdharNo());
        appointmentPatientDTO.setMobileNo(patient.getMobileNo());
        return appointmentPatientDTO;
    }
}
