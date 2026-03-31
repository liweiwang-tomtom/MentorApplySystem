package com.tomtom.service.mentorapply.repository.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TBL_PAIRINGS")
public class PairingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "mentor_id", nullable = false, unique = true)
    public MentorEntity mentor;

    @OneToOne(optional = false)
    @JoinColumn(name = "mentee_id", nullable = false, unique = true)
    public MenteeEntity mentee;

    @Column(name = "apply_date")
    public LocalDate applyDate = LocalDate.now();

    @Column(name = "start_date")
    public LocalDate startDate = LocalDate.now();

    @Column(name = "end_date")
    public LocalDate endDate = LocalDate.now().plusMonths(6);

    @ElementCollection
    @CollectionTable(name = "PAIRING_SKILLS", joinColumns = @JoinColumn(name = "pairing_id"))
    @Column(name = "skill_to_enhance", nullable = false)
    public List<String> skillsToEnhance = new ArrayList<>();

    public PairingEntity(MentorEntity mentor, MenteeEntity mentee) {
        this.mentor = mentor;
        this.mentee = mentee;
    }

    // Required by Hibernate
    public PairingEntity() {
        this.mentor = null;
        this.mentee = null;
    }
}
