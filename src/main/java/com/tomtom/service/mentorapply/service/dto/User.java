package com.tomtom.service.mentorapply.service.dto;

import org.jspecify.annotations.Nullable;

public record User(
    @Nullable Long id,
    @Nullable String name,
    @Nullable String email,
    @Nullable String password,
    @Nullable Role role
) {
}
