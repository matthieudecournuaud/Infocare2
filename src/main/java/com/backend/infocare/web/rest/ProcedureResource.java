package com.backend.infocare.web.rest;

import com.backend.infocare.domain.Procedure;
import com.backend.infocare.repository.ProcedureRepository;
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
 * REST controller for managing {@link com.backend.infocare.domain.Procedure}.
 */
@RestController
@RequestMapping("/api/procedures")
@Transactional
public class ProcedureResource {

    private final Logger log = LoggerFactory.getLogger(ProcedureResource.class);

    private static final String ENTITY_NAME = "procedure";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcedureRepository procedureRepository;

    public ProcedureResource(ProcedureRepository procedureRepository) {
        this.procedureRepository = procedureRepository;
    }

    /**
     * {@code POST  /procedures} : Create a new procedure.
     *
     * @param procedure the procedure to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new procedure, or with status {@code 400 (Bad Request)} if the procedure has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Procedure> createProcedure(@Valid @RequestBody Procedure procedure) throws URISyntaxException {
        log.debug("REST request to save Procedure : {}", procedure);
        if (procedure.getId() != null) {
            throw new BadRequestAlertException("A new procedure cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Procedure result = procedureRepository.save(procedure);
        return ResponseEntity
            .created(new URI("/api/procedures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /procedures/:id} : Updates an existing procedure.
     *
     * @param id the id of the procedure to save.
     * @param procedure the procedure to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated procedure,
     * or with status {@code 400 (Bad Request)} if the procedure is not valid,
     * or with status {@code 500 (Internal Server Error)} if the procedure couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Procedure> updateProcedure(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Procedure procedure
    ) throws URISyntaxException {
        log.debug("REST request to update Procedure : {}, {}", id, procedure);
        if (procedure.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, procedure.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!procedureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Procedure result = procedureRepository.save(procedure);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, procedure.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /procedures/:id} : Partial updates given fields of an existing procedure, field will ignore if it is null
     *
     * @param id the id of the procedure to save.
     * @param procedure the procedure to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated procedure,
     * or with status {@code 400 (Bad Request)} if the procedure is not valid,
     * or with status {@code 404 (Not Found)} if the procedure is not found,
     * or with status {@code 500 (Internal Server Error)} if the procedure couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Procedure> partialUpdateProcedure(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Procedure procedure
    ) throws URISyntaxException {
        log.debug("REST request to partial update Procedure partially : {}, {}", id, procedure);
        if (procedure.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, procedure.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!procedureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Procedure> result = procedureRepository
            .findById(procedure.getId())
            .map(existingProcedure -> {
                if (procedure.getName() != null) {
                    existingProcedure.setName(procedure.getName());
                }
                if (procedure.getDescription() != null) {
                    existingProcedure.setDescription(procedure.getDescription());
                }
                if (procedure.getCategory() != null) {
                    existingProcedure.setCategory(procedure.getCategory());
                }
                if (procedure.getProcedureId() != null) {
                    existingProcedure.setProcedureId(procedure.getProcedureId());
                }
                if (procedure.getStepByStepGuide() != null) {
                    existingProcedure.setStepByStepGuide(procedure.getStepByStepGuide());
                }
                if (procedure.getEstimatedTime() != null) {
                    existingProcedure.setEstimatedTime(procedure.getEstimatedTime());
                }
                if (procedure.getRequiredTools() != null) {
                    existingProcedure.setRequiredTools(procedure.getRequiredTools());
                }
                if (procedure.getSkillsRequired() != null) {
                    existingProcedure.setSkillsRequired(procedure.getSkillsRequired());
                }
                if (procedure.getSafetyInstructions() != null) {
                    existingProcedure.setSafetyInstructions(procedure.getSafetyInstructions());
                }
                if (procedure.getLastReviewed() != null) {
                    existingProcedure.setLastReviewed(procedure.getLastReviewed());
                }
                if (procedure.getReviewedBy() != null) {
                    existingProcedure.setReviewedBy(procedure.getReviewedBy());
                }
                if (procedure.getAttachments() != null) {
                    existingProcedure.setAttachments(procedure.getAttachments());
                }

                return existingProcedure;
            })
            .map(procedureRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, procedure.getId().toString())
        );
    }

    /**
     * {@code GET  /procedures} : get all the procedures.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of procedures in body.
     */
    @GetMapping("")
    public List<Procedure> getAllProcedures(@RequestParam(name = "filter", required = false) String filter) {
        if ("intervention-is-null".equals(filter)) {
            log.debug("REST request to get all Procedures where intervention is null");
            return StreamSupport
                .stream(procedureRepository.findAll().spliterator(), false)
                .filter(procedure -> procedure.getIntervention() == null)
                .toList();
        }
        log.debug("REST request to get all Procedures");
        return procedureRepository.findAll();
    }

    /**
     * {@code GET  /procedures/:id} : get the "id" procedure.
     *
     * @param id the id of the procedure to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the procedure, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Procedure> getProcedure(@PathVariable("id") Long id) {
        log.debug("REST request to get Procedure : {}", id);
        Optional<Procedure> procedure = procedureRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(procedure);
    }

    /**
     * {@code DELETE  /procedures/:id} : delete the "id" procedure.
     *
     * @param id the id of the procedure to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProcedure(@PathVariable("id") Long id) {
        log.debug("REST request to delete Procedure : {}", id);
        procedureRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
