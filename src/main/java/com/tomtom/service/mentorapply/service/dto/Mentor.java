package com.tomtom.service.mentorapply.service.dto;

import org.jspecify.annotations.Nullable;

import java.util.List;

public record Mentor(
    long id,
    String name,
    @Nullable String jobTitle,
    @Nullable String location,
    List<String> skills,
    boolean available,
    @Nullable Pairing pairing,
    List<PendingApplication> pendingApplications
) {
}
