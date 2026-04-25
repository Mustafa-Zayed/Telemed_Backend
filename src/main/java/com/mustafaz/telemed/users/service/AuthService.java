package com.mustafaz.telemed.users.service;

import com.mustafaz.telemed.res.Response;
import com.mustafaz.telemed.users.dto.LoginRequest;
import com.mustafaz.telemed.users.dto.LoginResponse;
import com.mustafaz.telemed.users.dto.RegistrationRequest;
import com.mustafaz.telemed.users.dto.ResetPasswordRequest;

public interface AuthService {
    Response<String> register(RegistrationRequest request);

    Response<LoginResponse> login(LoginRequest loginRequest);

    Response<?> forgetPassword(String email);

    Response<?> updatePasswordViaResetCode(ResetPasswordRequest resetPasswordRequest);
}