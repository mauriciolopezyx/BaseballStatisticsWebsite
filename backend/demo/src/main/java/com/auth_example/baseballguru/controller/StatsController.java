package com.auth_example.baseballguru.controller;

import com.auth_example.baseballguru.dto.AddPlayerDto;
import com.auth_example.baseballguru.model.User;
import com.auth_example.baseballguru.service.StatsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        try {
            statsService.addPlayer(currentUser, addPlayerDto);
            return ResponseEntity.ok("Player added to database successfully!");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/player/search")
    public ResponseEntity<?> searchForPlayers(@RequestParam String player, @RequestParam String team) {
        System.out.println("Received player and team query is " + player + " and " + team);
        try {
            return statsService.searchForPlayers(player, team);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/player/request")
    public ResponseEntity<?> requestExactPlayer(@RequestParam String player, @RequestParam Long playerId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        try {
            return statsService.requestExactPlayer(currentUser, player, playerId);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/team/search")
    public ResponseEntity<?> searchForTeamPlayers(@RequestParam String team) {
        System.out.println("Received team [ONLY] query is " + team);
        try {
            return statsService.searchForTeamPlayers(team);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}
