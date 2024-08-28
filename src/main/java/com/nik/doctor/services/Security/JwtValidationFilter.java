package com.nik.doctor.services.Security;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
@Component
public class JwtValidationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractTokenFromHeader(request);
        if (token != null) {
            // Make a call to the authentication microservice to validate the token
            boolean isValid = validateTokenWithAuthMicroservice(token);
            System.out.println("token got "+isValid+"->"+token);
            if (isValid) {
                logger.debug("Token is valid");

                // Extract user details from the token or from a user service if required
                // For simplicity, assuming token includes username directly
                String username = "nikhil";//extractUsernameFromToken(token); // Implement this method if needed

                // Define authorities if you have roles or permissions in the token
                List<GrantedAuthority> authorities = Collections.emptyList(); // Adjust if you have roles

                // Create an authentication object
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                // Set authentication in the security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
    private String extractTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
    private boolean validateTokenWithAuthMicroservice(String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> request = new HttpEntity<>(token, headers);
        try {
            //    @Value("${auth.service.url}") // URL to your authentication service
            String authServiceUrl = "http://localhost:8082";
            ResponseEntity<Boolean> response = restTemplate.exchange(
                    authServiceUrl + "/auth/validate",
                    HttpMethod.POST,
                    request,
                    Boolean.class
            );
            return response.getStatusCode() == HttpStatus.OK && Boolean.TRUE.equals(response.getBody());
        } catch (Exception e) {
            // Log and handle exceptions
            return false;
        }
    }
}
