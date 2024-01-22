package com.backend.infocare.repository;

import com.backend.infocare.domain.Procedure;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Procedure entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcedureRepository extends JpaRepository<Procedure, Long> {}
