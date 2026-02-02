package com.tomtom.service.mentorapply.service.pairing;

import com.tomtom.service.mentorapply.service.api.PairingService;
import com.tomtom.service.mentorapply.service.dto.Pairing;
import com.tomtom.service.mentorapply.service.dto.PendingApplication;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class PairingServiceImpl implements PairingService {

    @Override
    public ResponseEntity<List<Pairing>> getAllPairings() {
        return null;
    }

    @Override
    public ResponseEntity<Pairing> getPairingByMentorId(long mentorId) {
        return null;
    }

    @Override
    public ResponseEntity<Pairing> getParingByMenteeId(long menteeId) {
        return null;
    }

    @Override
    public ResponseEntity<List<PendingApplication>> getApplicationByMentorId(long mentorId) {
        return null;
    }

    @Override
    public ResponseEntity<List<PendingApplication>> getApplicationByMenteeId(long menteeId) {
        return null;
    }

    @Override
    public ResponseEntity<PendingApplication> submitApplication(long mentorId, long menteeId) {
        return null;
    }

    @Override
    public ResponseEntity<PendingApplication> declineApplication(long applicationId) {
        return null;
    }

    @Override
    public ResponseEntity<Pairing> approveApplication(long applicationId) {
        return null;
    }

    @Override
    public ResponseEntity<Void> cancelApplication(long applicationId) {
        return null;
    }

    @Override
    public ResponseEntity<Pairing> cancelPairing(long paringId) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteOutdatedPairing() {
        return null;
    }
}
