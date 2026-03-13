package com.tomtom.service.mentorapply.service.pairing;

import com.tomtom.service.mentorapply.mapper.Mapper;
import com.tomtom.service.mentorapply.repository.MenteeRepository;
import com.tomtom.service.mentorapply.repository.MentorRepository;
import com.tomtom.service.mentorapply.repository.PairingRepository;
import com.tomtom.service.mentorapply.repository.PendingApplicationRepository;
import com.tomtom.service.mentorapply.repository.entity.PairingEntity;
import com.tomtom.service.mentorapply.repository.entity.PendingApplicationEntity;
import com.tomtom.service.mentorapply.service.api.PairingService;
import com.tomtom.service.mentorapply.service.dto.Pairing;
import com.tomtom.service.mentorapply.service.dto.PendingApplication;
import com.tomtom.service.mentorapply.service.dto.PendingApplicationState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PairingServiceImpl implements PairingService {
    private final PairingRepository pairingRepository;
    private final PendingApplicationRepository pendingApplicationRepository;
    private final MentorRepository mentorRepository;
    private final MenteeRepository menteeRepository;

    @Autowired
    public PairingServiceImpl(
        PairingRepository pairingRepository,
        PendingApplicationRepository pendingApplicationRepository,
        MentorRepository mentorRepository,
        MenteeRepository menteeRepository) {
        this.pairingRepository = pairingRepository;
        this.pendingApplicationRepository = pendingApplicationRepository;
        this.mentorRepository = mentorRepository;
        this.menteeRepository = menteeRepository;
    }

    @Override
    public ResponseEntity<List<Pairing>> getAllPairings() {
        var pairings = pairingRepository
            .findAll()
            .stream()
            .map(Mapper::fromEntity)
            .toList();
        return ResponseEntity.ok(pairings);
    }

    @Override
    public ResponseEntity<Pairing> getPairingByMentorId(long mentorId) {
        var pairing = pairingRepository
            .findByMentorId(mentorId);
        if (pairing == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(Mapper.fromEntity(pairing));
    }

    @Override
    public ResponseEntity<Pairing> getPairingByMenteeId(long menteeId) {
        var pairing = pairingRepository
            .findByMenteeId(menteeId);
        if (pairing == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(Mapper.fromEntity(pairing));
    }

    @Override
    public ResponseEntity<List<PendingApplication>> getApplicationByMentorId(long mentorId) {
        var applications = pendingApplicationRepository
            .findByMentorId(mentorId)
            .stream()
            .map(Mapper::fromEntity)
            .toList();
        return ResponseEntity.ok(applications);
    }

    @Override
    public ResponseEntity<List<PendingApplication>> getApplicationByMenteeId(long menteeId) {
        var applications = pendingApplicationRepository
            .findByMenteeId(menteeId)
            .stream()
            .map(Mapper::fromEntity)
            .toList();
        return ResponseEntity.ok(applications);
    }

    @Override
    @Transactional
    public ResponseEntity<PendingApplication> submitApplication(long mentorId, long menteeId) {
        // Make sure the mentor is available.
        if (!mentorRepository.existsAndAvailable(mentorId)) {
            return ResponseEntity.badRequest().build();
        }
        // Make sure the mentor and mentee are not paired yet.
        var isMentorPaired = pairingRepository.findByMentorId(mentorId) != null;
        if (isMentorPaired) {
            return ResponseEntity.badRequest().build();
        }
        var isMenteePaired = pairingRepository.findByMenteeId(menteeId) != null;
        if (isMenteePaired) {
            return ResponseEntity.badRequest().build();
        }

        // Avoid submitting duplicate applications.
        var savedPendingApplication = pendingApplicationRepository
            .findByMentorAndMenteeIdWithState(mentorId, menteeId, PendingApplicationState.WAITING_APPROVAL);
        if (!savedPendingApplication.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Mapper.fromEntity(savedPendingApplication.getFirst()));
        }

        // Create a new application.
        var mentor = mentorRepository.findById(mentorId).orElse(null);
        if (mentor == null) {
            return ResponseEntity.notFound().build();
        }
        var mentee = menteeRepository.findById(menteeId).orElse(null);
        if (mentee == null) {
            return ResponseEntity.notFound().build();
        }
        var entity = new PendingApplicationEntity();
        entity.mentor = mentor;
        entity.mentee = mentee;
        entity.applyDate = LocalDate.now();
        entity.state = PendingApplicationState.WAITING_APPROVAL;
        var application = Mapper.fromEntity(pendingApplicationRepository.save(entity));
        return ResponseEntity.ok(application);
    }

    @Override
    public ResponseEntity<Void> declineApplication(long applicationId, long mentorId) {
        var application = pendingApplicationRepository.findById(applicationId).orElse(null);
        if (application == null ||
            application.state == PendingApplicationState.APPROVED ||
            application.mentor.id != mentorId) {
            return ResponseEntity.badRequest().build();
        }
        var modificationResult = pendingApplicationRepository
            .updateApplicationState(applicationId, PendingApplicationState.DECLINED);
        if (modificationResult == 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @Override
    @Transactional
    public ResponseEntity<Pairing> approveApplication(long applicationId, long mentorId, long menteeId) {
        // Mentor and mentee must not already be paired
        if (pairingRepository.findByMentorId(mentorId) != null || pairingRepository.findByMenteeId(menteeId) != null) {
            return ResponseEntity.badRequest().build();
        }
        // Approve this application
        int approveApplicationResult = pendingApplicationRepository
            .approveApplicationWithMentorMenteeId(mentorId, menteeId, applicationId);
        if (approveApplicationResult == 0) {
            return ResponseEntity.notFound().build();
        }
        // Decline all other WAITING applications for this mentor
        pendingApplicationRepository.declineMentorOtherApplications(mentorId, applicationId);
        // Decline all other WAITING applications for this mentee
        pendingApplicationRepository.declineMenteeOtherApplications(menteeId, applicationId);
        // Reload approved application after state update
        var approvedApplication = pendingApplicationRepository.findById(applicationId).orElse(null);
        if (approvedApplication == null) {
            return ResponseEntity.internalServerError().build();
        }
        // Save a new pairing.
        var pairingEntity = new PairingEntity();
        pairingEntity.mentor = approvedApplication.mentor;
        pairingEntity.mentee = approvedApplication.mentee;
        pairingEntity.applyDate = approvedApplication.applyDate;
        pairingEntity.startDate = LocalDate.now();
        pairingEntity.endDate = LocalDate.now().plusMonths(6);
        pairingEntity = pairingRepository.save(pairingEntity);
        // Update mentor and mentee's pairing state, no need to flush.
        approvedApplication.mentor.pairing = pairingEntity;
        approvedApplication.mentee.pairing = pairingEntity;
        return ResponseEntity.ok(Mapper.fromEntity(pairingEntity));
    }

    @Override
    public ResponseEntity<Void> cancelApplication(long applicationId, long menteeId) {
        var application = pendingApplicationRepository.findById(applicationId).orElse(null);
        if (application == null ||
            application.mentee.id != menteeId ||
            application.state != PendingApplicationState.WAITING_APPROVAL) {
            return ResponseEntity.badRequest().build();
        }
        pendingApplicationRepository.deleteById(applicationId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> cancelPairing(long paringId) {
        pairingRepository.deleteById(paringId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteOutdatedPairingAndApplications() {
        pairingRepository.deleteOutdatedPairing(LocalDate.now());
        pendingApplicationRepository.deleteOutdatedApplication(LocalDate.now().minusMonths(6));
        return null;
    }
}
