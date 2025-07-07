package com.auth_example.baseballguru.responses;

import com.auth_example.baseballguru.model.Stats;

import static com.auth_example.baseballguru.utility.SlugUtil.slugify;

public record SearchPlayerResponse(String player, String endpoint) {

    public SearchPlayerResponse(Stats stats) {
        this(
                stats.getPlayer(),
                slugify(stats.getPlayer(), stats.getId())
        );
    }

}