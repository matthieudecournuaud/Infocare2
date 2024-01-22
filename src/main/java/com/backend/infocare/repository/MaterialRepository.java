package com.backend.infocare.repository;

import com.backend.infocare.domain.Material;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Material entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {}
