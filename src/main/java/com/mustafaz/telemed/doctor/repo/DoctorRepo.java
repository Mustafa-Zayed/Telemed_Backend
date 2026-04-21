package com.mustafaz.telemed.doctor.repo;

import com.mustafaz.telemed.doctor.entity.Doctor;
import com.mustafaz.telemed.enums.Specialization;
import com.mustafaz.telemed.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepo extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByUser(User user);

    List<Doctor> findBySpecialization(Specialization specialization);
}