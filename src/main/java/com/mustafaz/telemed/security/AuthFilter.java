package com.mustafaz.telemed.security;

import com.mustafaz.telemed.exceptions.CustomAuthenticationEntryPoint;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * OncePerRequestFilter is a filter based class only applied once per request. This means that even if the filter
 * chain is called multiple times during the processing of a single request, this will only execute once.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {
    private final JwtService tokenService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String token = getTokenFromRequest(request);

        try {
            // Only proceed if token exists AND user is not already authenticated
            if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                String email;
                try {
                    email = tokenService.getUsernameFromToken(token);
                } catch (Exception e) {
                    log.error("Exception occurred while extracting username from token");
                    AuthenticationException authenticationException = new BadCredentialsException(e.getMessage());
                    customAuthenticationEntryPoint.commence(request, response, authenticationException);
                    return;
                }

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
                if (!StringUtils.hasText(email) || !tokenService.isTokenValid(token, userDetails)) {
                    try {
                        filterChain.doFilter(request, response);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        return;
                    }
                }

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            try {
                filterChain.doFilter(request, response);
            } catch (Exception e) {
                log.error(e.getMessage());
            }

        } catch (ExpiredJwtException ex) {
            log.warn("JWT expired: {}", ex.getMessage());
            customAuthenticationEntryPoint.commence(
                    request, response,
                    new BadCredentialsException("JWT token expired", ex)
            );

        } catch (JwtException | IllegalArgumentException ex) {
            log.warn("JWT error: {}", ex.getMessage());
            customAuthenticationEntryPoint.commence(
                    request, response,
                    new BadCredentialsException("Invalid JWT token", ex)
            );
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String tokenWithBearer = request.getHeader("Authorization");

        if (tokenWithBearer != null && tokenWithBearer.startsWith("Bearer ")) {
            return tokenWithBearer.substring(7);
        }
        return null;
    }
}