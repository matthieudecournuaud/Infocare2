package com.backend.infocare.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Comment.
 */
@Entity
@Table(name = "comment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Size(max = 50)
    @Column(name = "type", length = 50)
    private String type;

    @Size(max = 50)
    @Column(name = "visibility", length = 50)
    private String visibility;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "edited_by", nullable = false)
    private String editedBy;

    @NotNull
    @Column(name = "edited_at", nullable = false)
    private LocalDate editedAt;

    @Size(max = 5000)
    @Column(name = "attachments", length = 5000)
    private String attachments;

    @Column(name = "response_to_comment_id")
    private Long responseToCommentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "materials", "comments", "interventions", "category", "status", "priority", "applicationUsers" },
        allowSetters = true
    )
    private Ticket ticket;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Comment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Comment title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return this.type;
    }

    public Comment type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVisibility() {
        return this.visibility;
    }

    public Comment visibility(String visibility) {
        this.setVisibility(visibility);
        return this;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getDescription() {
        return this.description;
    }

    public Comment description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEditedBy() {
        return this.editedBy;
    }

    public Comment editedBy(String editedBy) {
        this.setEditedBy(editedBy);
        return this;
    }

    public void setEditedBy(String editedBy) {
        this.editedBy = editedBy;
    }

    public LocalDate getEditedAt() {
        return this.editedAt;
    }

    public Comment editedAt(LocalDate editedAt) {
        this.setEditedAt(editedAt);
        return this;
    }

    public void setEditedAt(LocalDate editedAt) {
        this.editedAt = editedAt;
    }

    public String getAttachments() {
        return this.attachments;
    }

    public Comment attachments(String attachments) {
        this.setAttachments(attachments);
        return this;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public Long getResponseToCommentId() {
        return this.responseToCommentId;
    }

    public Comment responseToCommentId(Long responseToCommentId) {
        this.setResponseToCommentId(responseToCommentId);
        return this;
    }

    public void setResponseToCommentId(Long responseToCommentId) {
        this.responseToCommentId = responseToCommentId;
    }

    public Ticket getTicket() {
        return this.ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Comment ticket(Ticket ticket) {
        this.setTicket(ticket);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Comment)) {
            return false;
        }
        return getId() != null && getId().equals(((Comment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Comment{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", type='" + getType() + "'" +
            ", visibility='" + getVisibility() + "'" +
            ", description='" + getDescription() + "'" +
            ", editedBy='" + getEditedBy() + "'" +
            ", editedAt='" + getEditedAt() + "'" +
            ", attachments='" + getAttachments() + "'" +
            ", responseToCommentId=" + getResponseToCommentId() +
            "}";
    }
}
