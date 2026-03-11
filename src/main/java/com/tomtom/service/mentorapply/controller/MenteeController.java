package com.tomtom.service.mentorapply.controller;

import com.tomtom.service.mentorapply.service.dto.Mentee;
import com.tomtom.service.mentorapply.service.api.MenteeService;
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
@RequestMapping("api/v1/mentee")
public class MenteeController {
    private final MenteeService menteeService;

    @Autowired
    public MenteeController(final MenteeService menteeService) {
        this.menteeService = menteeService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Mentee>> getAllMentees() {
        return menteeService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mentee> getById(@PathVariable Long id) {
        return menteeService.getById(id);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Mentee>> getByName(@PathVariable String name) {
        return menteeService.getByName(name);
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<Mentee>> getByLocation(@PathVariable String location) {
        return menteeService.getByLocation(location);
    }

    @PostMapping
    public ResponseEntity<Mentee> addOrUpdate(@RequestBody Mentee mentee) {
        return menteeService.addOrUpdate(mentee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        return menteeService.deleteById(id);
    }
}
