package com.tomtom.service.mentorapply.repository.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "TBL_MENTEES")
public class MenteeEntity {

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

    @Nullable
    @OneToOne(mappedBy = "mentee")
    public PairingEntity pairing;

    @OneToMany(mappedBy = "mentee", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<PendingApplicationEntity> pendingApplications;

    public MenteeEntity() {
        this.id = 0L;
        this.name = "";
        this.jobTitle = null;
        this.location = null;
        this.pairing = null;
        this.pendingApplications = Collections.emptyList();;
    }

    public MenteeEntity(
        long id,
        String name,
        @Nullable String jobTitle,
        @Nullable String location,
        @Nullable PairingEntity pairing,
        List<PendingApplicationEntity> pendingApplications) {
        this.id = id;
        this.name = name;
        this.jobTitle = jobTitle;
        this.location = location;
        this.pairing = pairing;
        this.pendingApplications = pendingApplications;
    }
}
