package com.tomtom.service.mentorapply.service.dto;

import org.jspecify.annotations.Nullable;

public record User(
    long id,
    @Nullable String name,
    String email,
    String password,
    Role role
) {
}
