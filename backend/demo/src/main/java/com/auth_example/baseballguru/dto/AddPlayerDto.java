package com.auth_example.baseballguru.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record AddPlayerDto(
        @NotBlank
         String player,
        @NotBlank
         String pos,
        @PositiveOrZero
         int age,
        @NotBlank
         String team,
        @NotBlank
         String lg,
        @PositiveOrZero
         float war,
        @PositiveOrZero
         int g,
        @PositiveOrZero
         int pa,
        @PositiveOrZero
         int ab,
        @PositiveOrZero
         int r,
        @PositiveOrZero
         int h,
        @PositiveOrZero
         int dbl,
        @PositiveOrZero
         int tpl,
        @PositiveOrZero
         int hr,
        @PositiveOrZero
         int rbi,
        @PositiveOrZero
         int sb,
        @PositiveOrZero
         int cs,
        @PositiveOrZero
         int bb,
        @PositiveOrZero
         int so,
        @PositiveOrZero
         float ba,
        @PositiveOrZero
         float obp,
        @PositiveOrZero
         float slg,
        @PositiveOrZero
         float ops,
        @PositiveOrZero
         int tb,
        @PositiveOrZero
         int gidp,
        @PositiveOrZero
         int hbp,
        @PositiveOrZero
         int sh,
        @PositiveOrZero
         int sf,
        @PositiveOrZero
         int ibb,
        @NotBlank
         String headshot
)
{}