package com.nik.doctor.services.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="appointment_table")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "appointmentId")
public class Appointment {
  @Id
  private String appointmentId;
  @ManyToOne
  @JoinColumn(name="doctor_id")
  private Doctor doctor;

  @ManyToOne
  @JoinColumn(name = "patient_id")
  private Patient patient;

  private LocalDate date;
  private LocalTime time;
  private String description;
  @Transient
  private String doctorId;

  @Transient
  private String patientId;
}
