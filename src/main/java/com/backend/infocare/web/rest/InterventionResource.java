package com.backend.infocare.web.rest;

import com.backend.infocare.domain.Intervention;
import com.backend.infocare.repository.InterventionRepository;
import com.backend.infocare.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.backend.infocare.domain.Intervention}.
 */
@RestController
@RequestMapping("/api/interventions")
@Transactional
public class InterventionResource {

    private static final Logger log = LoggerFactory.getLogger(InterventionResource.class);

    private static final String ENTITY_NAME = "intervention";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InterventionRepository interventionRepository;

    public InterventionResource(InterventionRepository interventionRepository) {
        this.interventionRepository = interventionRepository;
    }

    /**
     * {@code POST  /interventions} : Create a new intervention.
     *
     * @param intervention the intervention to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new intervention, or with status {@code 400 (Bad Request)} if the intervention has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Intervention> createIntervention(@Valid @RequestBody Intervention intervention) throws URISyntaxException {
        log.debug("REST request to save Intervention : {}", intervention);
        if (intervention.getId() != null) {
            throw new BadRequestAlertException("A new intervention cannot already have an ID", ENTITY_NAME, "idexists");
        }
        intervention = interventionRepository.save(intervention);
        return ResponseEntity.created(new URI("/api/interventions/" + intervention.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, intervention.getId().toString()))
            .body(intervention);
    }

    /**
     * {@code PUT  /interventions/:id} : Updates an existing intervention.
     *
     * @param id the id of the intervention to save.
     * @param intervention the intervention to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated intervention,
     * or with status {@code 400 (Bad Request)} if the intervention is not valid,
     * or with status {@code 500 (Internal Server Error)} if the intervention couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Intervention> updateIntervention(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Intervention intervention
    ) throws URISyntaxException {
        log.debug("REST request to update Intervention : {}, {}", id, intervention);
        if (intervention.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, intervention.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!interventionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        intervention = interventionRepository.save(intervention);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, intervention.getId().toString()))
            .body(intervention);
    }

    /**
     * {@code PATCH  /interventions/:id} : Partial updates given fields of an existing intervention, field will ignore if it is null
     *
     * @param id the id of the intervention to save.
     * @param intervention the intervention to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated intervention,
     * or with status {@code 400 (Bad Request)} if the intervention is not valid,
     * or with status {@code 404 (Not Found)} if the intervention is not found,
     * or with status {@code 500 (Internal Server Error)} if the intervention couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Intervention> partialUpdateIntervention(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Intervention intervention
    ) throws URISyntaxException {
        log.debug("REST request to partial update Intervention partially : {}, {}", id, intervention);
        if (intervention.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, intervention.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!interventionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Intervention> result = interventionRepository
            .findById(intervention.getId())
            .map(existingIntervention -> {
                if (intervention.getTitle() != null) {
                    existingIntervention.setTitle(intervention.getTitle());
                }
                if (intervention.getDescription() != null) {
                    existingIntervention.setDescription(intervention.getDescription());
                }
                if (intervention.getCreatedBy() != null) {
                    existingIntervention.setCreatedBy(intervention.getCreatedBy());
                }
                if (intervention.getCreatedAt() != null) {
                    existingIntervention.setCreatedAt(intervention.getCreatedAt());
                }
                if (intervention.getAttachments() != null) {
                    existingIntervention.setAttachments(intervention.getAttachments());
                }
                if (intervention.getNotes() != null) {
                    existingIntervention.setNotes(intervention.getNotes());
                }

                return existingIntervention;
            })
            .map(interventionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, intervention.getId().toString())
        );
    }

    /**
     * {@code GET  /interventions} : get all the interventions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of interventions in body.
     */
    @GetMapping("")
    public List<Intervention> getAllInterventions() {
        log.debug("REST request to get all Interventions");
        return interventionRepository.findAll();
    }

    /**
     * {@code GET  /interventions/:id} : get the "id" intervention.
     *
     * @param id the id of the intervention to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the intervention, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Intervention> getIntervention(@PathVariable("id") Long id) {
        log.debug("REST request to get Intervention : {}", id);
        Optional<Intervention> intervention = interventionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(intervention);
    }

    /**
     * {@code DELETE  /interventions/:id} : delete the "id" intervention.
     *
     * @param id the id of the intervention to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIntervention(@PathVariable("id") Long id) {
        log.debug("REST request to delete Intervention : {}", id);
        interventionRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
