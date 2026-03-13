package com.tomtom.service.mentorapply;

import com.tomtom.service.mentorapply.service.dto.Mentee;
import com.tomtom.service.mentorapply.service.dto.Mentor;
import com.tomtom.service.mentorapply.service.dto.PendingApplicationState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.tomtom.service.mentorapply.utils.ResultMapper.parseApplication;
import static com.tomtom.service.mentorapply.utils.ResultMapper.parseApplicationList;
import static com.tomtom.service.mentorapply.utils.ResultMapper.parseMentor;
import static com.tomtom.service.mentorapply.utils.ResultMapper.parsePairing;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApplicationApprovalTest extends BasePairingControllerTest {
    private final Map<Pair<Mentor, Mentee>, Long> applicationIds = new HashMap<>();

    @Override
    @BeforeEach
    void setup() throws Exception {
        super.setup();
        // Given all mentees applied mentor1, and mentee1 applied mentor2.
        submitApplication(mentor1, mentee1);
        submitApplication(mentor1, mentee2);
        submitApplication(mentor1, mentee3);
        submitApplication(mentor2, mentee1);
    }

    private void submitApplication(Mentor mentor, Mentee mentee) throws Exception {
        var submissionResult = mockMvc.perform(
                post("/api/v1/pairings/applications")
                    .param("mentorId", mentor.id().toString())
                    .param("menteeId", mentee.id().toString())
            )
            .andExpect(status().isOk())
            .andReturn();
        var application = parseApplication(submissionResult, objectMapper);
        applicationIds.put(Pair.of(mentor, mentee), application.id());
    }

    @Test
    void mentor1_approves_mentee1_then_decline_other_pending_applications() throws Exception {
        // Mentor1 approves application from mentee1.
        var applicationId = applicationIds.get(Pair.of(mentor1, mentee1));
        var approveResult = mockMvc.perform(
                post("/api/v1/pairings/applications/{applicationId}:approve", applicationId.toString())
                    .param("mentorId", mentor1.id().toString())
                    .param("menteeId", mentee1.id().toString())
            )
            .andExpect(status().isOk())
            .andReturn();
        var pairing = parsePairing(approveResult, objectMapper);
        assertEquals(mentor1.id(), pairing.mentorId());
        assertEquals(mentee1.id(), pairing.menteeId());
        // Assert mentor1's other applications are declined.
        var findApplicationByMentorIdResult = mockMvc.perform(
                get("/api/v1/pairings/applications/mentor/" + mentor1.id().toString())
            )
            .andExpect(status().isOk())
            .andReturn();
        var allApplicationsOfMentor1 = parseApplicationList(findApplicationByMentorIdResult, objectMapper);
        assertEquals(3, allApplicationsOfMentor1.size());
        allApplicationsOfMentor1.forEach(application -> {
            if (Objects.equals(application.id(), applicationId)) {
                assertEquals(applicationId, application.id());
                assertEquals(mentee1.id(), application.menteeId());
                assertEquals(PendingApplicationState.APPROVED, application.state());
            } else {
                assertEquals(PendingApplicationState.DECLINED, application.state());
            }
        });
        // Assert mentee1's other applications are declined.'
        var findApplicationByMenteeIdResult = mockMvc.perform(
                get("/api/v1/pairings/applications/mentee/" + mentee1.id().toString())
            )
            .andExpect(status().isOk())
            .andReturn();
        var allApplicationsOfMentee1 = parseApplicationList(findApplicationByMenteeIdResult, objectMapper);
        assertEquals(2, allApplicationsOfMentee1.size());
        allApplicationsOfMentor1.forEach(application -> {
            if (Objects.equals(application.id(), applicationId)) {
                assertEquals(PendingApplicationState.APPROVED, application.state());
            } else {
                assertEquals(PendingApplicationState.DECLINED, application.state());
            }
        });
    }

    @Test
    void mentor2_approves_mentee1_then_mentor1_can_not_approve_again() throws Exception {
        // Mentor2 approves application from mentee1.
        var applicationId = applicationIds.get(Pair.of(mentor2, mentee1));
        var approveResult = mockMvc.perform(
                post("/api/v1/pairings/applications/{applicationId}:approve", applicationId.toString())
                    .param("mentorId", mentor2.id().toString())
                    .param("menteeId", mentee1.id().toString())
            )
            .andExpect(status().isOk())
            .andReturn();
        var pairing = parsePairing(approveResult, objectMapper);
        assertEquals(mentor2.id(), pairing.mentorId());
        assertEquals(mentee1.id(), pairing.menteeId());
        // Assert mentor1's applications are updated as well.
        var findApplicationByMentorIdResult = mockMvc.perform(
                get("/api/v1/pairings/applications/mentor/" + mentor1.id().toString())
            )
            .andExpect(status().isOk())
            .andReturn();
        var allApplicationsOfMentor1 = parseApplicationList(findApplicationByMentorIdResult, objectMapper);
        assertEquals(3, allApplicationsOfMentor1.size());
        allApplicationsOfMentor1.forEach(application -> {
            if (Objects.equals(application.menteeId(), mentee1.id())) {
                assertEquals(PendingApplicationState.DECLINED, application.state());
            } else {
                assertEquals(PendingApplicationState.WAITING_APPROVAL, application.state());
            }
        });
        // Return bad request when mentor1 tries to approve mentee1 again.
        mockMvc.perform(
                post("/api/v1/pairings/applications/{applicationId}:approve", applicationId.toString())
                    .param("mentorId", mentor1.id().toString())
                    .param("menteeId", mentee1.id().toString())
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    void paired_mentee_can_not_submit_new_application() throws Exception {
        // Mentor1 approves application from mentee2.
        var applicationId = applicationIds.get(Pair.of(mentor1, mentee2));
        mockMvc.perform(
                post("/api/v1/pairings/applications/{applicationId}:approve", applicationId.toString())
                    .param("mentorId", mentor1.id().toString())
                    .param("menteeId", mentee2.id().toString())
            )
            .andExpect(status().isOk());
        // Return bad request when mentee2 submits a new application.
        mockMvc.perform(
                post("/api/v1/pairings/applications")
                    .param("mentorId", mentor2.id().toString())
                    .param("menteeId", mentee2.id().toString())
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    void update_mentor_mentee_paired_state_after_approving_application() throws Exception {
        // Mentor1 approves application from mentee3.
        var applicationId = applicationIds.get(Pair.of(mentor1, mentee3));
        var approvalResult = mockMvc.perform(
                post("/api/v1/pairings/applications/{applicationId}:approve", applicationId.toString())
                    .param("mentorId", mentor1.id().toString())
                    .param("menteeId", mentee3.id().toString())
            )
            .andExpect(status().isOk())
            .andReturn();
        var pairing = parsePairing(approvalResult, objectMapper);
        // Assert mentor1's pairing state is updated.
        var findMentor1Result = mockMvc.perform(get("/api/v1/mentor/" + mentor1.id()))
            .andExpect(status().isOk())
            .andReturn();
        var mentor1WithPairState = parseMentor(findMentor1Result, objectMapper);
        assertEquals(pairing.id(), mentor1WithPairState.pairingId());
        // Assert mentee3's pairing state is updated.
        var findMentee3Result = mockMvc.perform(get("/api/v1/mentee/" + mentee3.id()))
            .andExpect(status().isOk())
            .andReturn();
        var mentee3WithPairState = parseMentor(findMentee3Result, objectMapper);
        assertEquals(pairing.id(), mentee3WithPairState.pairingId());
    }

    @Test
    void return_bad_request_when_mentor_approves_multiple_applications() throws Exception {
        mockMvc.perform(
                post(
                    "/api/v1/pairings/applications/{applicationId}:approve",
                    applicationIds.get(Pair.of(mentor1, mentee1)).toString()
                )
                    .param("mentorId", mentor1.id().toString())
                    .param("menteeId", mentee1.id().toString())
            )
            .andExpect(status().isOk());
        mockMvc.perform(
                post(
                    "/api/v1/pairings/applications/{applicationId}:approve",
                    applicationIds.get(Pair.of(mentor1, mentee3)).toString()
                )
                    .param("mentorId", mentor1.id().toString())
                    .param("menteeId", mentee3.id().toString())
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    void return_bad_request_when_application_mismatch() throws Exception {
        mockMvc.perform(
                post(
                    "/api/v1/pairings/applications/{applicationId}:approve",
                    applicationIds.get(Pair.of(mentor1, mentee1)).toString()
                )
                    .param("mentorId", mentor2.id().toString())
                    .param("menteeId", mentee1.id().toString())
            )
            .andExpect(status().isNotFound());
    }
}
