package com.backend.infocare.web.rest;

import com.backend.infocare.domain.Material;
import com.backend.infocare.domain.Ticket;
import com.backend.infocare.repository.CategoryRepository;
import com.backend.infocare.repository.MaterialRepository;
import com.backend.infocare.repository.PriorityRepository;
import com.backend.infocare.repository.StatusRepository;
import com.backend.infocare.repository.TicketRepository;
import com.backend.infocare.service.UserService;
import com.backend.infocare.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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
    private final StatusRepository statusRepository;
    private final PriorityRepository priorityRepository;
    private final MaterialRepository materialRepository;
    private final CategoryRepository categoryRepository;

    public TicketResource(
        TicketRepository ticketRepository,
        UserService userService,
        StatusRepository statusRepository,
        PriorityRepository priorityRepository,
        MaterialRepository materialRepository,
        CategoryRepository categoryRepository
    ) {
        this.ticketRepository = ticketRepository;
        this.userService = userService;
        this.statusRepository = statusRepository;
        this.priorityRepository = priorityRepository;
        this.materialRepository = materialRepository;
        this.categoryRepository = categoryRepository;
    }

    private Ticket enrichTicketStatus(Ticket ticket) {
        if (ticket != null && ticket.getStatus() != null && ticket.getStatus().getId() != null) {
            ticket.setStatus(statusRepository.findById(ticket.getStatus().getId()).orElseThrow());
        }
        return ticket;
    }

    private Ticket enrichTicketPriority(Ticket ticket) {
        if (ticket != null && ticket.getPriority() != null && ticket.getPriority().getId() != null) {
            ticket.setPriority(priorityRepository.findById(ticket.getPriority().getId()).orElseThrow());
        }
        return ticket;
    }

    private Ticket enrichTicketMaterial(Ticket ticket) {
        if (ticket != null && ticket.getMaterials() != null) {
            Set<Material> materials = ticket.getMaterials();
            Set<Material> enrichedMaterials = new HashSet<>();

            for (Material material : materials) {
                if (material.getId() != null) {
                    Material enrichedMaterial = materialRepository.findById(material.getId()).orElseThrow();
                    enrichedMaterials.add(enrichedMaterial);
                }
            }

            ticket.setMaterials(enrichedMaterials);
        }
        return ticket;
    }

    private Ticket enrichTicketCategory(Ticket ticket) {
        if (ticket != null && ticket.getCategory() != null && ticket.getCategory().getId() != null) {
            ticket.setCategory(categoryRepository.findById(ticket.getCategory().getId()).orElseThrow());
        }
        return ticket;
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
        Optional<Ticket> ticket = ticketRepository
            .findById(id)
            .map(this::enrichTicketStatus)
            .map(this::enrichTicketPriority)
            .map(this::enrichTicketMaterial)
            .map(this::enrichTicketCategory);
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
        Long applicationUserId = userService
            .getUserWithAuthoritiesByLogin(userLogin)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
            .getId();

        log.debug(applicationUserId + "");
        List<Ticket> recentTickets = ticketRepository
            .findTop4ByApplicationUsers_UserIdOrderByCreatedAtDesc(applicationUserId)
            .stream()
            .map(this::enrichTicketStatus)
            .collect(Collectors.toList());
        if (recentTickets.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(recentTickets);
        }
    }

    @GetMapping("/user/{username}/resolved-tickets-percentage")
    public ResponseEntity<Double> getResolvedTicketsPercentage(@PathVariable String username) {
        Long userId = userService
            .getUserWithAuthoritiesByLogin(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
            .getId();

        Long totalTickets = ticketRepository.countByApplicationUsers_UserId(userId);
        Long resolvedTickets = ticketRepository.countResolvedTicketsByUserId(userId);
        if (totalTickets == 0) {
            return ResponseEntity.ok(0.0);
        }
        double percentage = ((double) resolvedTickets / totalTickets) * 100;
        return ResponseEntity.ok(percentage);
    }

    @GetMapping("/user/{username}/tickets-by-priority")
    public ResponseEntity<List<Object[]>> getTicketsCountByPriorityForUser(@PathVariable String username) {
        Long userId = userService
            .getUserWithAuthoritiesByLogin(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
            .getId();

        List<Object[]> ticketsCountByPriority = ticketRepository.countTicketsByPriorityAndUserId(userId);
        if (ticketsCountByPriority.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(ticketsCountByPriority);
        }
    }

    @PostMapping("/tickets/purge-old")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<Void> purgeOldTickets() {
        ticketRepository.purgeOldTickets();
        return ResponseEntity.ok().build();
    }
}
