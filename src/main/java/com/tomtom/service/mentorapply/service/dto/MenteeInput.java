package com.tomtom.service.mentorapply.service.dto;

import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * Mentee data parsed from frontend JSON, all fields are nullable.
 */
public record MenteeInput(
    @Nullable Long id,
    @Nullable String name,
    @Nullable String jobTitle,
    @Nullable String location,
    @Nullable Long pairingId,
    @Nullable List<Long> pendingApplicationIds
) {
}
