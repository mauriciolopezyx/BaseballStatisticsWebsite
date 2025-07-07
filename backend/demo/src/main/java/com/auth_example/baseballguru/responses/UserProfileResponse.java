package com.auth_example.baseballguru.responses;

import com.auth_example.baseballguru.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileResponse {
    private String username;
    private String email;

    public UserProfileResponse(User currentUser) {
        this.username = currentUser.getUsername();
        this.email = currentUser.getEmail();
    }
}
