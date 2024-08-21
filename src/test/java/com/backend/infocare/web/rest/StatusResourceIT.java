package com.backend.infocare.web.rest;

import static com.backend.infocare.domain.StatusAsserts.*;
import static com.backend.infocare.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.backend.infocare.IntegrationTest;
import com.backend.infocare.domain.Status;
import com.backend.infocare.repository.StatusRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link StatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StatusResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_STATUS_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR_CODE = "AAAAAAA";
    private static final String UPDATED_COLOR_CODE = "BBBBBBB";

    private static final String DEFAULT_NEXT_POSSIBLE_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_NEXT_POSSIBLE_STATUS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_FINAL = false;
    private static final Boolean UPDATED_IS_FINAL = true;

    private static final String ENTITY_API_URL = "/api/statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStatusMockMvc;

    private Status status;

    private Status insertedStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Status createEntity(EntityManager em) {
        Status status = new Status()
            .name(DEFAULT_NAME)
            .statusCode(DEFAULT_STATUS_CODE)
            .description(DEFAULT_DESCRIPTION)
            .colorCode(DEFAULT_COLOR_CODE)
            .nextPossibleStatus(DEFAULT_NEXT_POSSIBLE_STATUS)
            .isFinal(DEFAULT_IS_FINAL);
        return status;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Status createUpdatedEntity(EntityManager em) {
        Status status = new Status()
            .name(UPDATED_NAME)
            .statusCode(UPDATED_STATUS_CODE)
            .description(UPDATED_DESCRIPTION)
            .colorCode(UPDATED_COLOR_CODE)
            .nextPossibleStatus(UPDATED_NEXT_POSSIBLE_STATUS)
            .isFinal(UPDATED_IS_FINAL);
        return status;
    }

    @BeforeEach
    public void initTest() {
        status = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedStatus != null) {
            statusRepository.delete(insertedStatus);
            insertedStatus = null;
        }
    }

    @Test
    @Transactional
    void createStatus() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Status
        var returnedStatus = om.readValue(
            restStatusMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(status)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Status.class
        );

        // Validate the Status in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertStatusUpdatableFieldsEquals(returnedStatus, getPersistedStatus(returnedStatus));

        insertedStatus = returnedStatus;
    }

    @Test
    @Transactional
    void createStatusWithExistingId() throws Exception {
        // Create the Status with an existing ID
        status.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(status)))
            .andExpect(status().isBadRequest());

        // Validate the Status in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        status.setName(null);

        // Create the Status, which fails.

        restStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(status)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        status.setStatusCode(null);

        // Create the Status, which fails.

        restStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(status)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStatuses() throws Exception {
        // Initialize the database
        insertedStatus = statusRepository.saveAndFlush(status);

        // Get all the statusList
        restStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(status.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].statusCode").value(hasItem(DEFAULT_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].colorCode").value(hasItem(DEFAULT_COLOR_CODE)))
            .andExpect(jsonPath("$.[*].nextPossibleStatus").value(hasItem(DEFAULT_NEXT_POSSIBLE_STATUS)))
            .andExpect(jsonPath("$.[*].isFinal").value(hasItem(DEFAULT_IS_FINAL.booleanValue())));
    }

    @Test
    @Transactional
    void getStatus() throws Exception {
        // Initialize the database
        insertedStatus = statusRepository.saveAndFlush(status);

        // Get the status
        restStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, status.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(status.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.statusCode").value(DEFAULT_STATUS_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.colorCode").value(DEFAULT_COLOR_CODE))
            .andExpect(jsonPath("$.nextPossibleStatus").value(DEFAULT_NEXT_POSSIBLE_STATUS))
            .andExpect(jsonPath("$.isFinal").value(DEFAULT_IS_FINAL.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingStatus() throws Exception {
        // Get the status
        restStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStatus() throws Exception {
        // Initialize the database
        insertedStatus = statusRepository.saveAndFlush(status);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the status
        Status updatedStatus = statusRepository.findById(status.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStatus are not directly saved in db
        em.detach(updatedStatus);
        updatedStatus
            .name(UPDATED_NAME)
            .statusCode(UPDATED_STATUS_CODE)
            .description(UPDATED_DESCRIPTION)
            .colorCode(UPDATED_COLOR_CODE)
            .nextPossibleStatus(UPDATED_NEXT_POSSIBLE_STATUS)
            .isFinal(UPDATED_IS_FINAL);

        restStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedStatus))
            )
            .andExpect(status().isOk());

        // Validate the Status in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStatusToMatchAllProperties(updatedStatus);
    }

    @Test
    @Transactional
    void putNonExistingStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        status.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatusMockMvc
            .perform(put(ENTITY_API_URL_ID, status.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(status)))
            .andExpect(status().isBadRequest());

        // Validate the Status in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        status.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(status))
            )
            .andExpect(status().isBadRequest());

        // Validate the Status in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        status.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(status)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Status in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStatusWithPatch() throws Exception {
        // Initialize the database
        insertedStatus = statusRepository.saveAndFlush(status);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the status using partial update
        Status partialUpdatedStatus = new Status();
        partialUpdatedStatus.setId(status.getId());

        partialUpdatedStatus.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStatus))
            )
            .andExpect(status().isOk());

        // Validate the Status in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStatusUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedStatus, status), getPersistedStatus(status));
    }

    @Test
    @Transactional
    void fullUpdateStatusWithPatch() throws Exception {
        // Initialize the database
        insertedStatus = statusRepository.saveAndFlush(status);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the status using partial update
        Status partialUpdatedStatus = new Status();
        partialUpdatedStatus.setId(status.getId());

        partialUpdatedStatus
            .name(UPDATED_NAME)
            .statusCode(UPDATED_STATUS_CODE)
            .description(UPDATED_DESCRIPTION)
            .colorCode(UPDATED_COLOR_CODE)
            .nextPossibleStatus(UPDATED_NEXT_POSSIBLE_STATUS)
            .isFinal(UPDATED_IS_FINAL);

        restStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStatus))
            )
            .andExpect(status().isOk());

        // Validate the Status in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStatusUpdatableFieldsEquals(partialUpdatedStatus, getPersistedStatus(partialUpdatedStatus));
    }

    @Test
    @Transactional
    void patchNonExistingStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        status.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, status.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(status))
            )
            .andExpect(status().isBadRequest());

        // Validate the Status in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        status.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(status))
            )
            .andExpect(status().isBadRequest());

        // Validate the Status in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        status.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(status)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Status in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStatus() throws Exception {
        // Initialize the database
        insertedStatus = statusRepository.saveAndFlush(status);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the status
        restStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, status.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return statusRepository.count();
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

    protected Status getPersistedStatus(Status status) {
        return statusRepository.findById(status.getId()).orElseThrow();
    }

    protected void assertPersistedStatusToMatchAllProperties(Status expectedStatus) {
        assertStatusAllPropertiesEquals(expectedStatus, getPersistedStatus(expectedStatus));
    }

    protected void assertPersistedStatusToMatchUpdatableProperties(Status expectedStatus) {
        assertStatusAllUpdatablePropertiesEquals(expectedStatus, getPersistedStatus(expectedStatus));
    }
}
