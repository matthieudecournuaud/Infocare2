package com.backend.infocare.web.rest;

import com.backend.infocare.domain.Material;
import com.backend.infocare.repository.MaterialRepository;
import com.backend.infocare.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.backend.infocare.domain.Material}.
 */
@RestController
@RequestMapping("/api/materials")
@Transactional
public class MaterialResource {

    private final Logger log = LoggerFactory.getLogger(MaterialResource.class);

    private static final String ENTITY_NAME = "material";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MaterialRepository materialRepository;

    public MaterialResource(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    /**
     * {@code POST  /materials} : Create a new material.
     *
     * @param material the material to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new material, or with status {@code 400 (Bad Request)} if the material has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Material> createMaterial(@Valid @RequestBody Material material) throws URISyntaxException {
        log.debug("REST request to save Material : {}", material);
        if (material.getId() != null) {
            throw new BadRequestAlertException("A new material cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Material result = materialRepository.save(material);
        return ResponseEntity
            .created(new URI("/api/materials/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /materials/:id} : Updates an existing material.
     *
     * @param id the id of the material to save.
     * @param material the material to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated material,
     * or with status {@code 400 (Bad Request)} if the material is not valid,
     * or with status {@code 500 (Internal Server Error)} if the material couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Material> updateMaterial(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Material material
    ) throws URISyntaxException {
        log.debug("REST request to update Material : {}, {}", id, material);
        if (material.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, material.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!materialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Material result = materialRepository.save(material);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, material.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /materials/:id} : Partial updates given fields of an existing material, field will ignore if it is null
     *
     * @param id the id of the material to save.
     * @param material the material to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated material,
     * or with status {@code 400 (Bad Request)} if the material is not valid,
     * or with status {@code 404 (Not Found)} if the material is not found,
     * or with status {@code 500 (Internal Server Error)} if the material couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Material> partialUpdateMaterial(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Material material
    ) throws URISyntaxException {
        log.debug("REST request to partial update Material partially : {}, {}", id, material);
        if (material.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, material.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!materialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Material> result = materialRepository
            .findById(material.getId())
            .map(existingMaterial -> {
                if (material.getName() != null) {
                    existingMaterial.setName(material.getName());
                }
                if (material.getType() != null) {
                    existingMaterial.setType(material.getType());
                }
                if (material.getPurchaseDate() != null) {
                    existingMaterial.setPurchaseDate(material.getPurchaseDate());
                }
                if (material.getWarrantyEndDate() != null) {
                    existingMaterial.setWarrantyEndDate(material.getWarrantyEndDate());
                }
                if (material.getManufacturer() != null) {
                    existingMaterial.setManufacturer(material.getManufacturer());
                }
                if (material.getModel() != null) {
                    existingMaterial.setModel(material.getModel());
                }
                if (material.getStatusMaterial() != null) {
                    existingMaterial.setStatusMaterial(material.getStatusMaterial());
                }
                if (material.getLastMaintenanceDate() != null) {
                    existingMaterial.setLastMaintenanceDate(material.getLastMaintenanceDate());
                }
                if (material.getNote() != null) {
                    existingMaterial.setNote(material.getNote());
                }
                if (material.getSerialNumber() != null) {
                    existingMaterial.setSerialNumber(material.getSerialNumber());
                }

                return existingMaterial;
            })
            .map(materialRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, material.getId().toString())
        );
    }

    /**
     * {@code GET  /materials} : get all the materials.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of materials in body.
     */
    @GetMapping("")
    public List<Material> getAllMaterials(@RequestParam(name = "filter", required = false) String filter) {
        if ("ticket-is-null".equals(filter)) {
            log.debug("REST request to get all Materials where ticket is null");
            return StreamSupport
                .stream(materialRepository.findAll().spliterator(), false)
                .filter(material -> material.getTicket() == null)
                .toList();
        }
        log.debug("REST request to get all Materials");
        return materialRepository.findAll();
    }

    /**
     * {@code GET  /materials/:id} : get the "id" material.
     *
     * @param id the id of the material to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the material, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Material> getMaterial(@PathVariable("id") Long id) {
        log.debug("REST request to get Material : {}", id);
        Optional<Material> material = materialRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(material);
    }

    /**
     * {@code DELETE  /materials/:id} : delete the "id" material.
     *
     * @param id the id of the material to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable("id") Long id) {
        log.debug("REST request to delete Material : {}", id);
        materialRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
