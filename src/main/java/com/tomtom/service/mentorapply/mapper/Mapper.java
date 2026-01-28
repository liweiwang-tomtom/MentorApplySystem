package com.tomtom.service.mentorapply.mapper;

import com.tomtom.service.mentorapply.repository.entity.MenteeEntity;
import com.tomtom.service.mentorapply.repository.entity.MentorEntity;
import com.tomtom.service.mentorapply.repository.entity.PairingEntity;
import com.tomtom.service.mentorapply.repository.entity.PendingApplicationEntity;
import com.tomtom.service.mentorapply.service.dto.Mentee;
import com.tomtom.service.mentorapply.service.dto.Mentor;
import com.tomtom.service.mentorapply.service.dto.Pairing;
import com.tomtom.service.mentorapply.service.dto.PendingApplication;

public class Mapper {
    public static PairingEntity toEntity(final Pairing pairing) {
        return new PairingEntity(
            pairing.id(),
            pairing.mentorId(),
            pairing.menteeId(),
            pairing.applyDate(),
            pairing.startDate(),
            pairing.endDate(),
            pairing.skillsToEnhance(),
            pairing.state(),
            null,
            null
        );
    }

    public static Pairing fromEntity(final PairingEntity entity) {
        return new Pairing(
            entity.id,
            entity.mentorId,
            entity.menteeId,
            entity.applyDate,
            entity.startDate,
            entity.endDate,
            entity.skillsToEnhance,
            entity.state
        );
    }

    public static PendingApplicationEntity toEntity(final PendingApplication pendingApplication) {
        return new PendingApplicationEntity(
            pendingApplication.id(),
            pendingApplication.mentorId(),
            pendingApplication.menteeId(),
            pendingApplication.applyDate(),
            pendingApplication.skillsToEnhance(),
            pendingApplication.state(),
            null,
            null
        );
    }

    public static PendingApplication fromEntity(final PendingApplicationEntity entity) {
        return new PendingApplication(
            entity.id,
            entity.mentorId,
            entity.menteeId,
            entity.applyDate,
            entity.skillsToEnhance,
            entity.state
        );
    }

    public static MentorEntity toEntity(final Mentor mentor) {
        var pairing = mentor.pairing();
        return new MentorEntity(
            mentor.id(),
            mentor.name(),
            mentor.jobTitle(),
            mentor.location(),
            mentor.skills(),
            mentor.available(),
            pairing != null ? toEntity(pairing) : null,
            mentor.pendingApplications().stream().map(Mapper::toEntity).toList()
        );
    }

    public static Mentor fromEntity(final MentorEntity mentorEntity) {
        var pairing = mentorEntity.pairing;
        return new Mentor(
            mentorEntity.id,
            mentorEntity.name,
            mentorEntity.jobTitle,
            mentorEntity.location,
            mentorEntity.skills,
            mentorEntity.available,
            pairing != null ? fromEntity(pairing) : null,
            mentorEntity.pendingApplications.stream().map(Mapper::fromEntity).toList()
        );
    }

    public static MenteeEntity toEntity(final Mentee mentee) {
        var pairing = mentee.pairing();
        return new MenteeEntity(
            mentee.id(),
            mentee.name(),
            mentee.jobTitle(),
            mentee.location(),
            pairing != null ? toEntity(pairing) : null,
            mentee.pendingApplications().stream().map(Mapper::toEntity).toList()
        );
    }

    public static Mentee fromEntity(final MenteeEntity entity) {
        var pairing = entity.pairing;
        return new Mentee(
            entity.id,
            entity.name,
            entity.jobTitle,
            entity.location,
            pairing != null ? fromEntity(pairing) : null,
            entity.pendingApplications.stream().map(Mapper::fromEntity).toList()
        );
    }
}
