package com.backend.infocare.web.rest;

import com.backend.infocare.domain.ApplicationUserTicket;
import com.backend.infocare.repository.ApplicationUserTicketRepository;
import com.backend.infocare.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.backend.infocare.domain.ApplicationUserTicket}.
 */
@RestController
@RequestMapping("/api/application-user-tickets")
@Transactional
public class ApplicationUserTicketResource {

    private final Logger log = LoggerFactory.getLogger(ApplicationUserTicketResource.class);

    private static final String ENTITY_NAME = "applicationUserTicket";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApplicationUserTicketRepository applicationUserTicketRepository;

    public ApplicationUserTicketResource(ApplicationUserTicketRepository applicationUserTicketRepository) {
        this.applicationUserTicketRepository = applicationUserTicketRepository;
    }

    /**
     * {@code POST  /application-user-tickets} : Create a new applicationUserTicket.
     *
     * @param applicationUserTicket the applicationUserTicket to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new applicationUserTicket, or with status {@code 400 (Bad Request)} if the applicationUserTicket has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ApplicationUserTicket> createApplicationUserTicket(@RequestBody ApplicationUserTicket applicationUserTicket)
        throws URISyntaxException {
        log.debug("REST request to save ApplicationUserTicket : {}", applicationUserTicket);
        if (applicationUserTicket.getId() != null) {
            throw new BadRequestAlertException("A new applicationUserTicket cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApplicationUserTicket result = applicationUserTicketRepository.save(applicationUserTicket);
        return ResponseEntity
            .created(new URI("/api/application-user-tickets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /application-user-tickets/:id} : Updates an existing applicationUserTicket.
     *
     * @param id the id of the applicationUserTicket to save.
     * @param applicationUserTicket the applicationUserTicket to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationUserTicket,
     * or with status {@code 400 (Bad Request)} if the applicationUserTicket is not valid,
     * or with status {@code 500 (Internal Server Error)} if the applicationUserTicket couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApplicationUserTicket> updateApplicationUserTicket(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ApplicationUserTicket applicationUserTicket
    ) throws URISyntaxException {
        log.debug("REST request to update ApplicationUserTicket : {}, {}", id, applicationUserTicket);
        if (applicationUserTicket.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicationUserTicket.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicationUserTicketRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        // no save call needed as we have no fields that can be updated
        ApplicationUserTicket result = applicationUserTicket;
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, applicationUserTicket.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /application-user-tickets/:id} : Partial updates given fields of an existing applicationUserTicket, field will ignore if it is null
     *
     * @param id the id of the applicationUserTicket to save.
     * @param applicationUserTicket the applicationUserTicket to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationUserTicket,
     * or with status {@code 400 (Bad Request)} if the applicationUserTicket is not valid,
     * or with status {@code 404 (Not Found)} if the applicationUserTicket is not found,
     * or with status {@code 500 (Internal Server Error)} if the applicationUserTicket couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ApplicationUserTicket> partialUpdateApplicationUserTicket(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ApplicationUserTicket applicationUserTicket
    ) throws URISyntaxException {
        log.debug("REST request to partial update ApplicationUserTicket partially : {}, {}", id, applicationUserTicket);
        if (applicationUserTicket.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, applicationUserTicket.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicationUserTicketRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApplicationUserTicket> result = applicationUserTicketRepository.findById(applicationUserTicket.getId()); // .map(applicationUserTicketRepository::save)

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, applicationUserTicket.getId().toString())
        );
    }

    /**
     * {@code GET  /application-user-tickets} : get all the applicationUserTickets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of applicationUserTickets in body.
     */
    @GetMapping("")
    public List<ApplicationUserTicket> getAllApplicationUserTickets() {
        log.debug("REST request to get all ApplicationUserTickets");
        return applicationUserTicketRepository.findAll();
    }

    /**
     * {@code GET  /application-user-tickets/:id} : get the "id" applicationUserTicket.
     *
     * @param id the id of the applicationUserTicket to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the applicationUserTicket, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationUserTicket> getApplicationUserTicket(@PathVariable("id") Long id) {
        log.debug("REST request to get ApplicationUserTicket : {}", id);
        Optional<ApplicationUserTicket> applicationUserTicket = applicationUserTicketRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(applicationUserTicket);
    }

    /**
     * {@code DELETE  /application-user-tickets/:id} : delete the "id" applicationUserTicket.
     *
     * @param id the id of the applicationUserTicket to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplicationUserTicket(@PathVariable("id") Long id) {
        log.debug("REST request to delete ApplicationUserTicket : {}", id);
        applicationUserTicketRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
