package com.mustafaz.telemed.security.oauth2;

import com.mustafaz.telemed.res.Response;
import com.mustafaz.telemed.users.dto.LoginResponse;
import com.mustafaz.telemed.users.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final AuthService authService;

    public CustomOAuth2SuccessHandler(@Lazy AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        if (authentication instanceof OAuth2AuthenticationToken oAuth2AuthenticationToken) {
            Response<LoginResponse> authResponse = authService.loginRegisterByGoogleOAuth2(oAuth2AuthenticationToken);

            LoginResponse loginData = authResponse.getData();

            // Redirect to the frontend with JWT token and roles as query parameters to authenticate the user
            String token = loginData.getToken();
            String roles = String.join(",", loginData.getRoles());

            String redirectUrl = "http://localhost:4200/home"
                    + "?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8)
                    + "&roles=" + URLEncoder.encode(roles, StandardCharsets.UTF_8);
            response.sendRedirect(redirectUrl);
        } else {
            response.sendRedirect("http://localhost:4200/register");
        }
    }
}