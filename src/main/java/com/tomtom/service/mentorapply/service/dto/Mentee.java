package com.tomtom.service.mentorapply.service.dto;

import org.jspecify.annotations.Nullable;

import java.util.List;

public record Mentee(
    long id,
    String name,
    @Nullable String jobTitle,
    @Nullable String location,
    @Nullable Pairing pairing,
    List<PendingApplication> pendingApplications
) {
}
