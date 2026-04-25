package com.mustafaz.telemed.users.service;

import com.mustafaz.telemed.doctor.entity.Doctor;
import com.mustafaz.telemed.doctor.repo.DoctorRepo;
import com.mustafaz.telemed.exceptions.BadRequestException;
import com.mustafaz.telemed.exceptions.NotFoundException;
import com.mustafaz.telemed.notifications.dto.NotificationDTO;
import com.mustafaz.telemed.notifications.service.NotificationService;
import com.mustafaz.telemed.patient.entity.Patient;
import com.mustafaz.telemed.patient.repo.PatientRepo;
import com.mustafaz.telemed.res.Response;
import com.mustafaz.telemed.role.entity.Role;
import com.mustafaz.telemed.role.repo.RoleRepo;
import com.mustafaz.telemed.security.JwtService;
import com.mustafaz.telemed.users.dto.LoginRequest;
import com.mustafaz.telemed.users.dto.LoginResponse;
import com.mustafaz.telemed.users.dto.RegistrationRequest;
import com.mustafaz.telemed.users.entity.User;
import com.mustafaz.telemed.users.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final PatientRepo patientRepo;
    private final DoctorRepo doctorRepo;

    @Value("${login.link}")
    private String loginLink;

    @Override
    public Response<String> register(RegistrationRequest request) {
        // 1. Check if user already exists
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("User with this email already exists");
        }

        // Determine the roles to assign. Default to PATIENT if none are provided.
        List<String> requestedRoleNames = (request.getRoles() != null && !request.getRoles().isEmpty())
                ? request.getRoles().stream().map(String::toUpperCase).toList()
                : List.of("PATIENT");

        boolean isDoctor = requestedRoleNames.contains("DOCTOR");

        if (isDoctor && (request.getLicenseNumber() == null || request.getLicenseNumber().isBlank())) {
            throw new BadRequestException("License number required to register a doctor.");
        }

        // 2. Load and validate roles from the database
        List<Role> roles = requestedRoleNames.stream()
                .map(roleRepo::findByName)
                .flatMap(Optional::stream)
                .toList();

        if (roles.isEmpty()) {
            throw new NotFoundException("Registration failed: Requested roles were not found in the database.");
        }

        // 3. Create and save new user entity
        User newUser = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .roles(roles)
                .build();

        User savedUser = userRepo.save(newUser);

        log.info("New user registered: {} with {} roles.", savedUser.getEmail(), roles.size());

        // 4. Process Profile Creation
        for (Role role : roles) {
            String roleName = role.getName();

            switch (roleName) {
                case "PATIENT":
                    createPatientProfile(savedUser);
                    log.info("Patient profile created: {}", savedUser.getEmail());
                    break;

                case "DOCTOR":
                    createDoctorProfile(request, savedUser);
                    log.info("Doctor profile created: {}", savedUser.getEmail());
                    break;

                case "ADMIN":
                    log.info("Admin role assigned to user: {}", savedUser.getEmail());
                    break;

                default:
                    log.warn("Assigned role '{}' has no corresponding profile creation logic.", roleName);
                    break;
            }
        }

        // 5. Send welcome email out
        sendRegistrationEmail(request, savedUser);

        // 6. Return success response
        return Response.<String>builder()
                .statusCode(200)
                .message("Registration successful. A welcome email has been sent to you.")
                .data(savedUser.getEmail())
                .build();
    }

    @Override
    public Response<LoginResponse> login(LoginRequest loginRequest) {
        String userEmail = loginRequest.getEmail();
        String userPassword = loginRequest.getPassword();

//        // authenticate is best practice
//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            throw new BadRequestException("Password doesn't match");
//        }

        authenticate(userEmail, userPassword);
        // at this point here, the user has been authenticated and the username and password are correct.

        User user = userRepo.findByEmail(loginRequest.getEmail()).orElseThrow(
                () -> new NotFoundException("User not found")
        );

        String newGeneratedToken = jwtService.generateToken(user.getEmail());

        LoginResponse loginResponse = LoginResponse.builder()
                .roles(user.getRoles().stream().map(Role::getName).toList())
                .token(newGeneratedToken)
                .build();

        return Response.<LoginResponse>builder()
                .statusCode(200)
                .message("Login Successful")
                .data(loginResponse)
                .build();
    }

    private void createPatientProfile(User user) {
        Patient patient = Patient.builder()
                .user(user)
                .build();

        patientRepo.save(patient);
        log.info("Patient profile created");
    }

    private void createDoctorProfile(RegistrationRequest request, User user) {
        Doctor doctor = Doctor.builder()
                .specialization(request.getSpecialization())
                .licenseNumber(request.getLicenseNumber())
                .user(user)
                .build();

        doctorRepo.save(doctor);
    }

    private void sendRegistrationEmail(RegistrationRequest request, User user) {
        NotificationDTO welcomeEmail = NotificationDTO.builder()
                .recipient(user.getEmail())
                .subject("Welcome to Telemed Health!")
                .templateName("welcome")
                .message("Thank you for registering Your account is ready.")
                .templateVariables(Map.of(
                        "name", request.getName(),
                        "loginLink", loginLink
                ))
                .build();

        notificationService.sendEmail(welcomeEmail, user);
    }

    /**
     * <p><strong>authenticationManager.authenticate():</strong></p>
     * <ul>
     *   <li>✅ Uses the configured UserDetailsService to load the user</li>
     *   <li>✅ Uses your PasswordEncoder automatically</li>
     *   <li>✅ Handles:
     *     <ul>
     *       <li>disabled users</li>
     *       <li>locked accounts</li>
     *       <li>expired credentials</li>
     *     </ul>
     *   </li>
     *   <li>✅ Integrates with filters &amp; security context</li>
     * </ul>
     */
    private void authenticate(String userEmail, String userPassword) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userEmail, userPassword)
            );
        } catch (DisabledException e) {
            throw new DisabledException("User is disabled", e);
        } catch (BadCredentialsException e) {
            throw new BadRequestException("Password doesn't match");
        }
    }
}