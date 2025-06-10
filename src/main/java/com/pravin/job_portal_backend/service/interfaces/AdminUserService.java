package com.pravin.job_portal_backend.service.interfaces;

import com.pravin.job_portal_backend.dto.UserDto;
import java.util.List;

public interface AdminUserService {
    List<UserDto> getAll();
    List<UserDto> getAllUsersNoAuth();
    void deleteByUsername(String email);
}
