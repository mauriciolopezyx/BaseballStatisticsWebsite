package com.auth_example.demo.controller;

import com.auth_example.demo.dto.AddPlayerDto;
import com.auth_example.demo.dto.ResetPasswordDto;
import com.auth_example.demo.model.User;
import com.auth_example.demo.service.StatsService;
import com.auth_example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/stats")
@RestController
public class StatsController {
    private final StatsService statsService;
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @PostMapping("/player/add")
    public ResponseEntity<?> addPlayer(@Valid @RequestBody AddPlayerDto addPlayerDto) {
        System.out.println("Received add player request!");
        try {
            statsService.addPlayer(addPlayerDto);
            return ResponseEntity.ok("Player added to database successfully!");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
