package com.backend.infocare.domain;

import static com.backend.infocare.domain.ApplicationUserTicketTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.backend.infocare.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApplicationUserTicketTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationUserTicket.class);
        ApplicationUserTicket applicationUserTicket1 = getApplicationUserTicketSample1();
        ApplicationUserTicket applicationUserTicket2 = new ApplicationUserTicket();
        assertThat(applicationUserTicket1).isNotEqualTo(applicationUserTicket2);

        applicationUserTicket2.setId(applicationUserTicket1.getId());
        assertThat(applicationUserTicket1).isEqualTo(applicationUserTicket2);

        applicationUserTicket2 = getApplicationUserTicketSample2();
        assertThat(applicationUserTicket1).isNotEqualTo(applicationUserTicket2);
    }
}
