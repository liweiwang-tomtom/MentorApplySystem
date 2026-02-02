package com.tomtom.service.mentorapply.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TBL_MENTEES")
public class MenteeEntity {
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

    @Nullable
    @OneToOne(mappedBy = "mentee")
    @JsonIgnore
    public PairingEntity pairing; // @OneToOne(mappedBy = "mentee", optional = true)

    @OneToMany(mappedBy = "mentee", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    public List<PendingApplicationEntity> pendingApplications = new ArrayList<>();

    public MenteeEntity() {
    }
}
