package com.backend.infocare.domain;

import static com.backend.infocare.domain.ApplicationUserTestSamples.*;
import static com.backend.infocare.domain.TicketTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.backend.infocare.web.rest.TestUtil;
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
    void ticketTest() {
        ApplicationUser applicationUser = getApplicationUserRandomSampleGenerator();
        Ticket ticketBack = getTicketRandomSampleGenerator();

        applicationUser.setTicket(ticketBack);
        assertThat(applicationUser.getTicket()).isEqualTo(ticketBack);
        assertThat(ticketBack.getApplicationUser()).isEqualTo(applicationUser);

        applicationUser.ticket(null);
        assertThat(applicationUser.getTicket()).isNull();
        assertThat(ticketBack.getApplicationUser()).isNull();
    }
}
