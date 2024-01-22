package com.backend.infocare.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Procedure.
 */
@Entity
@Table(name = "jhi_procedure")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Procedure implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "category")
    private String category;

    @Column(name = "procedure_id")
    private Long procedureId;

    @Size(max = 5000)
    @Column(name = "step_by_step_guide", length = 5000)
    private String stepByStepGuide;

    @Column(name = "estimated_time")
    private Integer estimatedTime;

    @Size(max = 500)
    @Column(name = "required_tools", length = 500)
    private String requiredTools;

    @Size(max = 500)
    @Column(name = "skills_required", length = 500)
    private String skillsRequired;

    @Size(max = 500)
    @Column(name = "safety_instructions", length = 500)
    private String safetyInstructions;

    @Column(name = "last_reviewed")
    private LocalDate lastReviewed;

    @Size(max = 50)
    @Column(name = "reviewed_by", length = 50)
    private String reviewedBy;

    @Size(max = 200)
    @Column(name = "attachments", length = 200)
    private String attachments;

    @JsonIgnoreProperties(value = { "procedure", "ticket" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "procedure")
    private Intervention intervention;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Procedure id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Procedure name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Procedure description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return this.category;
    }

    public Procedure category(String category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getProcedureId() {
        return this.procedureId;
    }

    public Procedure procedureId(Long procedureId) {
        this.setProcedureId(procedureId);
        return this;
    }

    public void setProcedureId(Long procedureId) {
        this.procedureId = procedureId;
    }

    public String getStepByStepGuide() {
        return this.stepByStepGuide;
    }

    public Procedure stepByStepGuide(String stepByStepGuide) {
        this.setStepByStepGuide(stepByStepGuide);
        return this;
    }

    public void setStepByStepGuide(String stepByStepGuide) {
        this.stepByStepGuide = stepByStepGuide;
    }

    public Integer getEstimatedTime() {
        return this.estimatedTime;
    }

    public Procedure estimatedTime(Integer estimatedTime) {
        this.setEstimatedTime(estimatedTime);
        return this;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getRequiredTools() {
        return this.requiredTools;
    }

    public Procedure requiredTools(String requiredTools) {
        this.setRequiredTools(requiredTools);
        return this;
    }

    public void setRequiredTools(String requiredTools) {
        this.requiredTools = requiredTools;
    }

    public String getSkillsRequired() {
        return this.skillsRequired;
    }

    public Procedure skillsRequired(String skillsRequired) {
        this.setSkillsRequired(skillsRequired);
        return this;
    }

    public void setSkillsRequired(String skillsRequired) {
        this.skillsRequired = skillsRequired;
    }

    public String getSafetyInstructions() {
        return this.safetyInstructions;
    }

    public Procedure safetyInstructions(String safetyInstructions) {
        this.setSafetyInstructions(safetyInstructions);
        return this;
    }

    public void setSafetyInstructions(String safetyInstructions) {
        this.safetyInstructions = safetyInstructions;
    }

    public LocalDate getLastReviewed() {
        return this.lastReviewed;
    }

    public Procedure lastReviewed(LocalDate lastReviewed) {
        this.setLastReviewed(lastReviewed);
        return this;
    }

    public void setLastReviewed(LocalDate lastReviewed) {
        this.lastReviewed = lastReviewed;
    }

    public String getReviewedBy() {
        return this.reviewedBy;
    }

    public Procedure reviewedBy(String reviewedBy) {
        this.setReviewedBy(reviewedBy);
        return this;
    }

    public void setReviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public String getAttachments() {
        return this.attachments;
    }

    public Procedure attachments(String attachments) {
        this.setAttachments(attachments);
        return this;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public Intervention getIntervention() {
        return this.intervention;
    }

    public void setIntervention(Intervention intervention) {
        if (this.intervention != null) {
            this.intervention.setProcedure(null);
        }
        if (intervention != null) {
            intervention.setProcedure(this);
        }
        this.intervention = intervention;
    }

    public Procedure intervention(Intervention intervention) {
        this.setIntervention(intervention);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Procedure)) {
            return false;
        }
        return getId() != null && getId().equals(((Procedure) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Procedure{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", category='" + getCategory() + "'" +
            ", procedureId=" + getProcedureId() +
            ", stepByStepGuide='" + getStepByStepGuide() + "'" +
            ", estimatedTime=" + getEstimatedTime() +
            ", requiredTools='" + getRequiredTools() + "'" +
            ", skillsRequired='" + getSkillsRequired() + "'" +
            ", safetyInstructions='" + getSafetyInstructions() + "'" +
            ", lastReviewed='" + getLastReviewed() + "'" +
            ", reviewedBy='" + getReviewedBy() + "'" +
            ", attachments='" + getAttachments() + "'" +
            "}";
    }
}
