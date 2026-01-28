package com.tomtom.service.mentorapply.repository.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "TBL_MENTORS")
public class MentorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public long id;

    @Column(name = "name", nullable = false)
    public String name;

    @Nullable
    @Column(name = "job_title")
    public String jobTitle;

    @Nullable
    @Column(name = "location")
    public String location;

    @ElementCollection
    @CollectionTable(name = "MENTOR_SKILLS", joinColumns = @JoinColumn(name = "mentor_id"))
    @Column(name = "skill")
    public List<String> skills;

    @Column(name = "available", nullable = false)
    public boolean available;

    @Nullable
    @OneToOne(mappedBy = "mentor")
    public PairingEntity pairing;

    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<PendingApplicationEntity> pendingApplications;

    public MentorEntity(
        long id,
        String name,
        @Nullable String jobTitle,
        @Nullable String location,
        List<String> skills,
        boolean available,
        @Nullable PairingEntity pairing,
        List<PendingApplicationEntity> pendingApplications) {
        this.id = id;
        this.name = name;
        this.jobTitle = jobTitle;
        this.location = location;
        this.skills = skills;
        this.available = available;
        this.pairing = pairing;
        this.pendingApplications = pendingApplications;
    }

    public MentorEntity() {
        this.id = 0L;
        this.name = "";
        this.jobTitle = null;
        this.location = null;
        this.skills = Collections.emptyList();
        this.available = false;
        this.pairing = null;
        this.pendingApplications = Collections.emptyList();
    }
}
