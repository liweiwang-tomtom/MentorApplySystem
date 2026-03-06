package com.tomtom.service.mentorapply;


import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomtom.service.mentorapply.service.dto.Mentee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = MentorapplyApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MenteeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void createMenteeWithoutSkills() throws Exception {
        String json = """
                {
                    "name": "Bob Anderson",
                    "jobTitle": "Intern"
                }
            """;

        var result = mockMvc.perform(
                post("/api/v1/mentee")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
            )
            .andExpect(status().isOk())
            .andReturn();

        Mentee mentee = parseMentee(result);
        assertNotNull(mentee.id());
        assertEquals("Bob Anderson", mentee.name());
        assertEquals("Intern", mentee.jobTitle());
        assertNull(mentee.location());
        assertNull(mentee.pairingId());
        assertEquals(0, mentee.pendingApplicationIds().size());
    }

    @Test
    void updateMenteeSkillsAndFindItById() throws Exception {
        String json = """
                {
                    "name": "Charlie Wilson",
                    "location": "Berlin"
                }
            """;

        var result = mockMvc.perform(
                post("/api/v1/mentee")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
            )
            .andExpect(status().isOk())
            .andReturn();

        Mentee mentee = parseMentee(result);
        assertNotNull(mentee.id());

        var updated = new Mentee(
            mentee.id(),
            mentee.name(),
            "Engineer I",
            "Amsterdam",
            mentee.pairingId(),
            mentee.pendingApplicationIds()
        );

        var updateResult = mockMvc.perform(
                post("/api/v1/mentee")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updated))
            )
            .andExpect(status().isOk())
            .andReturn();

        Mentee updatedMentee = parseMentee(updateResult);
        assertEquals(mentee.id(), updatedMentee.id());
        assertEquals("Charlie Wilson", updatedMentee.name());
        assertEquals("Engineer I", updatedMentee.jobTitle());
        assertEquals("Amsterdam", updatedMentee.location());
        assertNull(updatedMentee.pairingId());
        assertTrue(updatedMentee.pendingApplicationIds().isEmpty());

        var findResult = mockMvc.perform(get("/api/v1/mentee/" + mentee.id()))
            .andExpect(status().isOk())
            .andReturn();

        Mentee found = parseMentee(findResult);
        assertEquals(updated, found);
    }

    @Test
    void findMenteesByName() throws Exception {
        var json1 = """
                { "name": "Bob Anderson" }
            """;
        var json2 = """
                { "name": "Bob Smith" }
            """;
        var json3 = """
                { "name": "Charlie Wilson" }
            """;

        mockMvc.perform(post("/api/v1/mentee").contentType(MediaType.APPLICATION_JSON).content(json1))
            .andExpect(status().isOk());
        mockMvc.perform(post("/api/v1/mentee").contentType(MediaType.APPLICATION_JSON).content(json2))
            .andExpect(status().isOk());
        mockMvc.perform(post("/api/v1/mentee").contentType(MediaType.APPLICATION_JSON).content(json3))
            .andExpect(status().isOk());

        var findResult = mockMvc.perform(get("/api/v1/mentee/name/bob"))
            .andExpect(status().isOk())
            .andReturn();

        var mentees = parseMenteeList(findResult);

        assertEquals(
            Set.of("Bob Anderson", "Bob Smith"),
            mentees.stream().map(Mentee::name).collect(Collectors.toSet())
        );
    }

    @Test
    void findMenteesByLocation() throws Exception {
        var json1 = """
                { "name": "Bob Anderson", "location": "Amsterdam" }
            """;
        var json2 = """
                { "name": "Bob Smith", "location": "Tokyo" }
            """;
        var json3 = """
                { "name": "Charlie Wilson", "location": "Berlin" }
            """;

        mockMvc.perform(post("/api/v1/mentee").contentType(MediaType.APPLICATION_JSON).content(json1))
            .andExpect(status().isOk());
        mockMvc.perform(post("/api/v1/mentee").contentType(MediaType.APPLICATION_JSON).content(json2))
            .andExpect(status().isOk());
        mockMvc.perform(post("/api/v1/mentee").contentType(MediaType.APPLICATION_JSON).content(json3))
            .andExpect(status().isOk());

        var findResult = mockMvc.perform(get("/api/v1/mentee/location/be"))
            .andExpect(status().isOk())
            .andReturn();

        var mentees = parseMenteeList(findResult);

        assertEquals(
            Set.of("Charlie Wilson"),
            mentees.stream().map(Mentee::name).collect(Collectors.toSet())
        );
    }

    @Test
    void deleteMenteeById() throws Exception {
        var json1 = """
                { "name": "Bob Anderson" }
            """;
        var json2 = """
                { "name": "Bob Smith" }
            """;
        var json3 = """
                { "name": "Charlie Wilson" }
            """;

        var createResult = mockMvc.perform(
                post("/api/v1/mentee")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json1)
            )
            .andExpect(status().isOk())
            .andReturn();

        var mentee1 = parseMentee(createResult);

        mockMvc.perform(post("/api/v1/mentee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json2))
            .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/mentee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json3))
            .andExpect(status().isOk());

        var allResult = mockMvc.perform(get("/api/v1/mentee/all"))
            .andExpect(status().isOk())
            .andReturn();
        var mentees = parseMenteeList(allResult);

        assertEquals(
            Set.of("Bob Anderson", "Bob Smith", "Charlie Wilson"),
            mentees.stream().map(Mentee::name).collect(Collectors.toSet())
        );

        mockMvc.perform(delete("/api/v1/mentee/" + mentee1.id()))
            .andExpect(status().isNoContent());

        var leftResult = mockMvc.perform(get("/api/v1/mentee/all"))
            .andExpect(status().isOk())
            .andReturn();

        var left = parseMenteeList(leftResult);

        assertEquals(
            Set.of("Bob Smith", "Charlie Wilson"),
            left.stream().map(Mentee::name).collect(Collectors.toSet())
        );
    }

    private Mentee parseMentee(MvcResult result) throws Exception {
        return objectMapper.readValue(
            result.getResponse().getContentAsString(),
            Mentee.class
        );
    }

    private List<Mentee> parseMenteeList(MvcResult result) throws Exception {
        String body = result.getResponse().getContentAsString();
        JavaType type = objectMapper.getTypeFactory()
            .constructCollectionType(List.class, Mentee.class);
        return objectMapper.readValue(body, type);
    }
}
