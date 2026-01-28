package com.tomtom.service.mentorapply.repository.entity;

import com.tomtom.service.mentorapply.service.dto.PendingApplicationState;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "TBL_PENDING_APPLICATIONS")
public class PendingApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public long id;

    @Column(name = "mentor_id", nullable = false, insertable = false, updatable = false)
    public long mentorId;

    @Column(name = "mentee_id", nullable = false, insertable = false, updatable = false)
    public long menteeId;

    @Column(name = "apply_date", nullable = false)
    public LocalDate applyDate;

    @ElementCollection
    @CollectionTable(
        name = "PENDING_APPLICATIONS_SKILLS",
        joinColumns = @JoinColumn(name = "pending_id")
    )
    @Column(name = "skill_to_enhance")
    public List<String> skillsToEnhance;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    public PendingApplicationState state;

    @ManyToOne
    @JoinColumn(name = "mentor_id", nullable = false)
    public MentorEntity mentor;

    @ManyToOne
    @JoinColumn(name = "mentee_id", nullable = false)
    public MenteeEntity mentee;

    public PendingApplicationEntity() {
        this.id = 0L;
        this.mentorId = 0L;
        this.menteeId = 0L;
        this.applyDate = LocalDate.now();
        this.skillsToEnhance = Collections.emptyList();
        this.state = PendingApplicationState.NOT_STARTED;
    }

    public PendingApplicationEntity(
        long id,
        long mentorId,
        long menteeId,
        LocalDate applyDate,
        List<String> skillsToEnhance,
        PendingApplicationState state,
        MentorEntity mentor,
        MenteeEntity mentee) {
        this.id = id;
        this.mentorId = mentorId;
        this.menteeId = menteeId;
        this.applyDate = applyDate;
        this.skillsToEnhance = skillsToEnhance;
        this.state = state;
        this.mentor = mentor;
        this.mentee = mentee;
    }
}
