package com.tomtom.service.mentorapply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomtom.service.mentorapply.service.dto.Mentee;
import com.tomtom.service.mentorapply.service.dto.Mentor;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.tomtom.service.mentorapply.utils.ResultMapper.parseMentee;
import static com.tomtom.service.mentorapply.utils.ResultMapper.parseMentor;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@Rollback
@SpringBootTest(classes = MentorapplyApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class BasePairingControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    protected Mentor mentor1 = null;
    protected Mentor mentor2 = null;
    protected Mentor mentor3 = null;
    protected Mentee mentee1 = null;
    protected Mentee mentee2 = null;
    protected Mentee mentee3 = null;

    @BeforeEach
    void setup() throws Exception {
        mentor1 = createMentor(JSON_MENTOR_1);
        mentor2 = createMentor(JSON_MENTOR_2);
        mentor3 = createMentor(JSON_MENTOR_3);
        mentee1 = createMentee(JSON_MENTEE_1);
        mentee2 = createMentee(JSON_MENTEE_2);
        mentee3 = createMentee(JSON_MENTEE_3);
        assertNotNull(mentor1);
        assertNotNull(mentor2);
        assertNotNull(mentor3);
        assertNotNull(mentee1);
        assertNotNull(mentee2);
        assertNotNull(mentee3);
    }

    protected Mentor createMentor(String json) throws Exception {
        var result = mockMvc.perform(
                post("/api/v1/mentor")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
            )
            .andExpect(status().isOk())
            .andReturn();
        return parseMentor(result, objectMapper);
    }

    protected Mentee createMentee(String json) throws Exception {
        var result = mockMvc.perform(
                post("/api/v1/mentee")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
            )
            .andExpect(status().isOk())
            .andReturn();
        return parseMentee(result, objectMapper);
    }

    static final String JSON_MENTOR_1 = """
            {
                "name": "mentor1",
                "jobTitle": "Engineer III",
                "available": true
            }
        """;
    static final String JSON_MENTOR_2 = """
            {
                "name": "mentor2",
                "jobTitle": "Engineer IV",
                "available": true
            }
        """;
    static final String JSON_MENTOR_3 = """
            {
                "name": "mentor3",
                "jobTitle": "Engineer V",
                "available": false
            }
        """;
    static final String JSON_MENTEE_1 = """
            {
                "name": "mentee1",
                "jobTitle": "Engineer I"
            }
        """;
    static final String JSON_MENTEE_2 = """
            {
                "name": "mentor2",
                "jobTitle": "Engineer II"
            }
        """;
    static final String JSON_MENTEE_3 = """
            {
                "name": "mentor3",
                "jobTitle": "Engineer III"
            }
        """;
}
