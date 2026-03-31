package com.tomtom.service.mentorapply.mapper;

import com.tomtom.service.mentorapply.repository.entity.MenteeEntity;
import com.tomtom.service.mentorapply.repository.entity.MentorEntity;
import com.tomtom.service.mentorapply.repository.entity.PairingEntity;
import com.tomtom.service.mentorapply.repository.entity.PendingApplicationEntity;
import com.tomtom.service.mentorapply.service.dto.Mentee;
import com.tomtom.service.mentorapply.service.dto.MenteeInput;
import com.tomtom.service.mentorapply.service.dto.Mentor;
import com.tomtom.service.mentorapply.service.dto.MentorInput;
import com.tomtom.service.mentorapply.service.dto.Pairing;
import com.tomtom.service.mentorapply.service.dto.PendingApplication;

import java.util.ArrayList;

public class Mapper {
    public static Mentor fromEntity(MentorEntity e) {
        return new Mentor(
            e.id,
            e.name,
            e.jobTitle,
            e.location,
            new ArrayList<>(e.skills),
            e.available,
            e.pairing != null ? e.pairing.id : null,
            e.pendingApplications.stream().map(pa -> pa.id).toList()
        );
    }

    public static MentorEntity toEntityForCreate(MentorInput dto) {
        var e = new MentorEntity();
        assert dto.name() != null;
        e.name = dto.name();
        e.jobTitle = dto.jobTitle();
        e.location = dto.location();
        e.skills = dto.skills() != null ? new ArrayList<>(dto.skills()) : new ArrayList<>();
        e.available = dto.available();
        return e;
    }

    public static MentorEntity updateEntity(MentorEntity e, MentorInput dto) {
        assert dto.name() != null;
        e.name = dto.name();
        e.jobTitle = dto.jobTitle();
        e.location = dto.location();
        e.skills = dto.skills() != null ? new ArrayList<>(dto.skills()) : new ArrayList<>();
        e.available = dto.available();
        return e;
    }

    // ==========================
    // Mentee
    // ==========================

    public static Mentee fromEntity(MenteeEntity e) {
        return new Mentee(
            e.id,
            e.name,
            e.jobTitle,
            e.location,
            e.pairing != null ? e.pairing.id : null,
            e.pendingApplications.stream().map(pa -> pa.id).toList()
        );
    }

    public static MenteeEntity toEntityForCreate(MenteeInput dto) {
        var e = new MenteeEntity();
        assert dto.name() != null;
        e.name = dto.name();
        e.jobTitle = dto.jobTitle();
        e.location = dto.location();
        return e;
    }

    public static MenteeEntity updateEntity(MenteeEntity e, MenteeInput dto) {
        assert dto.name() != null;
        e.name = dto.name();
        e.jobTitle = dto.jobTitle();
        e.location = dto.location();
        return e;
    }

    // ==========================
    // Pairing
    // ==========================

    public static Pairing fromEntity(PairingEntity e) {
        return new Pairing(
            e.id,
            e.mentor.id,
            e.mentee.id,
            e.applyDate,
            e.startDate,
            e.endDate,
            new ArrayList<>(e.skillsToEnhance)
        );
    }

    // ==========================
    // PendingApplication
    // ==========================

    public static PendingApplication fromEntity(PendingApplicationEntity e) {
        return new PendingApplication(
            e.id,
            e.mentor.id,
            e.mentee.id,
            e.applyDate,
            new ArrayList<>(e.skillsToEnhance),
            e.state
        );
    }
}
