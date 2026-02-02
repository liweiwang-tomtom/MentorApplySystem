package com.tomtom.service.mentorapply.mapper;

import com.tomtom.service.mentorapply.repository.entity.MenteeEntity;
import com.tomtom.service.mentorapply.repository.entity.MentorEntity;
import com.tomtom.service.mentorapply.repository.entity.PairingEntity;
import com.tomtom.service.mentorapply.repository.entity.PendingApplicationEntity;
import com.tomtom.service.mentorapply.service.dto.Mentee;
import com.tomtom.service.mentorapply.service.dto.Mentor;
import com.tomtom.service.mentorapply.service.dto.Pairing;
import com.tomtom.service.mentorapply.service.dto.PendingApplication;

import java.util.List;

public class Mapper {
    public static Mentor fromEntity(MentorEntity e) {
        return new Mentor(
            e.id,
            e.name,
            e.jobTitle,
            e.location,
            List.copyOf(e.skills),
            e.available,
            e.pairing != null ? e.pairing.id : null,
            e.pendingApplications.stream().map(pa -> pa.id).toList()
        );
    }

    public static MentorEntity toEntityForCreate(Mentor dto) {
        var e = new MentorEntity();
        e.id = dto.id();
        e.name = dto.name();
        e.jobTitle = dto.jobTitle();
        e.location = dto.location();
        e.skills = List.copyOf(dto.skills());
        e.available = dto.available();
        return e;
    }

    public static MentorEntity updateEntity(MentorEntity e, Mentor dto) {
        e.name = dto.name();
        e.jobTitle = dto.jobTitle();
        e.location = dto.location();
        e.skills = List.copyOf(dto.skills());
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

    public static MenteeEntity toEntityForCreate(Mentee dto) {
        var e = new MenteeEntity();
        e.id = dto.id();
        e.name = dto.name();
        e.jobTitle = dto.jobTitle();
        e.location = dto.location();
        return e;
    }

    public static MenteeEntity updateEntity(MenteeEntity e, Mentee dto) {
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
            List.copyOf(e.skillsToEnhance)
        );
    }

    public static PairingEntity toEntity(Pairing dto, MentorEntity mentor, MenteeEntity mentee) {
        var e = new PairingEntity();
        e.id = dto.id();
        e.mentor = mentor;
        e.mentee = mentee;
        e.applyDate = dto.applyDate();
        e.startDate = dto.startDate();
        e.endDate = dto.endDate();
        e.skillsToEnhance = List.copyOf(dto.skillsToEnhance());
        return e;
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
            List.copyOf(e.skillsToEnhance),
            e.state
        );
    }

    public static PendingApplicationEntity toEntity(
        PendingApplication dto,
        MentorEntity mentor,
        MenteeEntity mentee
    ) {
        var e = new PendingApplicationEntity();
        e.id = dto.id();
        e.mentor = mentor;
        e.mentee = mentee;
        e.applyDate = dto.applyDate();
        e.skillsToEnhance = List.copyOf(dto.skillsToEnhance());
        e.state = dto.state();
        return e;
    }

}
