package com.nik.doctor.services.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="patient_table")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "patientId")
public class Patient {
    @Id
    private String patientId;
    private String name;
    private String email;
    private int age;
    private String mobileNo;
    private String adharNo;
    private String gender;
    private String bloodGroup;
    private int pinCode;
    private String description;
    private String address;


    @OneToMany(mappedBy = "patient")

    private List<Appointment> appointments;
}
