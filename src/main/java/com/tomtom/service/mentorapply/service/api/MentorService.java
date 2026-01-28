package com.tomtom.service.mentorapply.service.api;

import com.tomtom.service.mentorapply.service.dto.Mentor;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MentorService {
    ResponseEntity<List<Mentor>> getAll();

    ResponseEntity<Mentor> getById(long id);

    ResponseEntity<List<Mentor>> getByName(String name);

    ResponseEntity<List<Mentor>> getByLocation(String location);

    ResponseEntity<Void> deleteById(long id);

    ResponseEntity<Mentor> addOrUpdate(Mentor mentor);
}
