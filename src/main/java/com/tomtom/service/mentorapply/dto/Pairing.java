package com.tomtom.service.mentorapply.dto;

import java.time.LocalDate;
import java.util.List;

public record Pairing(
    long id,
    long mentorId,
    long menteeId,
    LocalDate applyDate,
    LocalDate startDate,
    LocalDate endDate,
    List<String> skillsToEnhance,
    PairingState state
) {
}
