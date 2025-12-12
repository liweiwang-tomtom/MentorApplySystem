package com.tomtom.service.mentorapply.mapper;

import com.tomtom.service.mentorapply.dto.Mentee;
import com.tomtom.service.mentorapply.dto.Mentor;
import com.tomtom.service.mentorapply.dto.Pairing;
import com.tomtom.service.mentorapply.dto.PendingApplication;
import com.tomtom.service.mentorapply.entity.MenteeEntity;
import com.tomtom.service.mentorapply.entity.MentorEntity;
import com.tomtom.service.mentorapply.entity.PairingEntity;
import com.tomtom.service.mentorapply.entity.PendingApplicationEntity;

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
        return new MentorEntity(
            mentor.id(),
            mentor.name(),
            mentor.jobTitle(),
            mentor.location(),
            mentor.skills(),
            mentor.available(),
            toEntity(mentor.pairing()),
            mentor.pendingApplications().stream().map(Mapper::toEntity).toList()
        );
    }

    public static Mentor fromEntity(final MentorEntity mentorEntity) {
        return new Mentor(
            mentorEntity.id,
            mentorEntity.name,
            mentorEntity.jobTitle,
            mentorEntity.location,
            mentorEntity.skills,
            mentorEntity.available,
            fromEntity(mentorEntity.pairing),
            mentorEntity.pendingApplications.stream().map(Mapper::fromEntity).toList()
        );
    }

    public static MenteeEntity toEntity(final Mentee mentee) {
        return new MenteeEntity(
            mentee.id(),
            mentee.name(),
            mentee.jobTitle(),
            mentee.location(),
            toEntity(mentee.pairing()),
            mentee.pendingApplications().stream().map(Mapper::toEntity).toList()
        );
    }

    public static Mentee fromEntity(final MenteeEntity entity) {
        return new Mentee(
            entity.id,
            entity.name,
            entity.jobTitle,
            entity.location,
            fromEntity(entity.pairing),
            entity.pendingApplications.stream().map(Mapper::fromEntity).toList()
        );
    }
}
