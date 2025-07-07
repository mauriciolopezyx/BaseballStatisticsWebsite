package com.auth_example.baseballguru.repository;

import com.auth_example.baseballguru.model.Stats;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatsRepository extends CrudRepository<Stats, Long> {
    Optional<Stats> findByPlayer(String player);
    List<Stats> findByPlayerContainingIgnoreCase(String player);
    List<Stats> findByPlayerContainingIgnoreCaseAndTeamContainingIgnoreCase(String player, String team);
    List<Stats> findByTeamContainingIgnoreCase(String team);
}
