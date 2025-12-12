package com.tomtom.service.mentorapply.dto;

import java.time.LocalDate;
import java.util.List;

public record PendingApplication(
    long id,
    long mentorId,
    long menteeId,
    LocalDate applyDate,
    List<String> skillsToEnhance,
    PendingApplicationState state
) {
}
