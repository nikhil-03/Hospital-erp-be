package com.nik.doctor.services.services;

import com.nik.doctor.services.DTO.AppointmentDTO;
import com.nik.doctor.services.entities.Appointment;

import java.util.List;

public interface AppointmentService {
    Appointment addAppointment(Appointment appointment);

    List<AppointmentDTO> getAllAppointment();

    AppointmentDTO getAppointmentById(String id);

    void addAppointmentKafkaService(Appointment appointment);
}
