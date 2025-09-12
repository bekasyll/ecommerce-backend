package com.bekassyl.ecommerceapp.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.bekassyl.ecommerceapp.security.JwtUtil;
import com.bekassyl.ecommerceapp.service.AppUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final AppUserDetailsService appUserDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            if(jwt.isBlank()) {
                writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token in Bearer Header!");
                return;
            } else {
                try {
                    String email = jwtUtil.validateTokenAndRetrieveEmail(jwt);
                    UserDetails userDetails = appUserDetailsService.loadUserByUsername(email);

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );

                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } catch (JWTVerificationException e) {
                    writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token!");
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private void writeErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");

        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("message", message);
        errorBody.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        objectMapper.writeValue(response.getOutputStream(), errorBody);
    }
}
