package com.backend.infocare.repository;

import com.backend.infocare.domain.Ticket;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Ticket entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findTop4ByApplicationUsers_UserIdOrderByCreatedAtDesc(Long applicationUserId);
}
