package com.tomtom.service.mentorapply.dto;

public record Mentee(
    long id,
    String name,
    String jobTitle,
    String location,
    Pairing pairing,
    PendingApplication[] pendingApplications
) {
}
