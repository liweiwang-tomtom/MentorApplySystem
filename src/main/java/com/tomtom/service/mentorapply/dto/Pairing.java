package com.tomtom.service.mentorapply.dto;

public record Pairing(
    long id,
    long mentorId,
    long menteeId,
    long applyDate,
    long startDate,
    long endDate,
    String[] skillsToEnhance,
    State state
) {
    public enum State {
        NOT_STARTED,
        STARTED,
        FINISHED,
        CANCELED
    }
}
