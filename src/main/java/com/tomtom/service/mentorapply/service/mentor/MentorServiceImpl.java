package com.tomtom.service.mentorapply.service.mentor;

import com.tomtom.service.mentorapply.dto.Mentor;
import com.tomtom.service.mentorapply.entity.MentorEntity;
import com.tomtom.service.mentorapply.mapper.Mapper;
import com.tomtom.service.mentorapply.repository.MentorRepository;
import com.tomtom.service.mentorapply.service.api.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MentorServiceImpl implements MentorService {
    private final MentorRepository mentorRepository;

    @Autowired
    public MentorServiceImpl(@NonNull MentorRepository mentorRepository) {
        this.mentorRepository = mentorRepository;
    }

    @Override
    public ResponseEntity<List<Mentor>> getAll() {
        var mentors = mentorRepository
            .findAll()
            .stream()
            .map(Mapper::fromEntity)
            .toList();
        return ResponseEntity.ok(mentors);
    }

    @Override
    public ResponseEntity<Mentor> getById(long id) {
        return toResponse(mentorRepository.findById(id));
    }

    @Override
    public ResponseEntity<List<Mentor>> getByName(String name) {
        var mentors = mentorRepository
            .findByName(name)
            .stream()
            .map(Mapper::fromEntity)
            .toList();
        return ResponseEntity.ok(mentors);
    }

    @Override
    public ResponseEntity<List<Mentor>> getByLocation(String location) {
        var mentors = mentorRepository
            .findByLocation(location)
            .stream()
            .map(Mapper::fromEntity)
            .toList();
        return ResponseEntity.ok(mentors);
    }

    @Override
    public ResponseEntity<Void> deleteById(long id) {
        mentorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Mentor> addOrUpdate(Mentor mentor) {
        var entity = mentorRepository.save(Mapper.toEntity(mentor));
        return toResponse(entity);
    }

    private ResponseEntity<Mentor> toResponse(@Nullable MentorEntity entity) {
        return entity == null
            ? ResponseEntity.noContent().build()
            : ResponseEntity.ok(Mapper.fromEntity(entity));
    }
}
