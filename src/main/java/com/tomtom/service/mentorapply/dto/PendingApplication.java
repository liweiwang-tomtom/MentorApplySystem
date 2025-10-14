package com.tomtom.service.mentorapply.dto;

public record PendingApplication(
    long id,
    long mentorId,
    long menteeId,
    long applyDate,
    String[] skillsToEnhance,
    State state
) {
    public enum State {
        WAITING_APPROVAL,
        APPROVED,
        CANCELED
    }
}
