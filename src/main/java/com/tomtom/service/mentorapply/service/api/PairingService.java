package com.tomtom.service.mentorapply.service.api;

import com.tomtom.service.mentorapply.service.dto.Pairing;
import com.tomtom.service.mentorapply.service.dto.PendingApplication;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PairingService {
    ResponseEntity<List<Pairing>> getAllPairings();

    ResponseEntity<Pairing> getPairingByMentorId(long mentorId);

    ResponseEntity<Pairing> getParingByMenteeId(long menteeId);

    ResponseEntity<List<PendingApplication>> getApplicationByMentorId(long mentorId);

    ResponseEntity<List<PendingApplication>> getApplicationByMenteeId(long menteeId);

    /**
     * Mentee submits an application to mentor.
     */
    ResponseEntity<PendingApplication> submitApplication(long mentorId, long menteeId);

    /**
     * Mentor declines an application from mentee.
     */
    ResponseEntity<PendingApplication> declineApplication(long applicationId);

    /**
     * Mentor approves one pending application and declines others.
     * If both mentor and mentee are available, create a pairing and return a pairing and decline other pending applications.
     * Otherwise, return bad-request.
     */
    ResponseEntity<Pairing> approveApplication(long applicationId);

    /**
     * Mentee cancels (deletes) a submitted application.
     */
    ResponseEntity<Void> cancelApplication(long applicationId);

    /**
     * System administrator cancels (deletes) a pairing.
     */
    ResponseEntity<Pairing> cancelPairing(long paringId);

    /**
     * System administrator deletes outdated pairings.
     */
    ResponseEntity<Void> deleteOutdatedPairing();
}
