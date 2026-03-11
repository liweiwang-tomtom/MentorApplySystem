package com.tomtom.service.mentorapply.controller;


import com.tomtom.service.mentorapply.service.api.PairingService;
import com.tomtom.service.mentorapply.service.dto.Pairing;
import com.tomtom.service.mentorapply.service.dto.PendingApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("api/v1/pairings")
public class PairingController {
    private final PairingService pairingService;

    public PairingController(PairingService pairingService) {
        this.pairingService = pairingService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Pairing>> getAllPairings() {
        return pairingService.getAllPairings();
    }

    @GetMapping("/mentor/{mentorId}")
    public ResponseEntity<Pairing> getPairingByMentorId(@PathVariable long mentorId) {
        if (mentorId < 1) {
            return ResponseEntity.badRequest().build();
        }
        return pairingService.getPairingByMentorId(mentorId);
    }

    @GetMapping("/mentee/{menteeId}")
    public ResponseEntity<Pairing> getPairingByMenteeId(@PathVariable("menteeId") long menteeId) {
        if (menteeId < 1) {
            return ResponseEntity.badRequest().build();
        }
        return pairingService.getPairingByMenteeId(menteeId);
    }

    @DeleteMapping("/{pairingId}")
    public ResponseEntity<Void> cancelPairing(@PathVariable("pairingId") long pairingId) {
        if (pairingId < 1) {
            return ResponseEntity.badRequest().build();
        }
        return pairingService.cancelPairing(pairingId);
    }

    @GetMapping("/applications/mentor/{mentorId}")
    public ResponseEntity<List<PendingApplication>> getApplicationsByMentorId(@PathVariable long mentorId) {
        if (mentorId < 1) {
            return ResponseEntity.badRequest().build();
        }
        return pairingService.getApplicationByMentorId(mentorId);
    }

    @GetMapping("/applications/mentee/{menteeId}")
    public ResponseEntity<List<PendingApplication>> getApplicationsByMenteeId(@PathVariable long menteeId) {
        if (menteeId < 1) {
            return ResponseEntity.badRequest().build();
        }
        return pairingService.getApplicationByMenteeId(menteeId);
    }

    @PostMapping("/applications")
    public ResponseEntity<PendingApplication> submitApplication(
        @RequestParam long mentorId,
        @RequestParam long menteeId) {
        if (menteeId < 1 || mentorId < 1) {
            return ResponseEntity.badRequest().build();
        }
        return pairingService.submitApplication(mentorId, menteeId);
    }

    @PostMapping("/applications/{applicationId}:decline")
    public ResponseEntity<Void> declineApplication(
        @PathVariable long applicationId,
        @RequestParam long mentorId) {
        if (applicationId < 1 || mentorId < 1) {
            return ResponseEntity.badRequest().build();
        }
        return pairingService.declineApplication(applicationId, mentorId);
    }

    @PostMapping("/applications/{applicationId}:approve")
    public ResponseEntity<Pairing> approveApplication(
        @PathVariable long applicationId,
        @RequestParam long mentorId,
        @RequestParam long menteeId) {
        if (applicationId < 1 || mentorId < 1 || menteeId < 1) {
            return ResponseEntity.badRequest().build();
        }
        return pairingService.approveApplication(applicationId, mentorId, menteeId);
    }

    @DeleteMapping("/applications/{applicationId}")
    public ResponseEntity<Void> cancelApplication(
        @PathVariable long applicationId,
        @RequestParam long menteeId) {
        if (applicationId < 1 || menteeId < 1) {
            return ResponseEntity.badRequest().build();
        }
        return pairingService.cancelApplication(applicationId, menteeId);
    }

    @DeleteMapping("/maintenance/outdated")
    public ResponseEntity<Void> deleteOutdatedPairingAndApplications() {
        return pairingService.deleteOutdatedPairingAndApplications();
    }
}

