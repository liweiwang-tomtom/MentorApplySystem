package com.tomtom.service.mentorapply.repository;

import com.tomtom.service.mentorapply.repository.entity.PendingApplicationEntity;
import com.tomtom.service.mentorapply.service.dto.PendingApplicationState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PendingApplicationRepository extends JpaRepository<PendingApplicationEntity, Long> {
    @Query("SELECT p FROM PendingApplicationEntity p WHERE p.mentor.id = :mentorId")
    List<PendingApplicationEntity> findByMentorId(@Param("mentorId") long id);

    @Query("SELECT p FROM PendingApplicationEntity p WHERE p.mentee.id = :menteeId")
    List<PendingApplicationEntity> findByMenteeId(@Param("menteeId") long id);

    @Query("SELECT p FROM PendingApplicationEntity p WHERE p.mentor.id = :mentorId AND p.mentee.id = :menteeId AND p.state = :state")
    List<PendingApplicationEntity> findByMentorAndMenteeIdWithState(
        @Param("mentorId") long mentorId,
        @Param("menteeId") long menteeId,
        @Param("state") PendingApplicationState state);

    @Query("SELECT p FROM PendingApplicationEntity p WHERE p.mentor.id = :mentorId AND p.mentee.id = :menteeId AND p.state != :state")
    List<PendingApplicationEntity> findByMentorAndMenteeIdWithoutState(
        @Param("mentorId") long mentorId,
        @Param("menteeId") long menteeId,
        @Param("state") PendingApplicationState state);

    @Modifying
    @Transactional
    @Query("UPDATE PendingApplicationEntity p SET p.state = :state WHERE p.id = :id")
    int updateApplicationState(@Param("id") long id, @Param("state") PendingApplicationState state);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE PendingApplicationEntity p SET p.state = 'DECLINED' WHERE p.mentor.id = :mentorId AND p.id <> :approvedId AND p.state = 'WAITING_APPROVAL'")
    void declineMentorOtherApplications(@Param("mentorId") long mentorId, @Param("approvedId") long approvedId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE PendingApplicationEntity p SET p.state = 'DECLINED' WHERE p.mentee.id = :menteeId AND p.id <> :approvedId AND p.state = 'WAITING_APPROVAL'")
    void declineMenteeOtherApplications(@Param("menteeId") long menteeId, @Param("approvedId") long approvedId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE PendingApplicationEntity p SET p.state = 'APPROVED' WHERE p.id = :id AND p.mentor.id = :mentorId AND p.mentee.id = :menteeId AND p.state = 'WAITING_APPROVAL'")
    int approveApplicationWithMentorMenteeId(@Param("mentorId") long mentorId, @Param("menteeId") long menteeId, @Param("id") long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM PendingApplicationEntity p WHERE p.applyDate < :limit")
    void deleteOutdatedApplication(@Param("limit") LocalDate limit);
}
