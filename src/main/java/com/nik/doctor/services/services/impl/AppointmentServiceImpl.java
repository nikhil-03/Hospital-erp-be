package com.nik.doctor.services.services.impl;

import com.nik.doctor.services.DTO.AppointmentDTO;
import com.nik.doctor.services.DTO.DoctorDTO;
import com.nik.doctor.services.DTO.PatientDTO;
import com.nik.doctor.services.entities.Appointment;
import com.nik.doctor.services.entities.Doctor;
import com.nik.doctor.services.entities.Patient;
import com.nik.doctor.services.exceptions.ResourceNotFoundException;
import com.nik.doctor.services.repositories.AppointmentRepository;
import com.nik.doctor.services.repositories.DoctorRepository;
import com.nik.doctor.services.repositories.PatientRepository;
import com.nik.doctor.services.services.AppointmentService;
import com.nik.doctor.services.services.DoctorService;
import com.nik.doctor.services.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    AppointmentRepository appointmentRepository;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    PatientRepository patientRepository;
    @Override
    public Appointment addAppointment(Appointment appointment) {
       String id =UUID.randomUUID().toString();
        Doctor doctor = doctorRepository.findById(appointment.getDoctorId())
                .orElseThrow(()->new ResourceNotFoundException("Doctor not found with ID "+appointment.getDoctorId()));
        Patient patient= patientRepository.findById(appointment.getPatientId())
                .orElseThrow(()-> new ResourceNotFoundException("Patient not found with ID "+appointment.getPatientId()));

        appointment.setAppointmentId(id);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        return appointmentRepository.save(appointment);
    }

    @Override
    public List<AppointmentDTO> getAllAppointment() {
        List<Appointment> appointment = appointmentRepository.findAll();
        return appointment.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public AppointmentDTO getAppointmentById(String id) {
        return convertToDTO(appointmentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("appointment not found by ID "+id)));
    }
    private AppointmentDTO convertToDTO(Appointment appointment) {
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setDoctorId(appointment.getDoctor().getDoctorId());
        doctorDTO.setName(appointment.getDoctor().getName());
        doctorDTO.setSpecialization(appointment.getDoctor().getSpecialization());
        // Patient Information
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setPatientId(appointment.getPatient().getPatientId());
        patientDTO.setName(appointment.getPatient().getName());
        patientDTO.setAge(appointment.getPatient().getAge());
        patientDTO.setMobileNo(appointment.getPatient().getMobileNo());

        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setAppointmentId(appointment.getAppointmentId());
        appointmentDTO.setDate(appointment.getDate());
        appointmentDTO.setTime(appointment.getTime());
        appointmentDTO.setDescription(appointment.getDescription());
        appointmentDTO.setDoctor(doctorDTO);
        appointmentDTO.setPatient(patientDTO);




        return appointmentDTO;
    }
}
