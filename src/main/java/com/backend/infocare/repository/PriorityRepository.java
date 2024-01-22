package com.backend.infocare.repository;

import com.backend.infocare.domain.Priority;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Priority entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PriorityRepository extends JpaRepository<Priority, Long> {}
