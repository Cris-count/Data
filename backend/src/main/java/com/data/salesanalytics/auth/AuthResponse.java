package com.data.salesanalytics.auth;

import com.data.salesanalytics.user.Role;

public record AuthResponse(
        String token,
        Long userId,
        String name,
        String email,
        Role role
) {}
