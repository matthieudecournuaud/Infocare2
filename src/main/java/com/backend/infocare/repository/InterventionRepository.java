package com.backend.infocare.repository;

import com.backend.infocare.domain.Intervention;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Intervention entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InterventionRepository extends JpaRepository<Intervention, Long> {}
