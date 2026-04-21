package com.mustafaz.telemed.users.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mustafaz.telemed.enums.Specialization;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private Specialization specialization; // If a doctor, specify his specialization

    private String licenseNumber; // If a doctor, license number of the doctor
    private List<String> roles;
}