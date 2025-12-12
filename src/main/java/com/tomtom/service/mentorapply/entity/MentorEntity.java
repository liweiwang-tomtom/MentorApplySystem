package com.tomtom.service.mentorapply.entity;


import com.tomtom.service.mentorapply.dto.PendingApplication;
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
import org.springframework.lang.NonNull;

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

    @Column(name = "job_title")
    public String jobTitle;

    @Column(name = "location")
    public String location;

    @ElementCollection
    @CollectionTable(name = "MENTOR_SKILLS", joinColumns = @JoinColumn(name = "mentor_id"))
    @Column(name = "skill")
    public List<String> skills;

    @Column(name = "available", nullable = false)
    public boolean available;

    @OneToOne(mappedBy = "mentor")
    public PairingEntity pairing;

    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<PendingApplicationEntity> pendingApplications;

    public MentorEntity(
        long id,
        @NonNull String name,
        String jobTitle,
        String location,
        List<String> skills,
        boolean available,
        PairingEntity pairing,
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
    }
}
