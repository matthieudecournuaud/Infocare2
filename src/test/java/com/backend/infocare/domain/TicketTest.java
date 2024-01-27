package com.backend.infocare.domain;

import static com.backend.infocare.domain.ApplicationUserTestSamples.*;
import static com.backend.infocare.domain.CategoryTestSamples.*;
import static com.backend.infocare.domain.CommentTestSamples.*;
import static com.backend.infocare.domain.InterventionTestSamples.*;
import static com.backend.infocare.domain.MaterialTestSamples.*;
import static com.backend.infocare.domain.PriorityTestSamples.*;
import static com.backend.infocare.domain.StatusTestSamples.*;
import static com.backend.infocare.domain.TicketTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.backend.infocare.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TicketTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ticket.class);
        Ticket ticket1 = getTicketSample1();
        Ticket ticket2 = new Ticket();
        assertThat(ticket1).isNotEqualTo(ticket2);

        ticket2.setId(ticket1.getId());
        assertThat(ticket1).isEqualTo(ticket2);

        ticket2 = getTicketSample2();
        assertThat(ticket1).isNotEqualTo(ticket2);
    }

    @Test
    void materialTest() throws Exception {
        Ticket ticket = getTicketRandomSampleGenerator();
        Material materialBack = getMaterialRandomSampleGenerator();

        ticket.addMaterial(materialBack);
        assertThat(ticket.getMaterials()).containsOnly(materialBack);
        assertThat(materialBack.getTicket()).isEqualTo(ticket);

        ticket.removeMaterial(materialBack);
        assertThat(ticket.getMaterials()).doesNotContain(materialBack);
        assertThat(materialBack.getTicket()).isNull();

        ticket.materials(new HashSet<>(Set.of(materialBack)));
        assertThat(ticket.getMaterials()).containsOnly(materialBack);
        assertThat(materialBack.getTicket()).isEqualTo(ticket);

        ticket.setMaterials(new HashSet<>());
        assertThat(ticket.getMaterials()).doesNotContain(materialBack);
        assertThat(materialBack.getTicket()).isNull();
    }

    @Test
    void commentTest() throws Exception {
        Ticket ticket = getTicketRandomSampleGenerator();
        Comment commentBack = getCommentRandomSampleGenerator();

        ticket.addComment(commentBack);
        assertThat(ticket.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getTicket()).isEqualTo(ticket);

        ticket.removeComment(commentBack);
        assertThat(ticket.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getTicket()).isNull();

        ticket.comments(new HashSet<>(Set.of(commentBack)));
        assertThat(ticket.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getTicket()).isEqualTo(ticket);

        ticket.setComments(new HashSet<>());
        assertThat(ticket.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getTicket()).isNull();
    }

    @Test
    void interventionTest() throws Exception {
        Ticket ticket = getTicketRandomSampleGenerator();
        Intervention interventionBack = getInterventionRandomSampleGenerator();

        ticket.addIntervention(interventionBack);
        assertThat(ticket.getInterventions()).containsOnly(interventionBack);
        assertThat(interventionBack.getTicket()).isEqualTo(ticket);

        ticket.removeIntervention(interventionBack);
        assertThat(ticket.getInterventions()).doesNotContain(interventionBack);
        assertThat(interventionBack.getTicket()).isNull();

        ticket.interventions(new HashSet<>(Set.of(interventionBack)));
        assertThat(ticket.getInterventions()).containsOnly(interventionBack);
        assertThat(interventionBack.getTicket()).isEqualTo(ticket);

        ticket.setInterventions(new HashSet<>());
        assertThat(ticket.getInterventions()).doesNotContain(interventionBack);
        assertThat(interventionBack.getTicket()).isNull();
    }

    @Test
    void categoryTest() throws Exception {
        Ticket ticket = getTicketRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        ticket.setCategory(categoryBack);
        assertThat(ticket.getCategory()).isEqualTo(categoryBack);

        ticket.category(null);
        assertThat(ticket.getCategory()).isNull();
    }

    @Test
    void statusTest() throws Exception {
        Ticket ticket = getTicketRandomSampleGenerator();
        Status statusBack = getStatusRandomSampleGenerator();

        ticket.setStatus(statusBack);
        assertThat(ticket.getStatus()).isEqualTo(statusBack);

        ticket.status(null);
        assertThat(ticket.getStatus()).isNull();
    }

    @Test
    void priorityTest() throws Exception {
        Ticket ticket = getTicketRandomSampleGenerator();
        Priority priorityBack = getPriorityRandomSampleGenerator();

        ticket.setPriority(priorityBack);
        assertThat(ticket.getPriority()).isEqualTo(priorityBack);

        ticket.priority(null);
        assertThat(ticket.getPriority()).isNull();
    }

    @Test
    void applicationUserTest() throws Exception {
        Ticket ticket = getTicketRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        ticket.addApplicationUser(applicationUserBack);
        assertThat(ticket.getApplicationUsers()).containsOnly(applicationUserBack);
        assertThat(applicationUserBack.getTickets()).containsOnly(ticket);

        ticket.removeApplicationUser(applicationUserBack);
        assertThat(ticket.getApplicationUsers()).doesNotContain(applicationUserBack);
        assertThat(applicationUserBack.getTickets()).doesNotContain(ticket);

        ticket.applicationUsers(new HashSet<>(Set.of(applicationUserBack)));
        assertThat(ticket.getApplicationUsers()).containsOnly(applicationUserBack);
        assertThat(applicationUserBack.getTickets()).containsOnly(ticket);

        ticket.setApplicationUsers(new HashSet<>());
        assertThat(ticket.getApplicationUsers()).doesNotContain(applicationUserBack);
        assertThat(applicationUserBack.getTickets()).doesNotContain(ticket);
    }
}
