package com.tomtom.service.mentorapply.repository;

import com.tomtom.service.mentorapply.entity.MenteeEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenteeRepository extends JpaRepository<MenteeEntity, Long> {
    @Query("SELECT m FROM MenteeEntity m WHERE m.id = :id")
    MenteeEntity findById(@Param("id") long id);

    @Query("SELECT m FROM MenteeEntity m WHERE m.name = :name")
    List<MenteeEntity> findByName(@Param("name") String name);

    @Query("SELECT m FROM MentorEntity m WHERE LOWER(m.location) LIKE LOWER(CONCAT('%', :location, '%'))")
    List<MenteeEntity> findByLocation(@NonNull @Param("location") String location);

    @Modifying
    @Transactional
    @Query("DELETE FROM MenteeEntity m WHERE m.id = :id")
    void deleteById(@Param("id") long id);
}
