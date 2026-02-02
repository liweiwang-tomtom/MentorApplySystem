package com.tomtom.service.mentorapply.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TBL_MENTORS")

public class MentorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long id;

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
    @Column(name = "skill", nullable = false)
    public List<String> skills = new ArrayList<>();

    @Column(name = "available", nullable = false)
    public boolean available;

    @Nullable
    @OneToOne(mappedBy = "mentor")
    @JsonIgnore
    public PairingEntity pairing;

    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    public List<PendingApplicationEntity> pendingApplications = new ArrayList<>();

    public MentorEntity() {
    }
}
