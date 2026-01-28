package com.tomtom.service.mentorapply.repository.entity;

import com.tomtom.service.mentorapply.service.dto.PairingState;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Entity
@Table(
    name = "TBL_PAIRINGS",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "mentor_id"),
        @UniqueConstraint(columnNames = "mentee_id")
    }
)
public class PairingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public long id;

    @Column(name = "mentor_id", nullable = false, insertable = false, updatable = false)
    public long mentorId;

    @Column(name = "mentee_id", nullable = false, insertable = false, updatable = false)
    public long menteeId;

    @Column(name = "apply_date")
    public LocalDate applyDate;

    @Column(name = "start_date")
    public LocalDate startDate;

    @Column(name = "end_date")
    public LocalDate endDate;

    @ElementCollection
    @CollectionTable(name = "PAIRING_SKILLS", joinColumns = @JoinColumn(name = "pairing_id"))
    @Column(name = "skill_to_enhance")
    public List<String> skillsToEnhance;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    public PairingState state;

    @OneToOne
    @JoinColumn(name = "mentor_id", unique = true, nullable = false)
    public MentorEntity mentor;

    @OneToOne
    @JoinColumn(name = "mentee_id", unique = true, nullable = false)
    public MenteeEntity mentee;

    public PairingEntity() {
        this.id = 0L;
        this.mentorId = 0L;
        this.menteeId = 0L;
        this.applyDate = LocalDate.now();
        this.startDate = LocalDate.now();
        this.endDate = LocalDate.now();
        this.skillsToEnhance = Collections.emptyList();
        this.state = PairingState.NOT_STARTED;
    }

    public PairingEntity(
        long id,
        long mentorId,
        long menteeId,
        LocalDate applyDate,
        LocalDate startDate,
        LocalDate endDate,
        List<String> skillsToEnhance,
        PairingState state,
        MentorEntity mentor,
        MenteeEntity mentee) {
        this.id = id;
        this.mentorId = mentorId;
        this.menteeId = menteeId;
        this.applyDate = applyDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.skillsToEnhance = skillsToEnhance;
        this.state = state;
        this.mentor = mentor;
        this.mentee = mentee;
    }
}
