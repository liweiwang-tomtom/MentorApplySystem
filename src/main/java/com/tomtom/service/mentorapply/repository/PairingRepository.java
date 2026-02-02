package com.tomtom.service.mentorapply.repository;

import com.tomtom.service.mentorapply.repository.entity.MenteeEntity;
import com.tomtom.service.mentorapply.repository.entity.PairingEntity;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PairingRepository extends JpaRepository<PairingEntity, Long> {
    @Query("SELECT m FROM PairingEntity m WHERE m.id = :id")
    @Nullable PairingEntity findById(@Param("id") long id);

    @Query("SELECT m FROM PairingEntity m WHERE m.mentor_id = :mentorId")
    @Nullable PairingEntity findByMentorId(@Param("mentorId") long id);

    @Query("SELECT m FROM PairingEntity m WHERE m.menteeId = :menteeId")
    @Nullable PairingEntity findByMenteeId(@Param("menteeId") long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM PairingEntity m WHERE m.id = :id")
    void deleteById(@Param("id") long id);
}
