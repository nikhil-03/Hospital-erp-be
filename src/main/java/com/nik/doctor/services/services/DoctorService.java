package com.nik.doctor.services.services;

import com.nik.doctor.services.DTO.AppointmentDoctorDTO;
import com.nik.doctor.services.entities.Doctor;

import java.util.List;

public interface DoctorService {
   Doctor saveDoctor(Doctor doctor);
   List<Doctor> getAllDoctor();
   Doctor getDoctor(String doctorId);

   List<AppointmentDoctorDTO> getAllDoctorForAppointment();

   void addDoctorApiHitKafka();
//   List<Doctor> addDoctorList(List<Doctor> doctors);

}
