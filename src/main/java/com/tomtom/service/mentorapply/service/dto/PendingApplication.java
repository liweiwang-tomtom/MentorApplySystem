package com.tomtom.service.mentorapply.service.dto;

import java.time.LocalDate;
import java.util.List;

public record PendingApplication(
    Long id,
    Long mentorId,
    Long menteeId,
    LocalDate applyDate,
    List<String> skillsToEnhance,
    PendingApplicationState state
) {
}
