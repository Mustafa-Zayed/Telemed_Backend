package com.mustafaz.telemed.appointment.service;

import com.mustafaz.telemed.appointment.dto.AppointmentDTO;
import com.mustafaz.telemed.res.Response;

import java.util.List;

public interface AppointmentService {
    Response<AppointmentDTO> bookAppointment(AppointmentDTO appointmentDTO);

    Response<List<AppointmentDTO>> getMyAppointments();

    Response<?> cancelAppointment(Long appointmentId);

    Response<?> completeAppointment(Long appointmentId);
}