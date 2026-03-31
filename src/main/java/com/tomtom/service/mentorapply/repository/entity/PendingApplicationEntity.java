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
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TBL_PENDING_APPLICATIONS")
public class PendingApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mentor_id", nullable = false)
    public MentorEntity mentor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mentee_id", nullable = false)
    public MenteeEntity mentee;

    @Column(name = "apply_date", nullable = false)
    public LocalDate applyDate = LocalDate.now();

    @ElementCollection
    @CollectionTable(
        name = "PENDING_APPLICATIONS_SKILLS",
        joinColumns = @JoinColumn(name = "pending_application_id")
    )
    @Column(name = "skill_to_enhance", nullable = false)
    public List<String> skillsToEnhance = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    public PendingApplicationState state = PendingApplicationState.WAITING_APPROVAL;

    public PendingApplicationEntity(MentorEntity mentor, MenteeEntity mentee) {
        this.mentor = mentor;
        this.mentee = mentee;
    }

    // Required by Hibernate
    public PendingApplicationEntity() {
        this.mentor = null;
        this.mentee = null;
    }
}
