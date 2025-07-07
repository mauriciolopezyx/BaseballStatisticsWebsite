package com.auth_example.baseballguru.service;

import com.auth_example.baseballguru.dto.ResetPasswordDto;
import com.auth_example.baseballguru.dto.ToggleBookmarkDto;
import com.auth_example.baseballguru.model.User;
import com.auth_example.baseballguru.model.UserMetadata;
import com.auth_example.baseballguru.repository.UserMetadataRepository;
import com.auth_example.baseballguru.repository.UserRepository;
import com.auth_example.baseballguru.responses.UserMetadataResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMetadataRepository userMetadataRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder, UserMetadataRepository userMetadataRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMetadataRepository = userMetadataRepository;
    }

    public void resetPassword(User currentUser, ResetPasswordDto input) {
        System.out.println("Received reset password request:");
        System.out.println(input.oldPassword());
        System.out.println(input.newPassword());

        if (currentUser.hasPassword() && input.oldPassword() == null) {
            throw new RuntimeException("Expected old password in addition to new password");
        }
        if (currentUser.hasPassword() && !passwordEncoder.matches(input.oldPassword(), currentUser.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        currentUser.setPassword(passwordEncoder.encode(input.newPassword()));
        userRepository.save(currentUser);
    }

    public ResponseEntity<?> requestMetadata(User currentUser) {
        List<UserMetadata> recentlyViewed = userMetadataRepository.findTop10ByUserIdAndActivityOrderByUpdatedAtDesc(currentUser.getId(), "history");
        List<UserMetadata> bookmarked = userMetadataRepository.findTop10ByUserIdAndActivityOrderByUpdatedAtDesc(currentUser.getId(), "bookmark");

        System.out.println(currentUser.getUsername() + " is Requesting their metadata (for home page)");

        List<UserMetadataResponse> response = recentlyViewed.stream()
                .map(userMetadata -> new UserMetadataResponse(
                        userMetadata.getPlayer(),
                        userMetadata.getHeadshot(),
                        userMetadata.getEndpoint()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of("recentlyViewed", recentlyViewed, "bookmarked", bookmarked));
    }

    public boolean togglePlayerBookmark(User currentUser, ToggleBookmarkDto toggleBookmarkDto) {
        Optional<UserMetadata> bookmarkedEntry = userMetadataRepository.findByUserIdAndPlayerIdAndActivity(currentUser.getId(), toggleBookmarkDto.playerId(), "bookmark");
        if (bookmarkedEntry.isPresent()) {
            userMetadataRepository.deleteById(bookmarkedEntry.get().getId());
            return false;
        } else {
            UserMetadata newBookmarkedEntry = new UserMetadata(currentUser.getId(), toggleBookmarkDto.playerId(), "bookmark", toggleBookmarkDto.player(), toggleBookmarkDto.headshot(), toggleBookmarkDto.endpoint());
            userMetadataRepository.save(newBookmarkedEntry);
            return true;
        }
    }

}
