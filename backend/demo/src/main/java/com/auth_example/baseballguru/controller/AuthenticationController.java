package com.auth_example.baseballguru.controller;

import com.auth_example.baseballguru.dto.LoginUserDto;
import com.auth_example.baseballguru.dto.RegisterUserDto;
import com.auth_example.baseballguru.dto.VerifyUserDto;
import com.auth_example.baseballguru.model.User;
import com.auth_example.baseballguru.responses.LoginResponse;
import com.auth_example.baseballguru.service.AuthenticationService;
import com.auth_example.baseballguru.service.JWTService;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JWTService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JWTService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUserDto registerUserDto) {
        try {
            User registeredUser = authenticationService.register(registerUserDto);
            return ResponseEntity.ok(Map.of("email", registeredUser.getEmail()));
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserDto loginUserDto) {
        System.out.println("Received login request " + System.currentTimeMillis());
        try {
            User authenticatedUser = authenticationService.authenticate(loginUserDto);
            String token = jwtService.generateToken(authenticatedUser);
            long ttlMs = jwtService.getExpirationTime();

            ResponseCookie cookie = ResponseCookie
                    .from("jwt", token)
                    .httpOnly(true)
                    .secure(false) // TESTING, production would be true
                    .sameSite(SameSiteCookies.STRICT.toString())
                    .path("/")
                    .maxAge(Duration.ofMillis(ttlMs))
                    .build();

            LoginResponse loginResponse = new LoginResponse(token, jwtService.getExpirationTime());

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(loginResponse);
        } catch (RuntimeException e) {
            System.out.println("Here vvv");
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Create a cookie that expires immediately to clear it
        System.out.println("Currently logging out user 1..");
        ResponseCookie clearCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)  // TESTING, production would be true
                .sameSite(SameSiteCookies.STRICT.toString())
                .path("/")
                .maxAge(0)  // This makes it expire immediately
                .build();
        System.out.println("Currently logging out user 2..");
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, clearCookie.toString())
                .body(Map.of("message", "Logged out successfully"));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDto verifyUserDto) {
        System.out.println("received verify");
        try {
            authenticationService.verifyUser(verifyUserDto);
            return ResponseEntity.ok("User verified successfully");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
        try {
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code resent");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/session")
    public ResponseEntity<?> session(@CookieValue(name = "jwt", required = false) String token) {
        System.out.println("Refreshing session.." + token);
        return authenticationService.refreshOrValidate(token);
    }

}
