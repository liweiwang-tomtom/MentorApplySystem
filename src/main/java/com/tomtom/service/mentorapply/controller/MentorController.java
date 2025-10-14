package com.tomtom.service.mentorapply.controller;

import com.tomtom.service.mentorapply.dto.Mentor;
import com.tomtom.service.mentorapply.reponse.Response;
import com.tomtom.service.mentorapply.service.api.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/mentor")
public class MentorController {
    private final MentorService mentorService;

    @Autowired
    public MentorController(@NonNull final MentorService mentorService) {
        this.mentorService = mentorService;
    }

    @GetMapping("/all")
    @NonNull
    public Response<Mentor[]> getAllMentors() {
        return mentorService.getAll();
    }
}
