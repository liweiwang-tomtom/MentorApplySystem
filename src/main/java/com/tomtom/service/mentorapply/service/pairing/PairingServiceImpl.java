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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

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
    public ResponseEntity<Pairing> getParingByMenteeId(long menteeId) {
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
        // Make sure the mentor and mentee are not already paired.
        var isMentorAvailable = pairingRepository.findByMentorId(mentorId) == null;
        if (!isMentorAvailable) {
            return ResponseEntity.badRequest().build();
        }
        var isMenteeAvailable = pairingRepository.findByMenteeId(menteeId) == null;
        if (!isMenteeAvailable) {
            return ResponseEntity.badRequest().build();
        }

        // Make sure submitted applications don't repeat.
        var savedApplication = pendingApplicationRepository
            .findByMenteeAndMentorIdWithoutState(mentorId, menteeId, PendingApplicationState.DECLINED);
        if (!savedApplication.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Mapper.fromEntity(savedApplication.getFirst()));
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
        if (application == null || application.state == PendingApplicationState.APPROVED) {
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
    public ResponseEntity<Pairing> approveApplication(long applicationId, long mentorId) {
        var isMentorAvailable = pairingRepository.findByMentorId(mentorId) == null;
        if (!isMentorAvailable) {
            return ResponseEntity.badRequest().build();
        }
        var modificationResult = pendingApplicationRepository
            .updateApplicationState(applicationId, PendingApplicationState.APPROVED);
        if (modificationResult == 0) {
            return ResponseEntity.notFound().build();
        }
        var approvedApplication = pendingApplicationRepository.findById(applicationId).orElse(null);
        if (approvedApplication == null) {
            return ResponseEntity.internalServerError().build();
        }

        // Create a new pairing.
        var pairing = new PairingEntity();
        pairing.mentor = approvedApplication.mentor;
        pairing.mentee = approvedApplication.mentee;
        pairing.applyDate = approvedApplication.applyDate;
        pairing.startDate = LocalDate.now();
        pairing.endDate = LocalDate.now().plusMonths(6);

        return ResponseEntity.ok(Mapper.fromEntity(pairingRepository.save(pairing)));
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
    public ResponseEntity<Pairing> cancelPairing(long paringId) {
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
