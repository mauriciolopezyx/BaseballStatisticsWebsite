package com.auth_example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class ResetPasswordDto {
    private String oldPassword;
    private String newPassword;
}
