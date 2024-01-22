package com.backend.infocare.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.backend.infocare.IntegrationTest;
import com.backend.infocare.domain.Procedure;
import com.backend.infocare.repository.ProcedureRepository;
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
 * Integration tests for the {@link ProcedureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProcedureResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final Long DEFAULT_PROCEDURE_ID = 1L;
    private static final Long UPDATED_PROCEDURE_ID = 2L;

    private static final String DEFAULT_STEP_BY_STEP_GUIDE = "AAAAAAAAAA";
    private static final String UPDATED_STEP_BY_STEP_GUIDE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ESTIMATED_TIME = 1;
    private static final Integer UPDATED_ESTIMATED_TIME = 2;

    private static final String DEFAULT_REQUIRED_TOOLS = "AAAAAAAAAA";
    private static final String UPDATED_REQUIRED_TOOLS = "BBBBBBBBBB";

    private static final String DEFAULT_SKILLS_REQUIRED = "AAAAAAAAAA";
    private static final String UPDATED_SKILLS_REQUIRED = "BBBBBBBBBB";

    private static final String DEFAULT_SAFETY_INSTRUCTIONS = "AAAAAAAAAA";
    private static final String UPDATED_SAFETY_INSTRUCTIONS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_LAST_REVIEWED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_REVIEWED = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REVIEWED_BY = "AAAAAAAAAA";
    private static final String UPDATED_REVIEWED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_ATTACHMENTS = "AAAAAAAAAA";
    private static final String UPDATED_ATTACHMENTS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/procedures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProcedureRepository procedureRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProcedureMockMvc;

    private Procedure procedure;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Procedure createEntity(EntityManager em) {
        Procedure procedure = new Procedure()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .category(DEFAULT_CATEGORY)
            .procedureId(DEFAULT_PROCEDURE_ID)
            .stepByStepGuide(DEFAULT_STEP_BY_STEP_GUIDE)
            .estimatedTime(DEFAULT_ESTIMATED_TIME)
            .requiredTools(DEFAULT_REQUIRED_TOOLS)
            .skillsRequired(DEFAULT_SKILLS_REQUIRED)
            .safetyInstructions(DEFAULT_SAFETY_INSTRUCTIONS)
            .lastReviewed(DEFAULT_LAST_REVIEWED)
            .reviewedBy(DEFAULT_REVIEWED_BY)
            .attachments(DEFAULT_ATTACHMENTS);
        return procedure;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Procedure createUpdatedEntity(EntityManager em) {
        Procedure procedure = new Procedure()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .category(UPDATED_CATEGORY)
            .procedureId(UPDATED_PROCEDURE_ID)
            .stepByStepGuide(UPDATED_STEP_BY_STEP_GUIDE)
            .estimatedTime(UPDATED_ESTIMATED_TIME)
            .requiredTools(UPDATED_REQUIRED_TOOLS)
            .skillsRequired(UPDATED_SKILLS_REQUIRED)
            .safetyInstructions(UPDATED_SAFETY_INSTRUCTIONS)
            .lastReviewed(UPDATED_LAST_REVIEWED)
            .reviewedBy(UPDATED_REVIEWED_BY)
            .attachments(UPDATED_ATTACHMENTS);
        return procedure;
    }

    @BeforeEach
    public void initTest() {
        procedure = createEntity(em);
    }

    @Test
    @Transactional
    void createProcedure() throws Exception {
        int databaseSizeBeforeCreate = procedureRepository.findAll().size();
        // Create the Procedure
        restProcedureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(procedure)))
            .andExpect(status().isCreated());

        // Validate the Procedure in the database
        List<Procedure> procedureList = procedureRepository.findAll();
        assertThat(procedureList).hasSize(databaseSizeBeforeCreate + 1);
        Procedure testProcedure = procedureList.get(procedureList.size() - 1);
        assertThat(testProcedure.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProcedure.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProcedure.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testProcedure.getProcedureId()).isEqualTo(DEFAULT_PROCEDURE_ID);
        assertThat(testProcedure.getStepByStepGuide()).isEqualTo(DEFAULT_STEP_BY_STEP_GUIDE);
        assertThat(testProcedure.getEstimatedTime()).isEqualTo(DEFAULT_ESTIMATED_TIME);
        assertThat(testProcedure.getRequiredTools()).isEqualTo(DEFAULT_REQUIRED_TOOLS);
        assertThat(testProcedure.getSkillsRequired()).isEqualTo(DEFAULT_SKILLS_REQUIRED);
        assertThat(testProcedure.getSafetyInstructions()).isEqualTo(DEFAULT_SAFETY_INSTRUCTIONS);
        assertThat(testProcedure.getLastReviewed()).isEqualTo(DEFAULT_LAST_REVIEWED);
        assertThat(testProcedure.getReviewedBy()).isEqualTo(DEFAULT_REVIEWED_BY);
        assertThat(testProcedure.getAttachments()).isEqualTo(DEFAULT_ATTACHMENTS);
    }

    @Test
    @Transactional
    void createProcedureWithExistingId() throws Exception {
        // Create the Procedure with an existing ID
        procedure.setId(1L);

        int databaseSizeBeforeCreate = procedureRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcedureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(procedure)))
            .andExpect(status().isBadRequest());

        // Validate the Procedure in the database
        List<Procedure> procedureList = procedureRepository.findAll();
        assertThat(procedureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProcedures() throws Exception {
        // Initialize the database
        procedureRepository.saveAndFlush(procedure);

        // Get all the procedureList
        restProcedureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(procedure.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].procedureId").value(hasItem(DEFAULT_PROCEDURE_ID.intValue())))
            .andExpect(jsonPath("$.[*].stepByStepGuide").value(hasItem(DEFAULT_STEP_BY_STEP_GUIDE)))
            .andExpect(jsonPath("$.[*].estimatedTime").value(hasItem(DEFAULT_ESTIMATED_TIME)))
            .andExpect(jsonPath("$.[*].requiredTools").value(hasItem(DEFAULT_REQUIRED_TOOLS)))
            .andExpect(jsonPath("$.[*].skillsRequired").value(hasItem(DEFAULT_SKILLS_REQUIRED)))
            .andExpect(jsonPath("$.[*].safetyInstructions").value(hasItem(DEFAULT_SAFETY_INSTRUCTIONS)))
            .andExpect(jsonPath("$.[*].lastReviewed").value(hasItem(DEFAULT_LAST_REVIEWED.toString())))
            .andExpect(jsonPath("$.[*].reviewedBy").value(hasItem(DEFAULT_REVIEWED_BY)))
            .andExpect(jsonPath("$.[*].attachments").value(hasItem(DEFAULT_ATTACHMENTS)));
    }

    @Test
    @Transactional
    void getProcedure() throws Exception {
        // Initialize the database
        procedureRepository.saveAndFlush(procedure);

        // Get the procedure
        restProcedureMockMvc
            .perform(get(ENTITY_API_URL_ID, procedure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(procedure.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
            .andExpect(jsonPath("$.procedureId").value(DEFAULT_PROCEDURE_ID.intValue()))
            .andExpect(jsonPath("$.stepByStepGuide").value(DEFAULT_STEP_BY_STEP_GUIDE))
            .andExpect(jsonPath("$.estimatedTime").value(DEFAULT_ESTIMATED_TIME))
            .andExpect(jsonPath("$.requiredTools").value(DEFAULT_REQUIRED_TOOLS))
            .andExpect(jsonPath("$.skillsRequired").value(DEFAULT_SKILLS_REQUIRED))
            .andExpect(jsonPath("$.safetyInstructions").value(DEFAULT_SAFETY_INSTRUCTIONS))
            .andExpect(jsonPath("$.lastReviewed").value(DEFAULT_LAST_REVIEWED.toString()))
            .andExpect(jsonPath("$.reviewedBy").value(DEFAULT_REVIEWED_BY))
            .andExpect(jsonPath("$.attachments").value(DEFAULT_ATTACHMENTS));
    }

    @Test
    @Transactional
    void getNonExistingProcedure() throws Exception {
        // Get the procedure
        restProcedureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProcedure() throws Exception {
        // Initialize the database
        procedureRepository.saveAndFlush(procedure);

        int databaseSizeBeforeUpdate = procedureRepository.findAll().size();

        // Update the procedure
        Procedure updatedProcedure = procedureRepository.findById(procedure.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProcedure are not directly saved in db
        em.detach(updatedProcedure);
        updatedProcedure
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .category(UPDATED_CATEGORY)
            .procedureId(UPDATED_PROCEDURE_ID)
            .stepByStepGuide(UPDATED_STEP_BY_STEP_GUIDE)
            .estimatedTime(UPDATED_ESTIMATED_TIME)
            .requiredTools(UPDATED_REQUIRED_TOOLS)
            .skillsRequired(UPDATED_SKILLS_REQUIRED)
            .safetyInstructions(UPDATED_SAFETY_INSTRUCTIONS)
            .lastReviewed(UPDATED_LAST_REVIEWED)
            .reviewedBy(UPDATED_REVIEWED_BY)
            .attachments(UPDATED_ATTACHMENTS);

        restProcedureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProcedure.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProcedure))
            )
            .andExpect(status().isOk());

        // Validate the Procedure in the database
        List<Procedure> procedureList = procedureRepository.findAll();
        assertThat(procedureList).hasSize(databaseSizeBeforeUpdate);
        Procedure testProcedure = procedureList.get(procedureList.size() - 1);
        assertThat(testProcedure.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProcedure.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProcedure.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testProcedure.getProcedureId()).isEqualTo(UPDATED_PROCEDURE_ID);
        assertThat(testProcedure.getStepByStepGuide()).isEqualTo(UPDATED_STEP_BY_STEP_GUIDE);
        assertThat(testProcedure.getEstimatedTime()).isEqualTo(UPDATED_ESTIMATED_TIME);
        assertThat(testProcedure.getRequiredTools()).isEqualTo(UPDATED_REQUIRED_TOOLS);
        assertThat(testProcedure.getSkillsRequired()).isEqualTo(UPDATED_SKILLS_REQUIRED);
        assertThat(testProcedure.getSafetyInstructions()).isEqualTo(UPDATED_SAFETY_INSTRUCTIONS);
        assertThat(testProcedure.getLastReviewed()).isEqualTo(UPDATED_LAST_REVIEWED);
        assertThat(testProcedure.getReviewedBy()).isEqualTo(UPDATED_REVIEWED_BY);
        assertThat(testProcedure.getAttachments()).isEqualTo(UPDATED_ATTACHMENTS);
    }

    @Test
    @Transactional
    void putNonExistingProcedure() throws Exception {
        int databaseSizeBeforeUpdate = procedureRepository.findAll().size();
        procedure.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcedureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, procedure.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(procedure))
            )
            .andExpect(status().isBadRequest());

        // Validate the Procedure in the database
        List<Procedure> procedureList = procedureRepository.findAll();
        assertThat(procedureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProcedure() throws Exception {
        int databaseSizeBeforeUpdate = procedureRepository.findAll().size();
        procedure.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcedureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(procedure))
            )
            .andExpect(status().isBadRequest());

        // Validate the Procedure in the database
        List<Procedure> procedureList = procedureRepository.findAll();
        assertThat(procedureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProcedure() throws Exception {
        int databaseSizeBeforeUpdate = procedureRepository.findAll().size();
        procedure.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcedureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(procedure)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Procedure in the database
        List<Procedure> procedureList = procedureRepository.findAll();
        assertThat(procedureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProcedureWithPatch() throws Exception {
        // Initialize the database
        procedureRepository.saveAndFlush(procedure);

        int databaseSizeBeforeUpdate = procedureRepository.findAll().size();

        // Update the procedure using partial update
        Procedure partialUpdatedProcedure = new Procedure();
        partialUpdatedProcedure.setId(procedure.getId());

        partialUpdatedProcedure
            .description(UPDATED_DESCRIPTION)
            .category(UPDATED_CATEGORY)
            .stepByStepGuide(UPDATED_STEP_BY_STEP_GUIDE)
            .estimatedTime(UPDATED_ESTIMATED_TIME)
            .safetyInstructions(UPDATED_SAFETY_INSTRUCTIONS)
            .reviewedBy(UPDATED_REVIEWED_BY);

        restProcedureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcedure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcedure))
            )
            .andExpect(status().isOk());

        // Validate the Procedure in the database
        List<Procedure> procedureList = procedureRepository.findAll();
        assertThat(procedureList).hasSize(databaseSizeBeforeUpdate);
        Procedure testProcedure = procedureList.get(procedureList.size() - 1);
        assertThat(testProcedure.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProcedure.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProcedure.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testProcedure.getProcedureId()).isEqualTo(DEFAULT_PROCEDURE_ID);
        assertThat(testProcedure.getStepByStepGuide()).isEqualTo(UPDATED_STEP_BY_STEP_GUIDE);
        assertThat(testProcedure.getEstimatedTime()).isEqualTo(UPDATED_ESTIMATED_TIME);
        assertThat(testProcedure.getRequiredTools()).isEqualTo(DEFAULT_REQUIRED_TOOLS);
        assertThat(testProcedure.getSkillsRequired()).isEqualTo(DEFAULT_SKILLS_REQUIRED);
        assertThat(testProcedure.getSafetyInstructions()).isEqualTo(UPDATED_SAFETY_INSTRUCTIONS);
        assertThat(testProcedure.getLastReviewed()).isEqualTo(DEFAULT_LAST_REVIEWED);
        assertThat(testProcedure.getReviewedBy()).isEqualTo(UPDATED_REVIEWED_BY);
        assertThat(testProcedure.getAttachments()).isEqualTo(DEFAULT_ATTACHMENTS);
    }

    @Test
    @Transactional
    void fullUpdateProcedureWithPatch() throws Exception {
        // Initialize the database
        procedureRepository.saveAndFlush(procedure);

        int databaseSizeBeforeUpdate = procedureRepository.findAll().size();

        // Update the procedure using partial update
        Procedure partialUpdatedProcedure = new Procedure();
        partialUpdatedProcedure.setId(procedure.getId());

        partialUpdatedProcedure
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .category(UPDATED_CATEGORY)
            .procedureId(UPDATED_PROCEDURE_ID)
            .stepByStepGuide(UPDATED_STEP_BY_STEP_GUIDE)
            .estimatedTime(UPDATED_ESTIMATED_TIME)
            .requiredTools(UPDATED_REQUIRED_TOOLS)
            .skillsRequired(UPDATED_SKILLS_REQUIRED)
            .safetyInstructions(UPDATED_SAFETY_INSTRUCTIONS)
            .lastReviewed(UPDATED_LAST_REVIEWED)
            .reviewedBy(UPDATED_REVIEWED_BY)
            .attachments(UPDATED_ATTACHMENTS);

        restProcedureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcedure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcedure))
            )
            .andExpect(status().isOk());

        // Validate the Procedure in the database
        List<Procedure> procedureList = procedureRepository.findAll();
        assertThat(procedureList).hasSize(databaseSizeBeforeUpdate);
        Procedure testProcedure = procedureList.get(procedureList.size() - 1);
        assertThat(testProcedure.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProcedure.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProcedure.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testProcedure.getProcedureId()).isEqualTo(UPDATED_PROCEDURE_ID);
        assertThat(testProcedure.getStepByStepGuide()).isEqualTo(UPDATED_STEP_BY_STEP_GUIDE);
        assertThat(testProcedure.getEstimatedTime()).isEqualTo(UPDATED_ESTIMATED_TIME);
        assertThat(testProcedure.getRequiredTools()).isEqualTo(UPDATED_REQUIRED_TOOLS);
        assertThat(testProcedure.getSkillsRequired()).isEqualTo(UPDATED_SKILLS_REQUIRED);
        assertThat(testProcedure.getSafetyInstructions()).isEqualTo(UPDATED_SAFETY_INSTRUCTIONS);
        assertThat(testProcedure.getLastReviewed()).isEqualTo(UPDATED_LAST_REVIEWED);
        assertThat(testProcedure.getReviewedBy()).isEqualTo(UPDATED_REVIEWED_BY);
        assertThat(testProcedure.getAttachments()).isEqualTo(UPDATED_ATTACHMENTS);
    }

    @Test
    @Transactional
    void patchNonExistingProcedure() throws Exception {
        int databaseSizeBeforeUpdate = procedureRepository.findAll().size();
        procedure.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcedureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, procedure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(procedure))
            )
            .andExpect(status().isBadRequest());

        // Validate the Procedure in the database
        List<Procedure> procedureList = procedureRepository.findAll();
        assertThat(procedureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProcedure() throws Exception {
        int databaseSizeBeforeUpdate = procedureRepository.findAll().size();
        procedure.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcedureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(procedure))
            )
            .andExpect(status().isBadRequest());

        // Validate the Procedure in the database
        List<Procedure> procedureList = procedureRepository.findAll();
        assertThat(procedureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProcedure() throws Exception {
        int databaseSizeBeforeUpdate = procedureRepository.findAll().size();
        procedure.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcedureMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(procedure))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Procedure in the database
        List<Procedure> procedureList = procedureRepository.findAll();
        assertThat(procedureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProcedure() throws Exception {
        // Initialize the database
        procedureRepository.saveAndFlush(procedure);

        int databaseSizeBeforeDelete = procedureRepository.findAll().size();

        // Delete the procedure
        restProcedureMockMvc
            .perform(delete(ENTITY_API_URL_ID, procedure.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Procedure> procedureList = procedureRepository.findAll();
        assertThat(procedureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
