package com.tomtom.service.mentorapply.controller;

import com.tomtom.service.mentorapply.service.dto.Mentor;
import com.tomtom.service.mentorapply.service.api.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/mentor")
public class MentorController {
    private final MentorService mentorService;

    @Autowired
    public MentorController(final MentorService mentorService) {
        this.mentorService = mentorService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Mentor>> getAllMentors() {
        return mentorService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mentor> getById(@PathVariable long id) {
        return mentorService.getById(id);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Mentor>> getByName(@PathVariable String name) {
        return mentorService.getByName(name);
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<Mentor>> getByLocation(@PathVariable String location) {
        return mentorService.getByLocation(location);
    }

    @PostMapping
    public ResponseEntity<Mentor> addOrUpdate(@RequestBody Mentor mentor) {
        return mentorService.addOrUpdate(mentor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        return mentorService.deleteById(id);
    }
}
