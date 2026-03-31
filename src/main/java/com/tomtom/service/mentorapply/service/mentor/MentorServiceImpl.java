package com.tomtom.service.mentorapply.service.mentor;

import com.tomtom.service.mentorapply.mapper.Mapper;
import com.tomtom.service.mentorapply.repository.MentorRepository;
import com.tomtom.service.mentorapply.service.api.MentorService;
import com.tomtom.service.mentorapply.service.dto.Mentor;
import com.tomtom.service.mentorapply.service.dto.MentorInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MentorServiceImpl implements MentorService {
    private final MentorRepository mentorRepository;

    @Autowired
    public MentorServiceImpl(MentorRepository mentorRepository) {
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
        return mentorRepository
            .findById(id)
            .map(e -> ResponseEntity.ok(Mapper.fromEntity(e)))
            .orElseGet(() -> ResponseEntity.noContent().build());
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
    public ResponseEntity<Mentor> addOrUpdate(MentorInput mentor) {
        if (mentor.name() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (mentor.id() == null) {
            var saved = mentorRepository.save(Mapper.toEntityForCreate(mentor));
            return ResponseEntity.ok(Mapper.fromEntity(saved));
        }

        var saved = mentorRepository.findById(mentor.id())
            .map(e -> mentorRepository.save(Mapper.updateEntity(e, mentor)))
            .orElseGet(() -> mentorRepository.save(Mapper.toEntityForCreate(mentor)));

        return ResponseEntity.ok(Mapper.fromEntity(saved));

    }
}
