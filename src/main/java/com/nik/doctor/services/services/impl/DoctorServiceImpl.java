package com.nik.doctor.services.services.impl;

import com.nik.doctor.services.exceptions.ResourceNotFoundException;
import com.nik.doctor.services.DTO.AppointmentDoctorDTO;
import com.nik.doctor.services.services.DoctorService;
import com.nik.doctor.services.config.AppConstants;
import com.nik.doctor.services.entities.Appointment;
import com.nik.doctor.services.entities.Doctor;
import com.nik.doctor.services.entities.DoctorVisitDay;
import com.nik.doctor.services.repositories.DoctorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

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

    @Override
    public List<AppointmentDoctorDTO> getAllDoctorForAppointment() {
        List<Doctor> d = doctorRepository.findAll();
        return d.stream().map(this::convertToAppointmentDTO).collect(Collectors.toList());
    }

    @Override
    public void addDoctorApiHitKafka() {
        kafkaTemplate.send(AppConstants.DOCTOR_API_HIT_TOPIC_NAME, LocalTime.now().toString());
    }


    public AppointmentDoctorDTO convertToAppointmentDTO(Doctor doctor){
        AppointmentDoctorDTO appointmentDoctorDTO=new AppointmentDoctorDTO();

        appointmentDoctorDTO.setDoctorId(doctor.getDoctorId());
        appointmentDoctorDTO.setName(doctor.getName());
        appointmentDoctorDTO.setSpecialization(doctor.getSpecialization());
        appointmentDoctorDTO.setInTiming(doctor.getInTiming());
        List<String> daysOfWeek = doctor.getAvailability().stream()
                .map(DoctorVisitDay::getDayOfWeek)
                .collect(Collectors.toList());
        appointmentDoctorDTO.setDays(daysOfWeek);
        return appointmentDoctorDTO;
    }
}
