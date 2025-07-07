package com.auth_example.baseballguru.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name="users_metadata")
@Getter
@Setter
public class UserMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name="user_id", nullable=false)
    private long userId;
    @Column(name="player_id", nullable=false)
    private long playerId;
    @Column(nullable=false)
    private String activity;
    @Column(nullable=false)
    private String player;
    @Column(nullable=false)
    private String headshot;
    @Column(nullable=false)
    private String endpoint;
    @Column(name="updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    public UserMetadata(
            long userId,
            long playerId,
            String activity,
            String player,
            String headshot,
            String endpoint
    ) {
        this.userId = userId;
        this.playerId = playerId;
        this.activity = activity;
        this.player = player;
        this.headshot = headshot;
        this.endpoint = endpoint;
    }

    public UserMetadata() {}

}
