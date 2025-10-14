package com.tomtom.service.mentorapply.dto;

public record Mentor(
    long id,
    String name,
    String jobTitle,
    String location,
    String[] skills,
    boolean available,
    Pairing pairing,
    PendingApplication[] pendingApplications
) {
}
