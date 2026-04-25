package com.mustafaz.telemed.users.service;

import com.mustafaz.telemed.res.Response;
import com.mustafaz.telemed.users.dto.RegistrationRequest;

public interface AuthService {
    Response<String> register(RegistrationRequest request);
}