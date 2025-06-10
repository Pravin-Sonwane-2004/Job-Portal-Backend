package com.pravin.job_portal_backend.service.interfaces;

import com.pravin.job_portal_backend.dto.UserDto;
import com.pravin.job_portal_backend.dto.UpdateUserProfile;
import java.util.Optional;

public interface UserProfileService {
    UserDto getUserProfile(Long userId);
    void updateUserProfile(Long userId, UpdateUserProfile request);
    String getUserNameWithId(Long userId);
    Optional<UserDto> findByUserName(String email);
}
