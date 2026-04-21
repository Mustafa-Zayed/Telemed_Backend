package com.mustafaz.telemed.appointment.repo;

import com.mustafaz.telemed.appointment.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepo extends JpaRepository<Appointment, Long> {
    //fetch appointment of the doctor
    List<Appointment> findByDoctor_User_IdOrderByIdDesc(Long userId);

    //fetch appointment of the patient
    List<Appointment> findByPatient_User_IdOrderByIdDesc(Long userId);

    /**
     * Determines whether two time ranges overlap.
     *
     * <p>An existing appointment is considered conflicting if:
     * <pre>
     *     existingStart < newEnd AND existingEnd > newStart
     * </pre>
     *
     * <p>This condition covers all overlap scenarios:
     * <ul>
     *     <li>The new appointment starts during an existing appointment</li>
     *     <li>The new appointment ends during an existing appointment</li>
     *     <li>The new appointment completely contains an existing appointment</li>
     *     <li>The new appointment is completely contained within an existing appointment</li>
     * </ul>
     */
    @Query("SELECT a FROM Appointment a " +
            "WHERE a.doctor.id = :doctorId " +
            "AND a.status = 'SCHEDULED' " + // Only check for scheduled/confirmed appointments
            "AND (" +
            // Case 1: Existing appointment starts during the new slot
            "    (a.startTime < :newEndTime AND a.endTime > :newStartTime)" +
            ")")
    List<Appointment> findConflictingAppointments(
            @Param("doctorId") Long doctorId,
            @Param("newStartTime") LocalDateTime newStartTime,
            @Param("newEndTime") LocalDateTime newEndTime
    );
}