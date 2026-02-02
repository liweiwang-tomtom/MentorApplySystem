package com.tomtom.service.mentorapply.service.dto;

import java.time.LocalDate;
import java.util.List;

public record Pairing(
    Long id,
    Long mentorId,
    Long menteeId,
    LocalDate applyDate,
    LocalDate startDate,
    LocalDate endDate,
    List<String> skillsToEnhance
) {
}
