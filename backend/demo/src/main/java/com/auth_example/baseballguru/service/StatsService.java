package com.auth_example.baseballguru.service;

import com.auth_example.baseballguru.dto.AddPlayerDto;
import com.auth_example.baseballguru.model.Role;
import com.auth_example.baseballguru.model.Stats;
import com.auth_example.baseballguru.model.User;
import com.auth_example.baseballguru.model.UserMetadata;
import com.auth_example.baseballguru.repository.StatsRepository;
import com.auth_example.baseballguru.repository.UserMetadataRepository;
import com.auth_example.baseballguru.responses.SearchPlayerResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.auth_example.baseballguru.utility.SlugUtil.standardize;

@Service
public class StatsService {
    private final StatsRepository statsRepository;
    private final UserMetadataRepository userMetadataRepository;

    public StatsService(StatsRepository statsRepository, UserMetadataRepository userMetadataRepository) {
        this.statsRepository = statsRepository;
        this.userMetadataRepository = userMetadataRepository;
    }

    public void addPlayer(User currentUser, AddPlayerDto addPlayerDto) {
        Optional<Stats> userFromEmail = statsRepository.findByPlayer(addPlayerDto.player());
        if (userFromEmail.isPresent()) {
            throw new RuntimeException(addPlayerDto.player() + " already exists in database");
        }
        if (currentUser.getRole() == Role.USER) {
            throw new RuntimeException("You do not have permission to add a player!");
        }
        System.out.println("Adding player " + addPlayerDto.player());
        Stats newStatsEntry = new Stats(addPlayerDto);
        statsRepository.save(newStatsEntry);
    }

    public ResponseEntity<?> requestExactPlayer(User currentUser, String player, Long playerId) {
        Stats playerFromId = statsRepository.findById(playerId).orElseThrow(() -> new RuntimeException("URL Not Found (404-1)"));;
        if (playerFromId.getId() != playerId) {
            throw new RuntimeException("URL Not Found (404-2)");
        }
        String databaseName = Normalizer.normalize(standardize(playerFromId.getPlayer()), Normalizer.Form.NFD);
        if (!databaseName.equalsIgnoreCase(player)) {
            throw new RuntimeException("URL Not Found (404-2)");
        }
        String endpoint = databaseName.toLowerCase().replaceAll(" ", "-") + "-" + playerId;

        System.out.println("Requested endpoint: " +  endpoint);

        // Adding or updating recently viewed player entry accordingly
        UserMetadata historyEntry = userMetadataRepository.findByUserIdAndPlayerIdAndActivity(currentUser.getId(), playerFromId.getId(), "history").orElse(new UserMetadata());
        historyEntry.setUserId(currentUser.getId());
        historyEntry.setPlayerId(playerFromId.getId());
        historyEntry.setActivity("history");
        historyEntry.setPlayer(playerFromId.getPlayer());
        historyEntry.setHeadshot(playerFromId.getHeadshot());
        historyEntry.setEndpoint(endpoint);
        historyEntry.setUpdatedAt(Instant.now());
        userMetadataRepository.save(historyEntry);

        boolean isBookmarked = userMetadataRepository.existsByUserIdAndPlayerIdAndActivity(currentUser.getId(), playerFromId.getId(), "bookmark");
        playerFromId.setBookmarked(isBookmarked);

        return ResponseEntity.ok(playerFromId);
    }

    public ResponseEntity<?> searchForPlayers(String player, String team) {
        List<Stats> playersFound;

        if (team.equals("any")) {
            playersFound = statsRepository.findByPlayerContainingIgnoreCase(player);
        } else {
            playersFound = statsRepository.findByPlayerContainingIgnoreCaseAndTeamContainingIgnoreCase(player, team);
        }

        List<SearchPlayerResponse> response = playersFound.stream()
                .map(SearchPlayerResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> searchForTeamPlayers(String team) {
        List<Stats> playersFound = statsRepository.findByTeamContainingIgnoreCase(team);

        List<SearchPlayerResponse> response = playersFound.stream()
                .map(SearchPlayerResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

}
