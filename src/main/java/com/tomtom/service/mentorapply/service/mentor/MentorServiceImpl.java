package com.tomtom.service.mentorapply.service.mentor;

import com.tomtom.service.mentorapply.dto.Mentor;
import com.tomtom.service.mentorapply.mapper.Mapper;
import com.tomtom.service.mentorapply.reponse.Response;
import com.tomtom.service.mentorapply.reponse.ResponseCode;
import com.tomtom.service.mentorapply.repository.MentorRepository;
import com.tomtom.service.mentorapply.service.api.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class MentorServiceImpl implements MentorService {
    private final MentorRepository mentorRepository;

    @Autowired
    public MentorServiceImpl(@NonNull MentorRepository mentorRepository) {
        this.mentorRepository = mentorRepository;
    }

    @Override
    @NonNull
    public Response<Mentor[]> getAll() {
        Mentor[] mentors = mentorRepository
                .findAll()
                .stream()
                .map(Mapper::fromEntity)
                .toArray(Mentor[]::new);
        return new Response<>(mentors, ResponseCode.SUCCESS.code());
    }
}
