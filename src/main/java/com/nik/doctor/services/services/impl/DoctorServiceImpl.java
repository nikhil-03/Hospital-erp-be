package com.nik.doctor.services.services.impl;

import com.nik.doctor.services.DTO.AllDoctorsDTO;
import com.nik.doctor.services.DTO.AvailableTime;
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

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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
    public List<AllDoctorsDTO> getAllDoctors() {
        return doctorRepository.findAll().stream().map(doctor -> {
            AllDoctorsDTO dto = new AllDoctorsDTO();
            dto.setId(doctor.getDoctorId());
            dto.setName(doctor.getName());
            dto.setSpecialization(doctor.getSpecialization());
            dto.setExperience(doctor.getExperience());
            dto.setEducation("MBBS, MD - " + doctor.getSpecialization()); // or map properly if stored
            dto.setImage("https://images.pexels.com/photos/4167541/pexels-photo-4167541.jpeg"); // or actual image if stored

            // Extract available days
            List<String> availableDays = doctor.getAvailability()
                    .stream()
                    .map(DoctorVisitDay::getDayOfWeek)
                    .toList();
            dto.setAvailableDays(availableDays);


            AvailableTime time = new AvailableTime();
            time.setStart(doctor.getInTiming());
            time.setEnd(doctor.getOutTiming());
            dto.setAvailableTime(time);


            // Set default consultationFee, rating, status
            dto.setConsultationFee(150); // default or get from somewhere
            dto.setRating(4.8);          // same
            dto.setStatus("active");     // or map properly
            dto.setTotalPatients(doctor.getAppointments() != null ? doctor.getAppointments().size() : 0);

            return dto;
        }).collect(Collectors.toList());
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
