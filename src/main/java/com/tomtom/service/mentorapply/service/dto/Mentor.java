package com.tomtom.service.mentorapply.service.dto;

import org.jspecify.annotations.Nullable;

import java.util.List;

public record Mentor(
    Long id,
    String name,
    @Nullable String jobTitle,
    @Nullable String location,
    List<String> skills,
    boolean available,
    @Nullable Long pairingId,
    List<Long> pendingApplicationIds
) {
    public Mentor {
        skills = skills == null ? List.of() : skills;
        pendingApplicationIds =
            pendingApplicationIds == null ? List.of() : pendingApplicationIds;
    }
}
