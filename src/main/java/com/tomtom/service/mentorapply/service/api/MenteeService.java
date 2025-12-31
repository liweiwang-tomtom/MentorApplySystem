package com.tomtom.service.mentorapply.service.api;

import com.tomtom.service.mentorapply.dto.Mentee;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MenteeService {
    ResponseEntity<List<Mentee>> getAll();

    ResponseEntity<Mentee> getById(long id);

    ResponseEntity<List<Mentee>> getByName(String name);

    ResponseEntity<List<Mentee>> getByLocation(String location);

    ResponseEntity<Void> deleteById(long id);

    ResponseEntity<Mentee> addOrUpdate(Mentee mentee);
}
