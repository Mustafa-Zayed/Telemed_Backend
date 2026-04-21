package com.mustafaz.telemed.patient.repo;

import com.mustafaz.telemed.patient.entity.Patient;
import com.mustafaz.telemed.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepo extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUser(User user);
}