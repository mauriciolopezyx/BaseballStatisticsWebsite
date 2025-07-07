package com.auth_example.baseballguru.service;

import com.auth_example.baseballguru.dto.LoginUserDto;
import com.auth_example.baseballguru.dto.RegisterUserDto;
import com.auth_example.baseballguru.dto.VerifyUserDto;
import com.auth_example.baseballguru.model.User;
import com.auth_example.baseballguru.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final JWTService jwtService;

    public AuthenticationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            EmailService emailService,
            JWTService jwtService
    )
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.jwtService = jwtService;
    }

    // will be called when user first attempts to register
    public User register(RegisterUserDto input) {
        System.out.println(input.email());
        Optional<User> userFromEmail = userRepository.findByEmail(input.email());
        if (userFromEmail.isPresent()) {
            throw new RuntimeException("An account with the given email already exists");
        }
        Optional<User> userFromName = userRepository.findByUsername(input.username());
        if (userFromName.isPresent()) {
            throw new RuntimeException("An account with the given username already exists");
        }
        User user = new User(input.username(), input.email(), passwordEncoder.encode(input.password()));
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        sendVerificationEmail(user);
        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
        User user = userRepository.findByEmail(input.email())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.isEnabled()) {
            throw new RuntimeException("User is not verified - please verify to continue");
        }

        if (!user.hasPassword()) {
            throw new RuntimeException("This account uses " + user.getProvider() + " login. Please login with " + user.getProvider() + " and then reset your password after.");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.email(),
                        input.password()
                )
        );
        return user;
    }

    public void verifyUser(VerifyUserDto input) {
        Optional<User> optionalUser = userRepository.findByEmail(input.email());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isEnabled()) {
                throw new RuntimeException("Account already verified");
            }
            if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code has expired");
            }
            if (user.getVerificationCode().equals(input.verificationCode())) {
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                userRepository.save(user);
            } else {
                throw new RuntimeException("Invalid verification code");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void resendVerificationCode(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isEnabled()) {
                throw new RuntimeException("User is already verified");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
            sendVerificationEmail(user);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public ResponseEntity<?> refreshOrValidate(String token) {
        // 1) No cookie at all ➜ not authenticated
        System.out.println(token);
        if (token == null || token.isBlank()) {
            System.out.println("Token does not exist");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {

            String username = jwtService.extractUsername(token);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException(username));

            // 3) Completely invalid or tampered ➜ 401
            if (!jwtService.isTokenValid(token, user)) {
                System.out.println("JWT rejected");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Map<String, Object> userResponse = Map.of(
                    "username", user.getUsername(),
                    "authorities", user.getAuthorities(),
                    "authenticated", true
            );

            // 4) Check how close we are to expiry
            Instant exp = jwtService.extractRelativeExpiration(token);
            long remainingMs = Duration.between(Instant.now(), exp).toMillis();

            // Renew if < 5 minutes remain
            final long RENEW_THRESHOLD_MS = 5 * 60_000;

            if (remainingMs < RENEW_THRESHOLD_MS) {

                // 4a) Generate fresh token with same TTL
                long ttlMs = jwtService.getExpirationTime();   // e.g. 15 min
                String newToken = jwtService.generateToken(user);

                ResponseCookie cookie = ResponseCookie.from("jwt", newToken)
                        .httpOnly(true)
                        .secure(false) // TESTING, production would be true
                        .sameSite("Strict")
                        .path("/")
                        .maxAge(Duration.ofMillis(ttlMs))
                        .build();

                // 4b) Return 200 + new cookie
                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, cookie.toString())
                        .build();
            }

            System.out.println("Successfully verified user!");

            // 5) Still plenty of time left ➜ return user data
            return ResponseEntity.ok(userResponse);

        } catch (Exception ex) {
            // any parsing / lookup error ➜ 401
            System.out.println("Verification failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public void sendVerificationEmail(User user) {
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE " + user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }


}
