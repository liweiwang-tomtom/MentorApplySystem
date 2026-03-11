package com.tomtom.service.mentorapply;

import com.tomtom.service.mentorapply.service.dto.PendingApplicationState;
import org.junit.jupiter.api.Test;

import static com.tomtom.service.mentorapply.utils.ResultMapper.parseApplication;
import static com.tomtom.service.mentorapply.utils.ResultMapper.parseApplicationList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApplicationSubmissionTest extends BasePairingControllerTest {
    @Test
    void mentee_can_submit_applications_to_multiple_mentors() throws Exception {
        var submitApplicationResult = mockMvc.perform(
                post("/api/v1/pairings/applications")
                    .param("mentorId", mentor1.id().toString())
                    .param("menteeId", mentee1.id().toString())
            )
            .andExpect(status().isOk())
            .andReturn();
        var pendingApplication = parseApplication(submitApplicationResult, objectMapper);
        assertNotNull(pendingApplication.id());
        assertEquals(PendingApplicationState.WAITING_APPROVAL, pendingApplication.state());
        submitApplicationResult = mockMvc.perform(
                post("/api/v1/pairings/applications")
                    .param("mentorId", mentor2.id().toString())
                    .param("menteeId", mentee1.id().toString())
            )
            .andExpect(status().isOk())
            .andReturn();
        pendingApplication = parseApplication(submitApplicationResult, objectMapper);
        assertNotNull(pendingApplication.id());
        assertEquals(PendingApplicationState.WAITING_APPROVAL, pendingApplication.state());

        var findApplicationByMenteeIdResult = mockMvc.perform(
                get("/api/v1/pairings/applications/mentee/" + mentee1.id().toString())
            )
            .andExpect(status().isOk())
            .andReturn();
        var allApplications = parseApplicationList(findApplicationByMenteeIdResult, objectMapper);
        assertEquals(2, allApplications.size());

        var findApplicationByMentorIdResult = mockMvc.perform(
                get("/api/v1/pairings/applications/mentor/" + mentor2.id().toString())
            )
            .andExpect(status().isOk())
            .andReturn();
        var findByMentorIdApplications = parseApplicationList(findApplicationByMentorIdResult, objectMapper);
        assertEquals(1, findByMentorIdApplications.size());
        assertEquals(mentee1.id(), findByMentorIdApplications.getFirst().menteeId());
    }

    @Test
    void return_conflict_when_mentee_submit_duplicate_applications_to_the_same_mentor() throws Exception {
        mockMvc.perform(
                post("/api/v1/pairings/applications")
                    .param("mentorId", mentor1.id().toString())
                    .param("menteeId", mentee1.id().toString())
            )
            .andExpect(status().isOk());
        mockMvc.perform(
                post("/api/v1/pairings/applications")
                    .param("mentorId", mentor1.id().toString())
                    .param("menteeId", mentee2.id().toString())
            )
            .andExpect(status().isOk());
        mockMvc.perform(
                post("/api/v1/pairings/applications")
                    .param("mentorId", mentor1.id().toString())
                    .param("menteeId", mentee1.id().toString())
            )
            .andExpect(status().isConflict());
    }

    @Test
    void return_bad_request_when_mentor_is_unavailable() throws Exception {
        mockMvc.perform(
                post("/api/v1/pairings/applications")
                    .param("mentorId", mentor3.id().toString())
                    .param("menteeId", mentee1.id().toString())
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    void return_bad_request_when_application_is_approved() throws Exception {
        var applicationResult = mockMvc.perform(
                post("/api/v1/pairings/applications")
                    .param("mentorId", mentor1.id().toString())
                    .param("menteeId", mentee1.id().toString())
            )
            .andExpect(status().isOk())
            .andReturn();
        var application = parseApplication(applicationResult, objectMapper);
        // Approve application.
        mockMvc.perform(
                post("/api/v1/pairings/applications/{applicationId}:approve", application.id().toString())
                    .param("mentorId", mentor1.id().toString())
                    .param("menteeId", mentee1.id().toString())
            )
            .andExpect(status().isOk());
        // Can not submit new applications to this mentor anymore.
        mockMvc.perform(
                post("/api/v1/pairings/applications")
                    .param("mentorId", mentor1.id().toString())
                    .param("menteeId", mentee2.id().toString())
            )
            .andExpect(status().isBadRequest());
    }
}
