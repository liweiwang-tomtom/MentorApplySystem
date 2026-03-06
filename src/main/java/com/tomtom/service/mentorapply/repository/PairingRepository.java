package com.tomtom.service.mentorapply.repository;

import com.tomtom.service.mentorapply.repository.entity.PairingEntity;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Repository
public interface PairingRepository extends JpaRepository<PairingEntity, Long> {
    @Query("SELECT p FROM PairingEntity p WHERE p.mentor.id = :mentorId")
    @Nullable
    PairingEntity findByMentorId(@Param("mentorId") long mentorId);

    @Query("SELECT p FROM PairingEntity p WHERE p.mentee.id = :menteeId")
    @Nullable
    PairingEntity findByMenteeId(@Param("menteeId") long menteeId);

    @Modifying
    @Transactional
    @Query("DELETE FROM PairingEntity p WHERE p.endDate < :limit")
    void deleteOutdatedPairing(@Param("limit") LocalDate limit);
}
