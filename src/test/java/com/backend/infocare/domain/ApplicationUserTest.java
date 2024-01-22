package com.backend.infocare.domain;

import static com.backend.infocare.domain.ApplicationUserTestSamples.*;
import static com.backend.infocare.domain.TicketTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.backend.infocare.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ApplicationUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationUser.class);
        ApplicationUser applicationUser1 = getApplicationUserSample1();
        ApplicationUser applicationUser2 = new ApplicationUser();
        assertThat(applicationUser1).isNotEqualTo(applicationUser2);

        applicationUser2.setId(applicationUser1.getId());
        assertThat(applicationUser1).isEqualTo(applicationUser2);

        applicationUser2 = getApplicationUserSample2();
        assertThat(applicationUser1).isNotEqualTo(applicationUser2);
    }

    @Test
    void ticketTest() throws Exception {
        ApplicationUser applicationUser = getApplicationUserRandomSampleGenerator();
        Ticket ticketBack = getTicketRandomSampleGenerator();

        applicationUser.addTicket(ticketBack);
        assertThat(applicationUser.getTickets()).containsOnly(ticketBack);

        applicationUser.removeTicket(ticketBack);
        assertThat(applicationUser.getTickets()).doesNotContain(ticketBack);

        applicationUser.tickets(new HashSet<>(Set.of(ticketBack)));
        assertThat(applicationUser.getTickets()).containsOnly(ticketBack);

        applicationUser.setTickets(new HashSet<>());
        assertThat(applicationUser.getTickets()).doesNotContain(ticketBack);
    }
}
