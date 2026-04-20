package com.mustafaz.telemed.exceptions;

import com.mustafaz.telemed.res.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * AuthenticationEntryPoint is a central place launched by ExceptionTranslationFilter
 * when 401 (not authenticated at all) detected.
 * It decides what happens when an unauthenticated user tries to
 * access a protected resource.
 * It gives you full control over how your application responds to unauthorized access
 * Instead of the default behavior i.e redirects to /login or shows 401 error page, you can customize the response
 * such as sending a JSON response to the frontend
 *
 */
@RequiredArgsConstructor
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException {

        Response<?> responseResponse = Response.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value()) // 401. invalid token
                .message(authException.getMessage())
                .build();

        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(objectMapper.writeValueAsString(responseResponse));
    }
}












