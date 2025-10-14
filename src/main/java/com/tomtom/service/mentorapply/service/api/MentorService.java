package com.tomtom.service.mentorapply.service.api;

import com.tomtom.service.mentorapply.dto.Mentor;
import com.tomtom.service.mentorapply.reponse.Response;
import org.springframework.lang.NonNull;

public interface MentorService {
    @NonNull
    Response<Mentor[]> getAll();
}
