package com.tomtom.service.mentorapply.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "TBL_PENDING_APPLICATIONS")
public class PendingApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public long id;

    @Column(name = "mentor_id", nullable = false)
    public long mentorId;

    @Column(name = "mentee_id", nullable = false)
    public long menteeId;

    @Column(name = "apply_date", nullable = false)
    public LocalDate applyDate;

    @ElementCollection
    @Column(name = "skill_to_enhance")
    public List<String> skillsToEnhance;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    public State state;

    @ManyToOne
    @JoinColumn(name = "mentor_id", insertable = false, updatable = false)
    public MentorEntity mentor;

    @ManyToOne
    @JoinColumn(name = "mentee_id", insertable = false, updatable = false)
    public MenteeEntity mentee;

    public enum State {
        WAITING_APPROVAL,
        APPROVED,
        CANCELED
    }

    public PendingApplicationEntity() {}

    public PendingApplicationEntity(
        long id,
        long mentorId,
        long menteeId,
        LocalDate applyDate,
        List<String> skillsToEnhance,
        State state,
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
