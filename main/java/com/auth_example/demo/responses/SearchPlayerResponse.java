package com.auth_example.demo.responses;

import com.auth_example.demo.model.Stats;

import java.text.Normalizer;

import static com.auth_example.demo.utility.SlugUtil.slugify;

public record SearchPlayerResponse(String player, String endpoint) {

    public SearchPlayerResponse(Stats stats) {
        this(
                stats.getPlayer(),
                slugify(stats.getPlayer(), stats.getId())
        );
    }

}