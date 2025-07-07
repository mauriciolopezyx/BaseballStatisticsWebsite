package com.auth_example.baseballguru.repository;

import com.auth_example.baseballguru.model.UserMetadata;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserMetadataRepository extends CrudRepository<UserMetadata, Long> {
    List<UserMetadata> findTop10ByUserIdAndActivityOrderByUpdatedAtDesc(Long userId, String activity);
    Optional<UserMetadata> findByUserIdAndPlayerIdAndActivity(Long userId, Long playerId, String activity);
    boolean existsByUserIdAndPlayerIdAndActivity(Long userId, Long playerId, String activity);
}
