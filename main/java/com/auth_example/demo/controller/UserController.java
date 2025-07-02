package com.auth_example.demo.controller;

import com.auth_example.demo.dto.ResetPasswordDto;
import com.auth_example.demo.model.User;
import com.auth_example.demo.service.UserService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/me/haspassword")
    public ResponseEntity<?> hasPassword() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(Map.of("hasPassword", currentUser.hasPassword()));
    }

    @PostMapping("/me/resetpassword")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("User not authenticated");
        }

        try {
            userService.resetPassword(currentUser, resetPasswordDto);
            return ResponseEntity.ok("Password updated successfully");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Then we need the ability to upload a new player to the database
    // Settings page needs to retrieve the actual users name and not hardcode it
    // Then obviously the player pages, actually retreiving them, able to bookmark separately, will probably need new fields in Users
    // ability to bookmark and save recently viewed players, and actual working search page


//    @GetMapping("/")
//    public ResponseEntity<List<User>> allUsers() {
//        List<User> users = userService.allUsers();
//        return ResponseEntity.ok(users);
//    }

}
