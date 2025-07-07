package com.auth_example.baseballguru.controller;

import com.auth_example.baseballguru.dto.ResetPasswordDto;
import com.auth_example.baseballguru.dto.ToggleBookmarkDto;
import com.auth_example.baseballguru.model.User;
import com.auth_example.baseballguru.responses.UserProfileResponse;
import com.auth_example.baseballguru.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(new UserProfileResponse(currentUser));
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

        try {
            userService.resetPassword(currentUser, resetPasswordDto);
            return ResponseEntity.ok("Password updated successfully");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/me/request/metadata")
    public ResponseEntity<?> requestMetadata() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        try {
            return userService.requestMetadata(currentUser);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/me/bookmarks/toggle")
    public ResponseEntity<?> togglePlayerBookmark(@RequestBody ToggleBookmarkDto toggleBookmarkDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(userService.togglePlayerBookmark(currentUser, toggleBookmarkDto));
    }

}
