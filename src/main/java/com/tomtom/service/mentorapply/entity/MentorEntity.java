package com.tomtom.service.mentorapply.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.lang.NonNull;

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

    @Column(name = "skills")
    @Convert(converter = StringArrayConverter.class)
    public String[] skills;

    @Column(name = "available", nullable = false)
    @Convert(converter = StringArrayConverter.class)
    public boolean available;

    @Column(name = "pairing_id", nullable = false)
    public long pairingId;

    @Column(name = "pending_application_ids", nullable = false)
    @Convert(converter = LongArrayConverter.class)
    public long[] pendingApplicationIds;

    public MentorEntity(
            long id,
            @NonNull String name,
            String jobTitle,
            String location,
            String[] skills,
            boolean available,
            long pairingId,
            long[] pendingApplicationIds) {
        this.id = id;
        this.name = name;
        this.jobTitle = jobTitle;
        this.location = location;
        this.skills = skills;
        this.available = available;
        this.pairingId = pairingId;
        this.pendingApplicationIds = pendingApplicationIds;
    }

    public MentorEntity() {
        this.id = 0L;
        this.name = "";
    }
}
