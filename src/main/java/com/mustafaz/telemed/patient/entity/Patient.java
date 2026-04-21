package com.mustafaz.telemed.patient.entity;


import com.mustafaz.telemed.appointment.entity.Appointment;
import com.mustafaz.telemed.enums.BloodGroup;
import com.mustafaz.telemed.enums.Genotype;
import com.mustafaz.telemed.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String phone;

    @Lob // Stores allergies as a comma-separated string
    private String knownAllergies;

    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;

    @Enumerated(EnumType.STRING)
    private Genotype genotype;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Appointment> Appointment;
}