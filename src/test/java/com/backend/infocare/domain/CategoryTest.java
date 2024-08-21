package com.backend.infocare.domain;

import static com.backend.infocare.domain.CategoryTestSamples.*;
import static com.backend.infocare.domain.TicketTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.backend.infocare.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Category.class);
        Category category1 = getCategorySample1();
        Category category2 = new Category();
        assertThat(category1).isNotEqualTo(category2);

        category2.setId(category1.getId());
        assertThat(category1).isEqualTo(category2);

        category2 = getCategorySample2();
        assertThat(category1).isNotEqualTo(category2);
    }

    @Test
    void ticketTest() {
        Category category = getCategoryRandomSampleGenerator();
        Ticket ticketBack = getTicketRandomSampleGenerator();

        category.setTicket(ticketBack);
        assertThat(category.getTicket()).isEqualTo(ticketBack);
        assertThat(ticketBack.getCategory()).isEqualTo(category);

        category.ticket(null);
        assertThat(category.getTicket()).isNull();
        assertThat(ticketBack.getCategory()).isNull();
    }
}
