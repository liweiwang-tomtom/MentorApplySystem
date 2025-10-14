package com.tomtom.service.mentorapply.repository;

import com.tomtom.service.mentorapply.entity.MentorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentorRepository extends JpaRepository<MentorEntity, Long> {

    @NonNull
    @Query("SELECT m FROM MentorEntity m WHERE m.location = :location")
    List<MentorEntity> findByLocation(@NonNull String location);
}
