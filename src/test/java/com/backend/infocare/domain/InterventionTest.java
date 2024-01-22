package com.backend.infocare.domain;

import static com.backend.infocare.domain.InterventionTestSamples.*;
import static com.backend.infocare.domain.ProcedureTestSamples.*;
import static com.backend.infocare.domain.TicketTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.backend.infocare.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InterventionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Intervention.class);
        Intervention intervention1 = getInterventionSample1();
        Intervention intervention2 = new Intervention();
        assertThat(intervention1).isNotEqualTo(intervention2);

        intervention2.setId(intervention1.getId());
        assertThat(intervention1).isEqualTo(intervention2);

        intervention2 = getInterventionSample2();
        assertThat(intervention1).isNotEqualTo(intervention2);
    }

    @Test
    void procedureTest() throws Exception {
        Intervention intervention = getInterventionRandomSampleGenerator();
        Procedure procedureBack = getProcedureRandomSampleGenerator();

        intervention.setProcedure(procedureBack);
        assertThat(intervention.getProcedure()).isEqualTo(procedureBack);

        intervention.procedure(null);
        assertThat(intervention.getProcedure()).isNull();
    }

    @Test
    void ticketTest() throws Exception {
        Intervention intervention = getInterventionRandomSampleGenerator();
        Ticket ticketBack = getTicketRandomSampleGenerator();

        intervention.setTicket(ticketBack);
        assertThat(intervention.getTicket()).isEqualTo(ticketBack);

        intervention.ticket(null);
        assertThat(intervention.getTicket()).isNull();
    }
}
