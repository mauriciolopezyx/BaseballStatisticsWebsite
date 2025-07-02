package com.auth_example.demo.repository;

import com.auth_example.demo.model.Stats;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatsRepository extends CrudRepository<Stats, Long> {
    Optional<Stats> findByPlayer(String player);
}
