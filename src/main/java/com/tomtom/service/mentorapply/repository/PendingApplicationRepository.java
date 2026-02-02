package com.tomtom.service.mentorapply.repository;

import com.tomtom.service.mentorapply.repository.entity.PendingApplicationEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingApplicationRepository extends JpaRepository<PendingApplicationEntity, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM PairingEntity m WHERE m.id = :id")
    void deleteById(@Param("id") long id);
}
