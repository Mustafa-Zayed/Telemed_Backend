package com.mustafaz.telemed.patient.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mustafaz.telemed.enums.BloodGroup;
import com.mustafaz.telemed.enums.Genotype;
import com.mustafaz.telemed.users.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PatientDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String phone;

    private String knownAllergies;

    private BloodGroup bloodGroup;

    private Genotype genotype;

    private UserDTO user;
}