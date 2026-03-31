package com.tomtom.service.mentorapply.service.mentee;

import com.tomtom.service.mentorapply.mapper.Mapper;
import com.tomtom.service.mentorapply.repository.MenteeRepository;
import com.tomtom.service.mentorapply.service.api.MenteeService;
import com.tomtom.service.mentorapply.service.dto.Mentee;
import com.tomtom.service.mentorapply.service.dto.MenteeInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenteeServiceImpl implements MenteeService {
    private final MenteeRepository menteeRepository;

    @Autowired
    public MenteeServiceImpl(MenteeRepository menteeRepository) {
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
        return menteeRepository
            .findById(id)
            .map(e -> ResponseEntity.ok(Mapper.fromEntity(e)))
            .orElseGet(() -> ResponseEntity.noContent().build());
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
    public ResponseEntity<Mentee> addOrUpdate(MenteeInput mentee) {
        if (mentee.name() == null) {
            return ResponseEntity.badRequest().build();
        }
        if (mentee.id() == null) {
            var saved = menteeRepository.save(Mapper.toEntityForCreate(mentee));
            return ResponseEntity.ok(Mapper.fromEntity(saved));
        }

        var saved = menteeRepository.findById(mentee.id())
            .map(e -> menteeRepository.save(Mapper.updateEntity(e, mentee)))
            .orElseGet(() -> menteeRepository.save(Mapper.toEntityForCreate(mentee)));

        return ResponseEntity.ok(Mapper.fromEntity(saved));
    }
}
