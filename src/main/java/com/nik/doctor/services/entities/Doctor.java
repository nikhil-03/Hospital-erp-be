package com.nik.doctor.services.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="doctor_table")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "doctorId")
public class Doctor {
    @Id
    private String doctorId;
    private String name;
    private int age;
    private String specialization;
    private int experience;
    private String contactNo;
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)

    private List<DoctorVisitDay> availability;
    private LocalTime inTiming;
    private LocalTime outTiming;
    private String email;
    private String description;
    @OneToMany(mappedBy = "doctor",cascade = CascadeType.ALL) // field from appointment

    private List<Appointment> appointments;
}
