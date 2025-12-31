package com.tomtom.service.mentorapply.service.mentee;

import com.tomtom.service.mentorapply.dto.Mentee;
import com.tomtom.service.mentorapply.entity.MenteeEntity;
import com.tomtom.service.mentorapply.mapper.Mapper;
import com.tomtom.service.mentorapply.repository.MenteeRepository;
import com.tomtom.service.mentorapply.service.api.MenteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenteeServiceImpl implements MenteeService {
    private final MenteeRepository menteeRepository;

    @Autowired
    public MenteeServiceImpl(@NonNull MenteeRepository menteeRepository) {
        this.menteeRepository = menteeRepository;
    }

    @Override
    public ResponseEntity<List<Mentee>> getAll() {
        var mentees = menteeRepository
            .findAll()
            .stream()
            .map(Mapper::fromEntity)
            .toList();
        return ResponseEntity.ok(mentees);
    }

    @Override
    public ResponseEntity<Mentee> getById(long id) {
        return toResponse(menteeRepository.findById(id));
    }

    @Override
    public ResponseEntity<List<Mentee>> getByName(String name) {
        var mentees = menteeRepository
            .findByName(name)
            .stream()
            .map(Mapper::fromEntity)
            .toList();
        return ResponseEntity.ok(mentees);
    }

    @Override
    public ResponseEntity<List<Mentee>> getByLocation(String location) {
        var mentees = menteeRepository
            .findByLocation(location)
            .stream()
            .map(Mapper::fromEntity)
            .toList();
        return ResponseEntity.ok(mentees);
    }

    @Override
    public ResponseEntity<Void> deleteById(long id) {
        menteeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Mentee> addOrUpdate(Mentee mentee) {
        var entity = menteeRepository.save(Mapper.toEntity(mentee));
        return toResponse(entity);
    }

    private ResponseEntity<Mentee> toResponse(@Nullable MenteeEntity entity) {
        return entity == null
            ? ResponseEntity.noContent().build()
            : ResponseEntity.ok(Mapper.fromEntity(entity));
    }
}
