package com.mustafaz.telemed.users.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mustafaz.telemed.role.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // Ignore unknown fields that don’t exist in this class from being serialized or deserialized without error
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from JSON serialization
public class UserDTO {

    private Long id;

    private String name;

    private String email;

    @JsonIgnore
    private String password; // not included in JSON response, better remove it than @JsonIgnore

    private String profilePictureUrl;

    private List<Role> roles;
}