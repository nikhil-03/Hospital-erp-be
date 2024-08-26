package com.nik.doctor.services.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "visit_day_table")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class DoctorVisitDay {
        @Id
        private String id;  // Change this to String, no auto-generation strategy needed

        private String dayOfWeek; // e.g., "Monday", "Tuesday", etc.

        @ManyToOne
        @JoinColumn(name = "doctor_id")

        private Doctor doctor;

        @PrePersist
        public void generateId() {
                if (this.id == null) {
                        this.id = UUID.randomUUID().toString(); // Generate a random UUID
                }
        }
}
