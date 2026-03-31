package com.tomtom.service.mentorapply.service.dto;

import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * Mentor data parsed from frontend JSON, all fields are nullable.
 */
public record MentorInput(
    @Nullable Long id,
    @Nullable String name,
    @Nullable String jobTitle,
    @Nullable String location,
    @Nullable List<String> skills,
    boolean available,
    @Nullable Long pairingId,
    @Nullable List<Long> pendingApplicationIds
) {
}
