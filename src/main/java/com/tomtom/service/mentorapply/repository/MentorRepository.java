package com.tomtom.service.mentorapply.repository;

import com.tomtom.service.mentorapply.repository.entity.MentorEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentorRepository extends JpaRepository<MentorEntity, Long> {
    @Query("SELECT m FROM MentorEntity m WHERE m.id = :id")
    MentorEntity findById(@Param("id") long id);

    @Query("SELECT m FROM MentorEntity m WHERE m.name = :name")
    List<MentorEntity> findByName(@Param("name") String name);

    @Query("SELECT m FROM MentorEntity m WHERE LOWER(m.location) LIKE LOWER(CONCAT('%', :location, '%'))")
    List<MentorEntity> findByLocation(@Param("location") String location);

    @Modifying
    @Transactional
    @Query("DELETE FROM MentorEntity m WHERE m.id = :id")
    void deleteById(@Param("id") long id);
}
