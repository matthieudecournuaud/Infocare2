package com.backend.infocare.repository;

import com.backend.infocare.domain.Ticket;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Ticket entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT count(t) FROM Ticket t JOIN t.applicationUsers u WHERE u.id = :userId AND t.status.statusCode = 'RESOLVED'")
    Long countResolvedTicketsByUserId(@Param("userId") Long userId);

    @Query("SELECT count(t) FROM Ticket t JOIN t.applicationUsers u WHERE u.id = :userId")
    Long countByApplicationUsers_UserId(@Param("userId") Long userId);

    List<Ticket> findTop4ByApplicationUsers_UserIdOrderByCreatedAtDesc(Long applicationUserId);
}
