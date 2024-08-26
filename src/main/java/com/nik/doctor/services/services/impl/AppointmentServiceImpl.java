package com.nik.doctor.services.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nik.doctor.services.DTO.AppointmentDTO;
import com.nik.doctor.services.DTO.DoctorDTO;
import com.nik.doctor.services.DTO.PatientDTO;
import com.nik.doctor.services.config.AppConstants;
import com.nik.doctor.services.entities.Appointment;
import com.nik.doctor.services.entities.Doctor;
import com.nik.doctor.services.entities.Patient;
import com.nik.doctor.services.exceptions.ResourceNotFoundException;
import com.nik.doctor.services.repositories.AppointmentRepository;
import com.nik.doctor.services.repositories.DoctorRepository;
import com.nik.doctor.services.repositories.PatientRepository;
import com.nik.doctor.services.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
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
    @Autowired
    KafkaTemplate<String,Object> kafkaTemplate;

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

    @Override
    public void addAppointmentKafkaService(Appointment appointment) {
        String appointmentJson = convertToJson(convertToDTO(appointment));
        kafkaTemplate.send(AppConstants.APPOINTMENT_TOPIC_NAME, appointmentJson);
    }
    private String convertToJson(AppointmentDTO appointmentDTO) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            // Optionally, set the date format (ISO is the default)
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return objectMapper.writeValueAsString(appointmentDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert AppointmentDTO to JSON", e);
        }
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
