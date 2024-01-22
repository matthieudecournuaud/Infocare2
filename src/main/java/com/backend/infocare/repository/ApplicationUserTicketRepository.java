package com.backend.infocare.repository;

import com.backend.infocare.domain.ApplicationUserTicket;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ApplicationUserTicket entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationUserTicketRepository extends JpaRepository<ApplicationUserTicket, Long> {}
