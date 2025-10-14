package com.tomtom.service.mentorapply.mapper;

import com.tomtom.service.mentorapply.dto.Mentor;
import com.tomtom.service.mentorapply.dto.PendingApplication;
import com.tomtom.service.mentorapply.entity.MentorEntity;

import java.util.Arrays;

public class Mapper {
    public static MentorEntity toEntity(final Mentor mentor) {
        return new MentorEntity(
            mentor.id(),
            mentor.name(),
            mentor.jobTitle(),
            mentor.location(),
            mentor.skills(),
            mentor.available(),
            mentor.pairing() != null ? mentor.pairing().id() : 0,
            Arrays.stream(mentor.pendingApplications()).mapToLong(PendingApplication::id).toArray());
    }

    public static Mentor fromEntity(final MentorEntity mentorEntity) {
        return new Mentor(
            mentorEntity.id,
            mentorEntity.name,
            mentorEntity.jobTitle,
            mentorEntity.location,
            mentorEntity.skills,
            mentorEntity.available,
            null,
            new PendingApplication[0]
        );
    }
}
