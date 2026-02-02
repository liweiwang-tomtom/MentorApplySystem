package com.tomtom.service.mentorapply.service.dto;

import org.jspecify.annotations.Nullable;

import java.util.List;

public record Mentee(
    Long id,
    String name,
    @Nullable String jobTitle,
    @Nullable String location,
    @Nullable Long pairingId,
    List<Long> pendingApplicationIds
) {
}
