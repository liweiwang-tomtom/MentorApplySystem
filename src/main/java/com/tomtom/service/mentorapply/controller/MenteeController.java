package com.tomtom.service.mentorapply.controller;

import com.tomtom.service.mentorapply.dto.Mentee;
import com.tomtom.service.mentorapply.service.api.MenteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/mentor")
public class MenteeController {
    private final MenteeService menteeService;

    @Autowired
    public MenteeController(@NonNull final MenteeService menteeService) {
        this.menteeService = menteeService;
    }

    @GetMapping("/all")
    @NonNull
    public ResponseEntity<List<Mentee>> getAllMentees() {
        return menteeService.getAll();
    }
}
