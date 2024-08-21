package com.backend.infocare.repository;

import com.backend.infocare.domain.Ticket;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT count(t) FROM Ticket t JOIN t.applicationUsers u WHERE u.id = :userId AND t.status.statusCode = 'RESOLVED'")
    Long countResolvedTicketsByUserId(@Param("userId") Long userId);

    @Query("SELECT t.priority.name, COUNT(t) FROM Ticket t JOIN t.applicationUsers u WHERE u.id = :userId GROUP BY t.priority.name")
    List<Object[]> countTicketsByPriorityAndUserId(@Param("userId") Long userId);

    @Query("SELECT count(t) FROM Ticket t JOIN t.applicationUsers u WHERE u.id = :userId")
    Long countByApplicationUsers_UserId(@Param("userId") Long userId);

    @Modifying
    @Query(value = "CALL PurgeOldTickets();", nativeQuery = true)
    void purgeOldTickets();

    List<Ticket> findTop4ByApplicationUsers_UserIdOrderByCreatedAtDesc(Long applicationUserId);
}
