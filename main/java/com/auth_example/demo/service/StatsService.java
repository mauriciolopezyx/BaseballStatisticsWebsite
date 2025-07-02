package com.auth_example.demo.service;

import com.auth_example.demo.dto.AddPlayerDto;
import com.auth_example.demo.model.Stats;
import com.auth_example.demo.repository.StatsRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StatsService {
    private final StatsRepository statsRepository;

    public StatsService(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    public void addPlayer(AddPlayerDto addPlayerDto) {
        Optional<Stats> userFromEmail = statsRepository.findByPlayer(addPlayerDto.getPlayer());
        if (userFromEmail.isPresent()) {
            throw new RuntimeException(addPlayerDto.getPlayer() + " already exists in database");
        }
        System.out.println("Adding player " + addPlayerDto.getPlayer());
        Stats newStatsEntry = new Stats(addPlayerDto);
        statsRepository.save(newStatsEntry);
    }

}
