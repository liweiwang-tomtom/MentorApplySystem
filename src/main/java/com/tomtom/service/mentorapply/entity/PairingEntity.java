package com.tomtom.service.mentorapply.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.springframework.lang.NonNull;

import java.util.List;

@Entity
@Table(name = "TBL_PAIRINGS")
public class PairingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public long id;

    @Column(name = "mentor_id", nullable = false)
    public long mentorId;

    @Column(name = "mentee_id", nullable = false)
    public long menteeId;

    @Column(name = "apply_date")
    public long applyDate;

    @Column(name = "start_date")
    public long startDate;

    @Column(name = "end_date")
    public long endDate;

    @ElementCollection
    @Column(name = "skill_to_enhance")
    public List<String> skillsToEnhance;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    public State state;

    @OneToOne(mappedBy = "pairing", cascade = CascadeType.ALL)
    public MentorEntity mentor;

    @OneToOne(mappedBy = "pairing", cascade = CascadeType.ALL)
    public MenteeEntity mentee;

    public enum State {
        NOT_STARTED,
        STARTED,
        FINISHED,
        CANCELED
    }

    public PairingEntity() {
    }

    public PairingEntity(
        long id,
        long mentorId,
        long menteeId,
        long applyDate,
        long startDate,
        long endDate,
        @NonNull List<String> skillsToEnhance,
        State state,
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
