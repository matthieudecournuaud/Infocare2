package com.backend.infocare.domain;

import static com.backend.infocare.domain.CompanyTestSamples.*;
import static com.backend.infocare.domain.MaterialTestSamples.*;
import static com.backend.infocare.domain.TicketTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.backend.infocare.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaterialTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Material.class);
        Material material1 = getMaterialSample1();
        Material material2 = new Material();
        assertThat(material1).isNotEqualTo(material2);

        material2.setId(material1.getId());
        assertThat(material1).isEqualTo(material2);

        material2 = getMaterialSample2();
        assertThat(material1).isNotEqualTo(material2);
    }

    @Test
    void companyTest() {
        Material material = getMaterialRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        material.setCompany(companyBack);
        assertThat(material.getCompany()).isEqualTo(companyBack);

        material.company(null);
        assertThat(material.getCompany()).isNull();
    }

    @Test
    void ticketTest() {
        Material material = getMaterialRandomSampleGenerator();
        Ticket ticketBack = getTicketRandomSampleGenerator();

        material.setTicket(ticketBack);
        assertThat(material.getTicket()).isEqualTo(ticketBack);
        assertThat(ticketBack.getMaterial()).isEqualTo(material);

        material.ticket(null);
        assertThat(material.getTicket()).isNull();
        assertThat(ticketBack.getMaterial()).isNull();
    }
}
