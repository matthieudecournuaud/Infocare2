package com.backend.infocare.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.backend.infocare.IntegrationTest;
import com.backend.infocare.domain.Intervention;
import com.backend.infocare.repository.InterventionRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link InterventionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InterventionResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ATTACHMENTS = "AAAAAAAAAA";
    private static final String UPDATED_ATTACHMENTS = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/interventions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InterventionRepository interventionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInterventionMockMvc;

    private Intervention intervention;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Intervention createEntity(EntityManager em) {
        Intervention intervention = new Intervention()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .attachments(DEFAULT_ATTACHMENTS)
            .notes(DEFAULT_NOTES);
        return intervention;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Intervention createUpdatedEntity(EntityManager em) {
        Intervention intervention = new Intervention()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .attachments(UPDATED_ATTACHMENTS)
            .notes(UPDATED_NOTES);
        return intervention;
    }

    @BeforeEach
    public void initTest() {
        intervention = createEntity(em);
    }

    @Test
    @Transactional
    void createIntervention() throws Exception {
        int databaseSizeBeforeCreate = interventionRepository.findAll().size();
        // Create the Intervention
        restInterventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(intervention)))
            .andExpect(status().isCreated());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeCreate + 1);
        Intervention testIntervention = interventionList.get(interventionList.size() - 1);
        assertThat(testIntervention.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testIntervention.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testIntervention.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testIntervention.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testIntervention.getAttachments()).isEqualTo(DEFAULT_ATTACHMENTS);
        assertThat(testIntervention.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void createInterventionWithExistingId() throws Exception {
        // Create the Intervention with an existing ID
        intervention.setId(1L);

        int databaseSizeBeforeCreate = interventionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInterventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(intervention)))
            .andExpect(status().isBadRequest());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = interventionRepository.findAll().size();
        // set the field null
        intervention.setCreatedBy(null);

        // Create the Intervention, which fails.

        restInterventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(intervention)))
            .andExpect(status().isBadRequest());

        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = interventionRepository.findAll().size();
        // set the field null
        intervention.setCreatedAt(null);

        // Create the Intervention, which fails.

        restInterventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(intervention)))
            .andExpect(status().isBadRequest());

        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInterventions() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList
        restInterventionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(intervention.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].attachments").value(hasItem(DEFAULT_ATTACHMENTS)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @Test
    @Transactional
    void getIntervention() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get the intervention
        restInterventionMockMvc
            .perform(get(ENTITY_API_URL_ID, intervention.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(intervention.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.attachments").value(DEFAULT_ATTACHMENTS))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getNonExistingIntervention() throws Exception {
        // Get the intervention
        restInterventionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIntervention() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();

        // Update the intervention
        Intervention updatedIntervention = interventionRepository.findById(intervention.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIntervention are not directly saved in db
        em.detach(updatedIntervention);
        updatedIntervention
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .attachments(UPDATED_ATTACHMENTS)
            .notes(UPDATED_NOTES);

        restInterventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIntervention.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedIntervention))
            )
            .andExpect(status().isOk());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate);
        Intervention testIntervention = interventionList.get(interventionList.size() - 1);
        assertThat(testIntervention.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testIntervention.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testIntervention.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testIntervention.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testIntervention.getAttachments()).isEqualTo(UPDATED_ATTACHMENTS);
        assertThat(testIntervention.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void putNonExistingIntervention() throws Exception {
        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();
        intervention.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, intervention.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intervention))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIntervention() throws Exception {
        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();
        intervention.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intervention))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIntervention() throws Exception {
        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();
        intervention.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterventionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(intervention)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInterventionWithPatch() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();

        // Update the intervention using partial update
        Intervention partialUpdatedIntervention = new Intervention();
        partialUpdatedIntervention.setId(intervention.getId());

        partialUpdatedIntervention.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).createdAt(UPDATED_CREATED_AT).notes(UPDATED_NOTES);

        restInterventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntervention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIntervention))
            )
            .andExpect(status().isOk());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate);
        Intervention testIntervention = interventionList.get(interventionList.size() - 1);
        assertThat(testIntervention.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testIntervention.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testIntervention.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testIntervention.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testIntervention.getAttachments()).isEqualTo(DEFAULT_ATTACHMENTS);
        assertThat(testIntervention.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void fullUpdateInterventionWithPatch() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();

        // Update the intervention using partial update
        Intervention partialUpdatedIntervention = new Intervention();
        partialUpdatedIntervention.setId(intervention.getId());

        partialUpdatedIntervention
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .attachments(UPDATED_ATTACHMENTS)
            .notes(UPDATED_NOTES);

        restInterventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntervention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIntervention))
            )
            .andExpect(status().isOk());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate);
        Intervention testIntervention = interventionList.get(interventionList.size() - 1);
        assertThat(testIntervention.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testIntervention.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testIntervention.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testIntervention.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testIntervention.getAttachments()).isEqualTo(UPDATED_ATTACHMENTS);
        assertThat(testIntervention.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void patchNonExistingIntervention() throws Exception {
        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();
        intervention.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, intervention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(intervention))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIntervention() throws Exception {
        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();
        intervention.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(intervention))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIntervention() throws Exception {
        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();
        intervention.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterventionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(intervention))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIntervention() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        int databaseSizeBeforeDelete = interventionRepository.findAll().size();

        // Delete the intervention
        restInterventionMockMvc
            .perform(delete(ENTITY_API_URL_ID, intervention.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
