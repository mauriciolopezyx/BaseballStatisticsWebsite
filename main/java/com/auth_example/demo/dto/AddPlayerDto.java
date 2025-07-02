package com.auth_example.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddPlayerDto {
    @NotNull(message = "cannot be null")
    private String Player;
    @NotNull(message = "cannot be null")
    private String Pos;
    @NotNull(message = "cannot be null")
    private int Age;
    @NotNull(message = "cannot be null")
    private String Team;
    @NotNull(message = "cannot be null")
    private String Lg;
    @NotNull(message = "cannot be null")
    private float WAR;
    @NotNull(message = "cannot be null")
    private int G;
    @NotNull(message = "cannot be null")
    private int PA;
    @NotNull(message = "cannot be null")
    private int AB;
    @NotNull(message = "cannot be null")
    private int R;
    @NotNull(message = "cannot be null")
    private int H;
    @NotNull(message = "cannot be null")
    private int DBL;
    @NotNull(message = "cannot be null")
    private int TPL;
    @NotNull(message = "cannot be null")
    private int HR;
    @NotNull(message = "cannot be null")
    private int RBI;
    @NotNull(message = "cannot be null")
    private int SB;
    @NotNull(message = "cannot be null")
    private int CS;
    @NotNull(message = "cannot be null")
    private int BB;
    @NotNull(message = "cannot be null")
    private int SO;
    @NotNull(message = "cannot be null")
    private float BA;
    @NotNull(message = "cannot be null")
    private float OBP;
    @NotNull(message = "cannot be null")
    private float SLG;
    @NotNull(message = "cannot be null")
    private float OPS;
    @NotNull(message = "cannot be null")
    private int TB;
    @NotNull(message = "cannot be null")
    private int GIDP;
    @NotNull(message = "cannot be null")
    private int HBP;
    @NotNull(message = "cannot be null")
    private int SH;
    @NotNull(message = "cannot be null")
    private int SF;
    @NotNull(message = "cannot be null")
    private int IBB;
    @NotNull(message = "cannot be null")
    private String Headshot;

}
