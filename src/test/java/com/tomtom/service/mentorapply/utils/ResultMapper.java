package com.tomtom.service.mentorapply.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomtom.service.mentorapply.service.dto.Mentee;
import com.tomtom.service.mentorapply.service.dto.Mentor;
import com.tomtom.service.mentorapply.service.dto.Pairing;
import com.tomtom.service.mentorapply.service.dto.PendingApplication;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

public class ResultMapper {
    static public Mentor parseMentor(MvcResult result, ObjectMapper objectMapper) throws Exception {
        String responseBody = result.getResponse().getContentAsString();
        return objectMapper.readValue(responseBody, Mentor.class);
    }

    static public List<Mentor> parseMentorList(MvcResult result, ObjectMapper objectMapper) throws Exception {
        String responseBody = result.getResponse().getContentAsString();
        JavaType mentorListType = objectMapper.getTypeFactory()
            .constructCollectionType(List.class, Mentor.class);
        return objectMapper.readValue(responseBody, mentorListType);
    }

    static public Mentee parseMentee(MvcResult result, ObjectMapper objectMapper) throws Exception {
        return objectMapper.readValue(
            result.getResponse().getContentAsString(),
            Mentee.class
        );
    }

    static public List<Mentee> parseMenteeList(MvcResult result, ObjectMapper objectMapper) throws Exception {
        String body = result.getResponse().getContentAsString();
        JavaType type = objectMapper.getTypeFactory()
            .constructCollectionType(List.class, Mentee.class);
        return objectMapper.readValue(body, type);
    }

    static public PendingApplication parseApplication(MvcResult result, ObjectMapper objectMapper) throws Exception {
        return objectMapper.readValue(
            result.getResponse().getContentAsString(),
            PendingApplication.class
        );
    }

    static public List<PendingApplication> parseApplicationList(MvcResult result, ObjectMapper objectMapper) throws Exception {
        String body = result.getResponse().getContentAsString();
        JavaType type = objectMapper.getTypeFactory()
            .constructCollectionType(List.class, PendingApplication.class);
        return objectMapper.readValue(body, type);
    }

    static public Pairing parsePairing(MvcResult result, ObjectMapper objectMapper) throws Exception {
        return objectMapper.readValue(
            result.getResponse().getContentAsString(),
            Pairing.class
        );
    }
}
