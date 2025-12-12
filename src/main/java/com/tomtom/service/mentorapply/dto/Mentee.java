package com.tomtom.service.mentorapply.dto;

import java.util.List;

public record Mentee(
    long id,
    String name,
    String jobTitle,
    String location,
    Pairing pairing,
    List<PendingApplication> pendingApplications
) {
}
