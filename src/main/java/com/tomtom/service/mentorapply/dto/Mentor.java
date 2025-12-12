package com.tomtom.service.mentorapply.dto;

import java.util.List;

public record Mentor(
    long id,
    String name,
    String jobTitle,
    String location,
    List<String> skills,
    boolean available,
    Pairing pairing,
    List<PendingApplication> pendingApplications
) {
}
