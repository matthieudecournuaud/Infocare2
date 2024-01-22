package com.backend.infocare.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Status.
 */
@Entity
@Table(name = "status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Status implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @NotNull
    @Size(max = 20)
    @Column(name = "status_code", length = 20, nullable = false)
    private String statusCode;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @Size(max = 7)
    @Column(name = "color_code", length = 7)
    private String colorCode;

    @Size(max = 200)
    @Column(name = "next_possible_status", length = 200)
    private String nextPossibleStatus;

    @Column(name = "is_final")
    private Boolean isFinal;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Status id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Status name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatusCode() {
        return this.statusCode;
    }

    public Status statusCode(String statusCode) {
        this.setStatusCode(statusCode);
        return this;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getDescription() {
        return this.description;
    }

    public Status description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColorCode() {
        return this.colorCode;
    }

    public Status colorCode(String colorCode) {
        this.setColorCode(colorCode);
        return this;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getNextPossibleStatus() {
        return this.nextPossibleStatus;
    }

    public Status nextPossibleStatus(String nextPossibleStatus) {
        this.setNextPossibleStatus(nextPossibleStatus);
        return this;
    }

    public void setNextPossibleStatus(String nextPossibleStatus) {
        this.nextPossibleStatus = nextPossibleStatus;
    }

    public Boolean getIsFinal() {
        return this.isFinal;
    }

    public Status isFinal(Boolean isFinal) {
        this.setIsFinal(isFinal);
        return this;
    }

    public void setIsFinal(Boolean isFinal) {
        this.isFinal = isFinal;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Status)) {
            return false;
        }
        return getId() != null && getId().equals(((Status) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Status{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", statusCode='" + getStatusCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", colorCode='" + getColorCode() + "'" +
            ", nextPossibleStatus='" + getNextPossibleStatus() + "'" +
            ", isFinal='" + getIsFinal() + "'" +
            "}";
    }
}
