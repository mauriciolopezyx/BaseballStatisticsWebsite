package com.auth_example.baseballguru.config;

import com.auth_example.baseballguru.model.User;
import com.auth_example.baseballguru.repository.UserRepository;
import com.auth_example.baseballguru.service.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JWTService jwtService;
    private final UserRepository userRepository;

    public OAuth2AuthenticationSuccessHandler(JWTService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException  {
        OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) authentication;
        String registrationId = oAuth2Token.getAuthorizedClientRegistrationId(); // "google"

        Map<String, Object> attributes = oAuth2Token.getPrincipal().getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String googleId = (String) attributes.get("sub"); // Google's user ID

        System.out.println("Received oAuthSuccess: " + email + " " + name + " " + googleId);

        try {
            User user = userRepository.findByEmail(email).orElseGet(() -> createNewOAuth2User(email, name, registrationId, googleId));

            // Update OAuth2 info if user exists but wasn't OAuth2 before
            if ("local".equals(user.getProvider())) {
                user.setProvider(registrationId);
                user.setProviderId(googleId);
                userRepository.save(user);
            }

            // Generate JWT tokens and HTTP only cookie (same as the existing system)
            String jwtToken = jwtService.generateToken(user);
            long ttlMs = jwtService.getExpirationTime();
            ResponseCookie cookie = ResponseCookie
                    .from("jwt", jwtToken)
                    .httpOnly(true)
                    .secure(false) // Set to true in production
                    .sameSite(SameSiteCookies.STRICT.toString())
                    .path("/")
                    .maxAge(Duration.ofMillis(ttlMs))
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            response.sendRedirect("http://localhost:5173"); // redirects user to home page

        } catch (Exception e) {
            System.out.println("OAuth2 authentication failed: " + e.getMessage());
            response.sendRedirect("http://localhost:5173/login?error=oauth2_failed");
        }
    }


    private User createNewOAuth2User(String email, String name, String provider, String providerId) {
        // will generate default username
        String username = generateUniqueUsername(email, name);

        User newUser = new User(username, email, provider, providerId);
        return userRepository.save(newUser);
    }

    private String generateUniqueUsername(String email, String name) {
        // Try email prefix first
        String baseUsername = email.split("@")[0];

        // If that doesn't work, try name
        if (baseUsername.length() < 3 && name != null) {
            baseUsername = name.toLowerCase().replaceAll("\\s+", "");
        }

        // Ensure uniqueness
        String username = baseUsername;
        int counter = 1;
        while (userRepository.findByUsername(username).isPresent()) {
            username = baseUsername + counter;
            counter++;
        }

        return username;
    }

}
