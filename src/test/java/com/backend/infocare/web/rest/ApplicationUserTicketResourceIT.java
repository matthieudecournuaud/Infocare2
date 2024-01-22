package com.backend.infocare.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.backend.infocare.IntegrationTest;
import com.backend.infocare.domain.ApplicationUserTicket;
import com.backend.infocare.repository.ApplicationUserTicketRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ApplicationUserTicketResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApplicationUserTicketResourceIT {

    private static final String ENTITY_API_URL = "/api/application-user-tickets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApplicationUserTicketRepository applicationUserTicketRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicationUserTicketMockMvc;

    private ApplicationUserTicket applicationUserTicket;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationUserTicket createEntity(EntityManager em) {
        ApplicationUserTicket applicationUserTicket = new ApplicationUserTicket();
        return applicationUserTicket;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApplicationUserTicket createUpdatedEntity(EntityManager em) {
        ApplicationUserTicket applicationUserTicket = new ApplicationUserTicket();
        return applicationUserTicket;
    }

    @BeforeEach
    public void initTest() {
        applicationUserTicket = createEntity(em);
    }

    @Test
    @Transactional
    void createApplicationUserTicket() throws Exception {
        int databaseSizeBeforeCreate = applicationUserTicketRepository.findAll().size();
        // Create the ApplicationUserTicket
        restApplicationUserTicketMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationUserTicket))
            )
            .andExpect(status().isCreated());

        // Validate the ApplicationUserTicket in the database
        List<ApplicationUserTicket> applicationUserTicketList = applicationUserTicketRepository.findAll();
        assertThat(applicationUserTicketList).hasSize(databaseSizeBeforeCreate + 1);
        ApplicationUserTicket testApplicationUserTicket = applicationUserTicketList.get(applicationUserTicketList.size() - 1);
    }

    @Test
    @Transactional
    void createApplicationUserTicketWithExistingId() throws Exception {
        // Create the ApplicationUserTicket with an existing ID
        applicationUserTicket.setId(1L);

        int databaseSizeBeforeCreate = applicationUserTicketRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationUserTicketMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationUserTicket))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUserTicket in the database
        List<ApplicationUserTicket> applicationUserTicketList = applicationUserTicketRepository.findAll();
        assertThat(applicationUserTicketList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllApplicationUserTickets() throws Exception {
        // Initialize the database
        applicationUserTicketRepository.saveAndFlush(applicationUserTicket);

        // Get all the applicationUserTicketList
        restApplicationUserTicketMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationUserTicket.getId().intValue())));
    }

    @Test
    @Transactional
    void getApplicationUserTicket() throws Exception {
        // Initialize the database
        applicationUserTicketRepository.saveAndFlush(applicationUserTicket);

        // Get the applicationUserTicket
        restApplicationUserTicketMockMvc
            .perform(get(ENTITY_API_URL_ID, applicationUserTicket.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(applicationUserTicket.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingApplicationUserTicket() throws Exception {
        // Get the applicationUserTicket
        restApplicationUserTicketMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingApplicationUserTicket() throws Exception {
        // Initialize the database
        applicationUserTicketRepository.saveAndFlush(applicationUserTicket);

        int databaseSizeBeforeUpdate = applicationUserTicketRepository.findAll().size();

        // Update the applicationUserTicket
        ApplicationUserTicket updatedApplicationUserTicket = applicationUserTicketRepository
            .findById(applicationUserTicket.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedApplicationUserTicket are not directly saved in db
        em.detach(updatedApplicationUserTicket);

        restApplicationUserTicketMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApplicationUserTicket.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApplicationUserTicket))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationUserTicket in the database
        List<ApplicationUserTicket> applicationUserTicketList = applicationUserTicketRepository.findAll();
        assertThat(applicationUserTicketList).hasSize(databaseSizeBeforeUpdate);
        ApplicationUserTicket testApplicationUserTicket = applicationUserTicketList.get(applicationUserTicketList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingApplicationUserTicket() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserTicketRepository.findAll().size();
        applicationUserTicket.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationUserTicketMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicationUserTicket.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationUserTicket))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUserTicket in the database
        List<ApplicationUserTicket> applicationUserTicketList = applicationUserTicketRepository.findAll();
        assertThat(applicationUserTicketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApplicationUserTicket() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserTicketRepository.findAll().size();
        applicationUserTicket.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserTicketMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationUserTicket))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUserTicket in the database
        List<ApplicationUserTicket> applicationUserTicketList = applicationUserTicketRepository.findAll();
        assertThat(applicationUserTicketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApplicationUserTicket() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserTicketRepository.findAll().size();
        applicationUserTicket.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserTicketMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicationUserTicket))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationUserTicket in the database
        List<ApplicationUserTicket> applicationUserTicketList = applicationUserTicketRepository.findAll();
        assertThat(applicationUserTicketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApplicationUserTicketWithPatch() throws Exception {
        // Initialize the database
        applicationUserTicketRepository.saveAndFlush(applicationUserTicket);

        int databaseSizeBeforeUpdate = applicationUserTicketRepository.findAll().size();

        // Update the applicationUserTicket using partial update
        ApplicationUserTicket partialUpdatedApplicationUserTicket = new ApplicationUserTicket();
        partialUpdatedApplicationUserTicket.setId(applicationUserTicket.getId());

        restApplicationUserTicketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationUserTicket.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplicationUserTicket))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationUserTicket in the database
        List<ApplicationUserTicket> applicationUserTicketList = applicationUserTicketRepository.findAll();
        assertThat(applicationUserTicketList).hasSize(databaseSizeBeforeUpdate);
        ApplicationUserTicket testApplicationUserTicket = applicationUserTicketList.get(applicationUserTicketList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateApplicationUserTicketWithPatch() throws Exception {
        // Initialize the database
        applicationUserTicketRepository.saveAndFlush(applicationUserTicket);

        int databaseSizeBeforeUpdate = applicationUserTicketRepository.findAll().size();

        // Update the applicationUserTicket using partial update
        ApplicationUserTicket partialUpdatedApplicationUserTicket = new ApplicationUserTicket();
        partialUpdatedApplicationUserTicket.setId(applicationUserTicket.getId());

        restApplicationUserTicketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicationUserTicket.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplicationUserTicket))
            )
            .andExpect(status().isOk());

        // Validate the ApplicationUserTicket in the database
        List<ApplicationUserTicket> applicationUserTicketList = applicationUserTicketRepository.findAll();
        assertThat(applicationUserTicketList).hasSize(databaseSizeBeforeUpdate);
        ApplicationUserTicket testApplicationUserTicket = applicationUserTicketList.get(applicationUserTicketList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingApplicationUserTicket() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserTicketRepository.findAll().size();
        applicationUserTicket.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicationUserTicketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, applicationUserTicket.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationUserTicket))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUserTicket in the database
        List<ApplicationUserTicket> applicationUserTicketList = applicationUserTicketRepository.findAll();
        assertThat(applicationUserTicketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApplicationUserTicket() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserTicketRepository.findAll().size();
        applicationUserTicket.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserTicketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationUserTicket))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApplicationUserTicket in the database
        List<ApplicationUserTicket> applicationUserTicketList = applicationUserTicketRepository.findAll();
        assertThat(applicationUserTicketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApplicationUserTicket() throws Exception {
        int databaseSizeBeforeUpdate = applicationUserTicketRepository.findAll().size();
        applicationUserTicket.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicationUserTicketMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicationUserTicket))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApplicationUserTicket in the database
        List<ApplicationUserTicket> applicationUserTicketList = applicationUserTicketRepository.findAll();
        assertThat(applicationUserTicketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApplicationUserTicket() throws Exception {
        // Initialize the database
        applicationUserTicketRepository.saveAndFlush(applicationUserTicket);

        int databaseSizeBeforeDelete = applicationUserTicketRepository.findAll().size();

        // Delete the applicationUserTicket
        restApplicationUserTicketMockMvc
            .perform(delete(ENTITY_API_URL_ID, applicationUserTicket.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApplicationUserTicket> applicationUserTicketList = applicationUserTicketRepository.findAll();
        assertThat(applicationUserTicketList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
