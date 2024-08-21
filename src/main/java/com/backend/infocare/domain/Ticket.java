package com.backend.infocare.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ticket.
 */
@Entity
@Table(name = "ticket")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @NotNull
    @Size(max = 5000)
    @Column(name = "description", length = 5000, nullable = false)
    private String description;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "resolution_date")
    private LocalDate resolutionDate;

    @Column(name = "closed_at")
    private LocalDate closedAt;

    @Column(name = "limit_date")
    private LocalDate limitDate;

    @Size(max = 50)
    @Column(name = "impact", length = 50)
    private String impact;

    @Size(max = 5000)
    @Column(name = "resolution", length = 5000)
    private String resolution;

    @Size(max = 5000)
    @Column(name = "attachments", length = 5000)
    private String attachments;

    @JsonIgnoreProperties(value = { "user", "ticket" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private ApplicationUser applicationUser;

    @JsonIgnoreProperties(value = { "ticket" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Category category;

    @JsonIgnoreProperties(value = { "ticket" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Status status;

    @JsonIgnoreProperties(value = { "ticket" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Priority priority;

    @JsonIgnoreProperties(value = { "company", "ticket" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Material material;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ticket")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ticket" }, allowSetters = true)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ticket")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "procedure", "ticket" }, allowSetters = true)
    private Set<Intervention> interventions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ticket id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Ticket title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Ticket description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public Ticket createdAt(LocalDate createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getResolutionDate() {
        return this.resolutionDate;
    }

    public Ticket resolutionDate(LocalDate resolutionDate) {
        this.setResolutionDate(resolutionDate);
        return this;
    }

    public void setResolutionDate(LocalDate resolutionDate) {
        this.resolutionDate = resolutionDate;
    }

    public LocalDate getClosedAt() {
        return this.closedAt;
    }

    public Ticket closedAt(LocalDate closedAt) {
        this.setClosedAt(closedAt);
        return this;
    }

    public void setClosedAt(LocalDate closedAt) {
        this.closedAt = closedAt;
    }

    public LocalDate getLimitDate() {
        return this.limitDate;
    }

    public Ticket limitDate(LocalDate limitDate) {
        this.setLimitDate(limitDate);
        return this;
    }

    public void setLimitDate(LocalDate limitDate) {
        this.limitDate = limitDate;
    }

    public String getImpact() {
        return this.impact;
    }

    public Ticket impact(String impact) {
        this.setImpact(impact);
        return this;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }

    public String getResolution() {
        return this.resolution;
    }

    public Ticket resolution(String resolution) {
        this.setResolution(resolution);
        return this;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getAttachments() {
        return this.attachments;
    }

    public Ticket attachments(String attachments) {
        this.setAttachments(attachments);
        return this;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public ApplicationUser getApplicationUser() {
        return this.applicationUser;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }

    public Ticket applicationUser(ApplicationUser applicationUser) {
        this.setApplicationUser(applicationUser);
        return this;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Ticket category(Category category) {
        this.setCategory(category);
        return this;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Ticket status(Status status) {
        this.setStatus(status);
        return this;
    }

    public Priority getPriority() {
        return this.priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Ticket priority(Priority priority) {
        this.setPriority(priority);
        return this;
    }

    public Material getMaterial() {
        return this.material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Ticket material(Material material) {
        this.setMaterial(material);
        return this;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setTicket(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setTicket(this));
        }
        this.comments = comments;
    }

    public Ticket comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public Ticket addComment(Comment comment) {
        this.comments.add(comment);
        comment.setTicket(this);
        return this;
    }

    public Ticket removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setTicket(null);
        return this;
    }

    public Set<Intervention> getInterventions() {
        return this.interventions;
    }

    public void setInterventions(Set<Intervention> interventions) {
        if (this.interventions != null) {
            this.interventions.forEach(i -> i.setTicket(null));
        }
        if (interventions != null) {
            interventions.forEach(i -> i.setTicket(this));
        }
        this.interventions = interventions;
    }

    public Ticket interventions(Set<Intervention> interventions) {
        this.setInterventions(interventions);
        return this;
    }

    public Ticket addIntervention(Intervention intervention) {
        this.interventions.add(intervention);
        intervention.setTicket(this);
        return this;
    }

    public Ticket removeIntervention(Intervention intervention) {
        this.interventions.remove(intervention);
        intervention.setTicket(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ticket)) {
            return false;
        }
        return getId() != null && getId().equals(((Ticket) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ticket{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", resolutionDate='" + getResolutionDate() + "'" +
            ", closedAt='" + getClosedAt() + "'" +
            ", limitDate='" + getLimitDate() + "'" +
            ", impact='" + getImpact() + "'" +
            ", resolution='" + getResolution() + "'" +
            ", attachments='" + getAttachments() + "'" +
            "}";
    }
}
