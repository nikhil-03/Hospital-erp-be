package com.nik.doctor.services.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AllDoctorsDTO {
    private String id;
    private String name;
    private String specialization;
    private int experience;
    private String education;
    private String image;
    private List<String> availableDays;
    private AvailableTime availableTime;
    private double consultationFee;
    private double rating;
    private int totalPatients;
    private String status;

}
