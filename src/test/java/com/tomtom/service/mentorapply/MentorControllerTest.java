package com.tomtom.service.mentorapply;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomtom.service.mentorapply.service.dto.Mentor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@Rollback
@SpringBootTest(classes = MentorapplyApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MentorControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void createMentorWithoutSkills() throws Exception {
        String json = """
            {
                "name": "Alice Clinton",
                "jobTitle": "Engineer III",
                "available": true
            }
        """;
        var result = mockMvc.perform(
                post("/api/v1/mentor")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
            )
            .andExpect(status().isOk())
            .andReturn();
        Mentor mentor = parseMentor(result);
        assertNotNull(mentor.id());
        assertEquals("Alice Clinton", mentor.name());
        assertEquals("Engineer III", mentor.jobTitle());
        assertNull(mentor.location());
        assertEquals(0, mentor.skills().size());
        assertTrue(mentor.available());
        assertNull(mentor.pairingId());
        assertEquals(0, mentor.pendingApplicationIds().size());
    }

    @Test
    void updateMentorSkillsAndFindItById() throws Exception {
        String json = """
            {
                "name": "Alice Clinton",
                "skills": ["Kotlin", "JVM"]
            }
        """;
        var result = mockMvc.perform(
                post("/api/v1/mentor")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
            )
            .andExpect(status().isOk())
            .andReturn();
        Mentor mentor = parseMentor(result);
        assertNotNull(mentor.id());
        assertEquals(
            Set.of("Kotlin", "JVM"),
            new HashSet<>(mentor.skills())
        );
        assertFalse(mentor.available());

        var updatedMentor = new Mentor(
            mentor.id(),
            mentor.name(),
            "Engineer I",
            "Amsterdam",
            List.of("Java", "Spring"),
            true,
            mentor.pairingId(),
            mentor.pendingApplicationIds());
        var updateResult = mockMvc.perform(
                post("/api/v1/mentor")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updatedMentor))
            )
            .andExpect(status().isOk())
            .andReturn();
        Mentor updateMentor = parseMentor(updateResult);
        assertEquals(mentor.id(), updateMentor.id());
        assertEquals("Alice Clinton", updateMentor.name());
        assertEquals("Engineer I", updateMentor.jobTitle());
        assertEquals("Amsterdam", updateMentor.location());
        assertEquals(Set.of("Java", "Spring"), new HashSet<>(updateMentor.skills()));
        assertTrue(updateMentor.available());
        assertNull(updateMentor.pairingId());
        assertTrue(updateMentor.pendingApplicationIds().isEmpty());

        var findIdResult = mockMvc.perform(get("/api/v1/mentor/" + mentor.id()))
            .andExpect(status().isOk())
            .andReturn();
        Mentor findIdMentor = parseMentor(findIdResult);
        assertEquals(updatedMentor, findIdMentor);
    }

    @Test
    void findMentorsByName() throws Exception {
        var mentorJson1 = """
            {
                "name": "Alice Clinton"
            }
        """;
        var mentorJson2 = """
            {
                "name": "Alice Smith"
            }
        """;
        var mentorJson3 = """
            {
                "name": "Joe Johnson"
            }
        """;
        mockMvc.perform(
                post("/api/v1/mentor")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mentorJson1)
            )
            .andExpect(status().isOk());
        mockMvc.perform(
                post("/api/v1/mentor")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mentorJson2)
            )
            .andExpect(status().isOk());
        mockMvc.perform(
                post("/api/v1/mentor")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mentorJson3)
            )
            .andExpect(status().isOk());

        var findNameResult = mockMvc.perform(get("/api/v1/mentor/name/alice"))
            .andExpect(status().isOk())
            .andReturn();
        var mentors = parseMentorList(findNameResult);
        assertEquals(
            Set.of("Alice Clinton", "Alice Smith"),
            mentors.stream().map(Mentor::name).collect(Collectors.toSet())
        );
    }

    @Test
    void findMentorsByLocation() throws Exception {
        var mentorJson1 = """
            {
                "name": "Alice Clinton",
                "location": "Amsterdam"
            }
        """;
        var mentorJson2 = """
            {
                "name": "Alice Smith",
                "location": "Tokyo"
            }
        """;
        var mentorJson3 = """
            {
                "name": "Joe Johnson",
                "location": "Berlin"
            }
        """;
        mockMvc.perform(
                post("/api/v1/mentor")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mentorJson1)
            )
            .andExpect(status().isOk());
        mockMvc.perform(
                post("/api/v1/mentor")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mentorJson2)
            )
            .andExpect(status().isOk());
        mockMvc.perform(
                post("/api/v1/mentor")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mentorJson3)
            )
            .andExpect(status().isOk());

        var findLocationResult = mockMvc.perform(get("/api/v1/mentor/location/be"))
            .andExpect(status().isOk())
            .andReturn();
        var mentors = parseMentorList(findLocationResult);
        assertEquals(
            Set.of("Joe Johnson"),
            mentors.stream().map(Mentor::name).collect(Collectors.toSet())
        );
    }

    @Test
    void deleteMentorById() throws Exception {
        var mentorJson1 = """
            {
                "name": "Alice Clinton"
            }
        """;
        var mentorJson2 = """
            {
                "name": "Alice Smith"
            }
        """;
        var mentorJson3 = """
            {
                "name": "Joe Johnson"
            }
        """;
        var createResult1 = mockMvc.perform(
                post("/api/v1/mentor")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mentorJson1)
            )
            .andExpect(status().isOk())
            .andReturn();
        var mentor1 = parseMentor(createResult1);
        mockMvc.perform(
                post("/api/v1/mentor")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mentorJson2)
            )
            .andExpect(status().isOk());
        mockMvc.perform(
                post("/api/v1/mentor")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mentorJson3)
            )
            .andExpect(status().isOk());

        var allResult = mockMvc.perform(get("/api/v1/mentor/all"))
            .andExpect(status().isOk())
            .andReturn();
        var mentors = parseMentorList(allResult);
        assertEquals(
            Set.of("Alice Clinton", "Alice Smith", "Joe Johnson"),
            mentors.stream().map(Mentor::name).collect(Collectors.toSet())
        );

        mockMvc.perform(delete("/api/v1/mentor/" + mentor1.id()))
            .andExpect(status().isNoContent());
        var leftResult = mockMvc.perform(get("/api/v1/mentor/all"))
            .andExpect(status().isOk())
            .andReturn();
        var leftMentors = parseMentorList(leftResult);
        assertEquals(
            Set.of("Alice Smith", "Joe Johnson"),
            leftMentors.stream().map(Mentor::name).collect(Collectors.toSet())
        );
    }

    private Mentor parseMentor(MvcResult result) throws Exception {
        String responseBody = result.getResponse().getContentAsString();
        return objectMapper.readValue(responseBody, Mentor.class);
    }

    private List<Mentor> parseMentorList(MvcResult result) throws Exception {
        String responseBody = result.getResponse().getContentAsString();
        JavaType mentorListType = objectMapper.getTypeFactory()
            .constructCollectionType(List.class, Mentor.class);
        return objectMapper.readValue(responseBody, mentorListType);
    }
}
