package com.mustafaz.telemed.exceptions;

import com.mustafaz.telemed.res.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * AccessDeniedHandler is a central place launched by ExceptionTranslationFilter
 * when 403 (not authorized) detected.
 * It decides what happens when an unauthorized user tries to access a protected resource.
 * It gives you full control over how your application responds to unauthorized access
 * Instead of the default behavior i.e 403 Forbidden in a plain text, you can customize the response
 */
@Component
@RequiredArgsConstructor
public class CustomAccessDenialHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException {

        Response<?> responseResponse = Response.builder()
                .statusCode(HttpStatus.FORBIDDEN.value()) // 403. valid jwt. but user not permitted to access the route
                .message(accessDeniedException.getMessage())
                .build();


        response.setContentType("application/json");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().write(objectMapper.writeValueAsString(responseResponse));

    }
}












