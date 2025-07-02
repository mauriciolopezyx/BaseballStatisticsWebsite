package com.auth_example.demo.service;

import com.auth_example.demo.dto.ResetPasswordDto;
import com.auth_example.demo.model.User;
import com.auth_example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void resetPassword(User currentUser, ResetPasswordDto input) {
        System.out.println("Received reset password request:");
        System.out.println(input.getOldPassword());
        System.out.println(input.getNewPassword());

        if (currentUser.hasPassword() && input.getOldPassword() == null) {
            throw new RuntimeException("Expected old password in addition to new password");
        }
        if (currentUser.hasPassword() && !passwordEncoder.matches(input.getOldPassword(), currentUser.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        currentUser.setPassword(passwordEncoder.encode(input.getNewPassword()));
        userRepository.save(currentUser);
    }



}
