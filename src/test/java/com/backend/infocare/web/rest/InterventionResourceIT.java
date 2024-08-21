package com.backend.infocare.web.rest;

import static com.backend.infocare.domain.InterventionAsserts.*;
import static com.backend.infocare.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.backend.infocare.IntegrationTest;
import com.backend.infocare.domain.Intervention;
import com.backend.infocare.repository.InterventionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
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
    private ObjectMapper om;

    @Autowired
    private InterventionRepository interventionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInterventionMockMvc;

    private Intervention intervention;

    private Intervention insertedIntervention;

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

    @AfterEach
    public void cleanup() {
        if (insertedIntervention != null) {
            interventionRepository.delete(insertedIntervention);
            insertedIntervention = null;
        }
    }

    @Test
    @Transactional
    void createIntervention() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Intervention
        var returnedIntervention = om.readValue(
            restInterventionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(intervention)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Intervention.class
        );

        // Validate the Intervention in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInterventionUpdatableFieldsEquals(returnedIntervention, getPersistedIntervention(returnedIntervention));

        insertedIntervention = returnedIntervention;
    }

    @Test
    @Transactional
    void createInterventionWithExistingId() throws Exception {
        // Create the Intervention with an existing ID
        intervention.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInterventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(intervention)))
            .andExpect(status().isBadRequest());

        // Validate the Intervention in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        intervention.setCreatedBy(null);

        // Create the Intervention, which fails.

        restInterventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(intervention)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        intervention.setCreatedAt(null);

        // Create the Intervention, which fails.

        restInterventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(intervention)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInterventions() throws Exception {
        // Initialize the database
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

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
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

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
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

        long databaseSizeBeforeUpdate = getRepositoryCount();

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
                    .content(om.writeValueAsBytes(updatedIntervention))
            )
            .andExpect(status().isOk());

        // Validate the Intervention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInterventionToMatchAllProperties(updatedIntervention);
    }

    @Test
    @Transactional
    void putNonExistingIntervention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        intervention.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, intervention.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(intervention))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIntervention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        intervention.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(intervention))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIntervention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        intervention.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterventionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(intervention)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Intervention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInterventionWithPatch() throws Exception {
        // Initialize the database
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the intervention using partial update
        Intervention partialUpdatedIntervention = new Intervention();
        partialUpdatedIntervention.setId(intervention.getId());

        partialUpdatedIntervention.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).createdAt(UPDATED_CREATED_AT).notes(UPDATED_NOTES);

        restInterventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntervention.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIntervention))
            )
            .andExpect(status().isOk());

        // Validate the Intervention in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInterventionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIntervention, intervention),
            getPersistedIntervention(intervention)
        );
    }

    @Test
    @Transactional
    void fullUpdateInterventionWithPatch() throws Exception {
        // Initialize the database
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

        long databaseSizeBeforeUpdate = getRepositoryCount();

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
                    .content(om.writeValueAsBytes(partialUpdatedIntervention))
            )
            .andExpect(status().isOk());

        // Validate the Intervention in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInterventionUpdatableFieldsEquals(partialUpdatedIntervention, getPersistedIntervention(partialUpdatedIntervention));
    }

    @Test
    @Transactional
    void patchNonExistingIntervention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        intervention.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, intervention.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(intervention))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIntervention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        intervention.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(intervention))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIntervention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        intervention.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterventionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(intervention)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Intervention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIntervention() throws Exception {
        // Initialize the database
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the intervention
        restInterventionMockMvc
            .perform(delete(ENTITY_API_URL_ID, intervention.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return interventionRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Intervention getPersistedIntervention(Intervention intervention) {
        return interventionRepository.findById(intervention.getId()).orElseThrow();
    }

    protected void assertPersistedInterventionToMatchAllProperties(Intervention expectedIntervention) {
        assertInterventionAllPropertiesEquals(expectedIntervention, getPersistedIntervention(expectedIntervention));
    }

    protected void assertPersistedInterventionToMatchUpdatableProperties(Intervention expectedIntervention) {
        assertInterventionAllUpdatablePropertiesEquals(expectedIntervention, getPersistedIntervention(expectedIntervention));
    }
}
