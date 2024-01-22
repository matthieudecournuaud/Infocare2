package com.backend.infocare.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.backend.infocare.IntegrationTest;
import com.backend.infocare.domain.Material;
import com.backend.infocare.repository.MaterialRepository;
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
 * Integration tests for the {@link MaterialResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MaterialResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PURCHASE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PURCHASE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_WARRANTY_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_WARRANTY_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_MANUFACTURER = "AAAAAAAAAA";
    private static final String UPDATED_MANUFACTURER = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_MODEL = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS_MATERIAL = "AAAAAAAAAA";
    private static final String UPDATED_STATUS_MATERIAL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_LAST_MAINTENANCE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MAINTENANCE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final Long DEFAULT_SERIAL_NUMBER = 1L;
    private static final Long UPDATED_SERIAL_NUMBER = 2L;

    private static final String ENTITY_API_URL = "/api/materials";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaterialMockMvc;

    private Material material;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Material createEntity(EntityManager em) {
        Material material = new Material()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .purchaseDate(DEFAULT_PURCHASE_DATE)
            .warrantyEndDate(DEFAULT_WARRANTY_END_DATE)
            .manufacturer(DEFAULT_MANUFACTURER)
            .model(DEFAULT_MODEL)
            .statusMaterial(DEFAULT_STATUS_MATERIAL)
            .lastMaintenanceDate(DEFAULT_LAST_MAINTENANCE_DATE)
            .note(DEFAULT_NOTE)
            .serialNumber(DEFAULT_SERIAL_NUMBER);
        return material;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Material createUpdatedEntity(EntityManager em) {
        Material material = new Material()
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .warrantyEndDate(UPDATED_WARRANTY_END_DATE)
            .manufacturer(UPDATED_MANUFACTURER)
            .model(UPDATED_MODEL)
            .statusMaterial(UPDATED_STATUS_MATERIAL)
            .lastMaintenanceDate(UPDATED_LAST_MAINTENANCE_DATE)
            .note(UPDATED_NOTE)
            .serialNumber(UPDATED_SERIAL_NUMBER);
        return material;
    }

    @BeforeEach
    public void initTest() {
        material = createEntity(em);
    }

    @Test
    @Transactional
    void createMaterial() throws Exception {
        int databaseSizeBeforeCreate = materialRepository.findAll().size();
        // Create the Material
        restMaterialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(material)))
            .andExpect(status().isCreated());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeCreate + 1);
        Material testMaterial = materialList.get(materialList.size() - 1);
        assertThat(testMaterial.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMaterial.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testMaterial.getPurchaseDate()).isEqualTo(DEFAULT_PURCHASE_DATE);
        assertThat(testMaterial.getWarrantyEndDate()).isEqualTo(DEFAULT_WARRANTY_END_DATE);
        assertThat(testMaterial.getManufacturer()).isEqualTo(DEFAULT_MANUFACTURER);
        assertThat(testMaterial.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testMaterial.getStatusMaterial()).isEqualTo(DEFAULT_STATUS_MATERIAL);
        assertThat(testMaterial.getLastMaintenanceDate()).isEqualTo(DEFAULT_LAST_MAINTENANCE_DATE);
        assertThat(testMaterial.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testMaterial.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void createMaterialWithExistingId() throws Exception {
        // Create the Material with an existing ID
        material.setId(1L);

        int databaseSizeBeforeCreate = materialRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaterialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(material)))
            .andExpect(status().isBadRequest());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = materialRepository.findAll().size();
        // set the field null
        material.setName(null);

        // Create the Material, which fails.

        restMaterialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(material)))
            .andExpect(status().isBadRequest());

        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = materialRepository.findAll().size();
        // set the field null
        material.setType(null);

        // Create the Material, which fails.

        restMaterialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(material)))
            .andExpect(status().isBadRequest());

        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMaterials() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList
        restMaterialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(material.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(DEFAULT_PURCHASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].warrantyEndDate").value(hasItem(DEFAULT_WARRANTY_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].manufacturer").value(hasItem(DEFAULT_MANUFACTURER)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].statusMaterial").value(hasItem(DEFAULT_STATUS_MATERIAL)))
            .andExpect(jsonPath("$.[*].lastMaintenanceDate").value(hasItem(DEFAULT_LAST_MAINTENANCE_DATE.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER.intValue())));
    }

    @Test
    @Transactional
    void getMaterial() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get the material
        restMaterialMockMvc
            .perform(get(ENTITY_API_URL_ID, material.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(material.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.purchaseDate").value(DEFAULT_PURCHASE_DATE.toString()))
            .andExpect(jsonPath("$.warrantyEndDate").value(DEFAULT_WARRANTY_END_DATE.toString()))
            .andExpect(jsonPath("$.manufacturer").value(DEFAULT_MANUFACTURER))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL))
            .andExpect(jsonPath("$.statusMaterial").value(DEFAULT_STATUS_MATERIAL))
            .andExpect(jsonPath("$.lastMaintenanceDate").value(DEFAULT_LAST_MAINTENANCE_DATE.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingMaterial() throws Exception {
        // Get the material
        restMaterialMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMaterial() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        int databaseSizeBeforeUpdate = materialRepository.findAll().size();

        // Update the material
        Material updatedMaterial = materialRepository.findById(material.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMaterial are not directly saved in db
        em.detach(updatedMaterial);
        updatedMaterial
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .warrantyEndDate(UPDATED_WARRANTY_END_DATE)
            .manufacturer(UPDATED_MANUFACTURER)
            .model(UPDATED_MODEL)
            .statusMaterial(UPDATED_STATUS_MATERIAL)
            .lastMaintenanceDate(UPDATED_LAST_MAINTENANCE_DATE)
            .note(UPDATED_NOTE)
            .serialNumber(UPDATED_SERIAL_NUMBER);

        restMaterialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMaterial.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMaterial))
            )
            .andExpect(status().isOk());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
        Material testMaterial = materialList.get(materialList.size() - 1);
        assertThat(testMaterial.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMaterial.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testMaterial.getPurchaseDate()).isEqualTo(UPDATED_PURCHASE_DATE);
        assertThat(testMaterial.getWarrantyEndDate()).isEqualTo(UPDATED_WARRANTY_END_DATE);
        assertThat(testMaterial.getManufacturer()).isEqualTo(UPDATED_MANUFACTURER);
        assertThat(testMaterial.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testMaterial.getStatusMaterial()).isEqualTo(UPDATED_STATUS_MATERIAL);
        assertThat(testMaterial.getLastMaintenanceDate()).isEqualTo(UPDATED_LAST_MAINTENANCE_DATE);
        assertThat(testMaterial.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testMaterial.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingMaterial() throws Exception {
        int databaseSizeBeforeUpdate = materialRepository.findAll().size();
        material.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, material.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(material))
            )
            .andExpect(status().isBadRequest());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMaterial() throws Exception {
        int databaseSizeBeforeUpdate = materialRepository.findAll().size();
        material.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(material))
            )
            .andExpect(status().isBadRequest());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMaterial() throws Exception {
        int databaseSizeBeforeUpdate = materialRepository.findAll().size();
        material.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(material)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMaterialWithPatch() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        int databaseSizeBeforeUpdate = materialRepository.findAll().size();

        // Update the material using partial update
        Material partialUpdatedMaterial = new Material();
        partialUpdatedMaterial.setId(material.getId());

        partialUpdatedMaterial
            .warrantyEndDate(UPDATED_WARRANTY_END_DATE)
            .lastMaintenanceDate(UPDATED_LAST_MAINTENANCE_DATE)
            .note(UPDATED_NOTE);

        restMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaterial.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMaterial))
            )
            .andExpect(status().isOk());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
        Material testMaterial = materialList.get(materialList.size() - 1);
        assertThat(testMaterial.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMaterial.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testMaterial.getPurchaseDate()).isEqualTo(DEFAULT_PURCHASE_DATE);
        assertThat(testMaterial.getWarrantyEndDate()).isEqualTo(UPDATED_WARRANTY_END_DATE);
        assertThat(testMaterial.getManufacturer()).isEqualTo(DEFAULT_MANUFACTURER);
        assertThat(testMaterial.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testMaterial.getStatusMaterial()).isEqualTo(DEFAULT_STATUS_MATERIAL);
        assertThat(testMaterial.getLastMaintenanceDate()).isEqualTo(UPDATED_LAST_MAINTENANCE_DATE);
        assertThat(testMaterial.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testMaterial.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateMaterialWithPatch() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        int databaseSizeBeforeUpdate = materialRepository.findAll().size();

        // Update the material using partial update
        Material partialUpdatedMaterial = new Material();
        partialUpdatedMaterial.setId(material.getId());

        partialUpdatedMaterial
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .warrantyEndDate(UPDATED_WARRANTY_END_DATE)
            .manufacturer(UPDATED_MANUFACTURER)
            .model(UPDATED_MODEL)
            .statusMaterial(UPDATED_STATUS_MATERIAL)
            .lastMaintenanceDate(UPDATED_LAST_MAINTENANCE_DATE)
            .note(UPDATED_NOTE)
            .serialNumber(UPDATED_SERIAL_NUMBER);

        restMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaterial.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMaterial))
            )
            .andExpect(status().isOk());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
        Material testMaterial = materialList.get(materialList.size() - 1);
        assertThat(testMaterial.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMaterial.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testMaterial.getPurchaseDate()).isEqualTo(UPDATED_PURCHASE_DATE);
        assertThat(testMaterial.getWarrantyEndDate()).isEqualTo(UPDATED_WARRANTY_END_DATE);
        assertThat(testMaterial.getManufacturer()).isEqualTo(UPDATED_MANUFACTURER);
        assertThat(testMaterial.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testMaterial.getStatusMaterial()).isEqualTo(UPDATED_STATUS_MATERIAL);
        assertThat(testMaterial.getLastMaintenanceDate()).isEqualTo(UPDATED_LAST_MAINTENANCE_DATE);
        assertThat(testMaterial.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testMaterial.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingMaterial() throws Exception {
        int databaseSizeBeforeUpdate = materialRepository.findAll().size();
        material.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, material.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(material))
            )
            .andExpect(status().isBadRequest());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMaterial() throws Exception {
        int databaseSizeBeforeUpdate = materialRepository.findAll().size();
        material.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(material))
            )
            .andExpect(status().isBadRequest());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMaterial() throws Exception {
        int databaseSizeBeforeUpdate = materialRepository.findAll().size();
        material.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaterialMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(material)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMaterial() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        int databaseSizeBeforeDelete = materialRepository.findAll().size();

        // Delete the material
        restMaterialMockMvc
            .perform(delete(ENTITY_API_URL_ID, material.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
