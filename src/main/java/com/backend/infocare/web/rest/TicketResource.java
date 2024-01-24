package com.backend.infocare.web.rest;

import com.backend.infocare.domain.Ticket;
import com.backend.infocare.domain.User;
import com.backend.infocare.repository.TicketRepository;
import com.backend.infocare.service.UserService;
import com.backend.infocare.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.backend.infocare.domain.Ticket}.
 */
@RestController
@RequestMapping("/api/tickets")
@Transactional
public class TicketResource {

    private final Logger log = LoggerFactory.getLogger(TicketResource.class);

    private static final String ENTITY_NAME = "ticket";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TicketRepository ticketRepository;
    private final UserService userService;

    public TicketResource(TicketRepository ticketRepository, UserService userService) {
        this.ticketRepository = ticketRepository;
        this.userService = userService;
    }

    /**
     * {@code POST  /tickets} : Create a new ticket.
     *
     * @param ticket the ticket to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new ticket, or with status {@code 400 (Bad Request)} if the
     *         ticket has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Ticket> createTicket(@Valid @RequestBody Ticket ticket) throws URISyntaxException {
        log.debug("REST request to save Ticket : {}", ticket);
        if (ticket.getId() != null) {
            throw new BadRequestAlertException("A new ticket cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ticket result = ticketRepository.save(ticket);
        return ResponseEntity
            .created(new URI("/api/tickets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tickets/:id} : Updates an existing ticket.
     *
     * @param id     the id of the ticket to save.
     * @param ticket the ticket to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated ticket,
     *         or with status {@code 400 (Bad Request)} if the ticket is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the ticket
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateTicket(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Ticket ticket
    ) throws URISyntaxException {
        log.debug("REST request to update Ticket : {}, {}", id, ticket);
        if (ticket.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ticket.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ticketRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Ticket result = ticketRepository.save(ticket);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ticket.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tickets/:id} : Partial updates given fields of an existing
     * ticket, field will ignore if it is null
     *
     * @param id     the id of the ticket to save.
     * @param ticket the ticket to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated ticket,
     *         or with status {@code 400 (Bad Request)} if the ticket is not valid,
     *         or with status {@code 404 (Not Found)} if the ticket is not found,
     *         or with status {@code 500 (Internal Server Error)} if the ticket
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Ticket> partialUpdateTicket(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Ticket ticket
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ticket partially : {}, {}", id, ticket);
        if (ticket.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ticket.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ticketRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ticket> result = ticketRepository
            .findById(ticket.getId())
            .map(existingTicket -> {
                if (ticket.getTitle() != null) {
                    existingTicket.setTitle(ticket.getTitle());
                }
                if (ticket.getDescription() != null) {
                    existingTicket.setDescription(ticket.getDescription());
                }
                if (ticket.getCreatedAt() != null) {
                    existingTicket.setCreatedAt(ticket.getCreatedAt());
                }
                if (ticket.getResolutionDate() != null) {
                    existingTicket.setResolutionDate(ticket.getResolutionDate());
                }
                if (ticket.getClosedAt() != null) {
                    existingTicket.setClosedAt(ticket.getClosedAt());
                }
                if (ticket.getLimitDate() != null) {
                    existingTicket.setLimitDate(ticket.getLimitDate());
                }
                if (ticket.getImpact() != null) {
                    existingTicket.setImpact(ticket.getImpact());
                }
                if (ticket.getResolution() != null) {
                    existingTicket.setResolution(ticket.getResolution());
                }
                if (ticket.getAttachments() != null) {
                    existingTicket.setAttachments(ticket.getAttachments());
                }

                return existingTicket;
            })
            .map(ticketRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ticket.getId().toString())
        );
    }

    /**
     * {@code GET  /tickets} : get all the tickets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of tickets in body.
     */
    @GetMapping("")
    public List<Ticket> getAllTickets() {
        log.debug("REST request to get all Tickets");
        return ticketRepository.findAll();
    }

    /**
     * {@code GET  /tickets/:id} : get the "id" ticket.
     *
     * @param id the id of the ticket to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the ticket, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicket(@PathVariable("id") Long id) {
        log.debug("REST request to get Ticket : {}", id);
        Optional<Ticket> ticket = ticketRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ticket);
    }

    /**
     * {@code DELETE  /tickets/:id} : delete the "id" ticket.
     *
     * @param id the id of the ticket to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable("id") Long id) {
        log.debug("REST request to delete Ticket : {}", id);
        ticketRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Ticket>> getRecentTickets() {
        String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.getUserWithAuthoritiesByLogin(userLogin);
        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Long applicationUserId = user.get().getId();

        log.debug(applicationUserId + "");
        List<Ticket> recentTickets = ticketRepository.findTop4ByApplicationUsers_UserIdOrderByCreatedAtDesc(applicationUserId);
        if (CollectionUtils.isEmpty(recentTickets)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(recentTickets);
        }
    }
}
