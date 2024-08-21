package com.backend.infocare.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Material.
 */
@Entity
@Table(name = "material")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Material implements Serializable {

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
    @Size(max = 50)
    @Column(name = "type", length = 50, nullable = false)
    private String type;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "warranty_end_date")
    private LocalDate warrantyEndDate;

    @Size(max = 50)
    @Column(name = "manufacturer", length = 50)
    private String manufacturer;

    @Size(max = 50)
    @Column(name = "model", length = 50)
    private String model;

    @Size(max = 50)
    @Column(name = "status_material", length = 50)
    private String statusMaterial;

    @Column(name = "last_maintenance_date")
    private LocalDate lastMaintenanceDate;

    @Size(max = 500)
    @Column(name = "note", length = 500)
    private String note;

    @Column(name = "serial_number")
    private Long serialNumber;

    @JsonIgnoreProperties(value = { "material" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Company company;

    @JsonIgnoreProperties(
        value = { "applicationUser", "category", "status", "priority", "material", "comments", "interventions" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "material")
    private Ticket ticket;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Material id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Material name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public Material type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getPurchaseDate() {
        return this.purchaseDate;
    }

    public Material purchaseDate(LocalDate purchaseDate) {
        this.setPurchaseDate(purchaseDate);
        return this;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public LocalDate getWarrantyEndDate() {
        return this.warrantyEndDate;
    }

    public Material warrantyEndDate(LocalDate warrantyEndDate) {
        this.setWarrantyEndDate(warrantyEndDate);
        return this;
    }

    public void setWarrantyEndDate(LocalDate warrantyEndDate) {
        this.warrantyEndDate = warrantyEndDate;
    }

    public String getManufacturer() {
        return this.manufacturer;
    }

    public Material manufacturer(String manufacturer) {
        this.setManufacturer(manufacturer);
        return this;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return this.model;
    }

    public Material model(String model) {
        this.setModel(model);
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStatusMaterial() {
        return this.statusMaterial;
    }

    public Material statusMaterial(String statusMaterial) {
        this.setStatusMaterial(statusMaterial);
        return this;
    }

    public void setStatusMaterial(String statusMaterial) {
        this.statusMaterial = statusMaterial;
    }

    public LocalDate getLastMaintenanceDate() {
        return this.lastMaintenanceDate;
    }

    public Material lastMaintenanceDate(LocalDate lastMaintenanceDate) {
        this.setLastMaintenanceDate(lastMaintenanceDate);
        return this;
    }

    public void setLastMaintenanceDate(LocalDate lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }

    public String getNote() {
        return this.note;
    }

    public Material note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getSerialNumber() {
        return this.serialNumber;
    }

    public Material serialNumber(Long serialNumber) {
        this.setSerialNumber(serialNumber);
        return this;
    }

    public void setSerialNumber(Long serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Material company(Company company) {
        this.setCompany(company);
        return this;
    }

    public Ticket getTicket() {
        return this.ticket;
    }

    public void setTicket(Ticket ticket) {
        if (this.ticket != null) {
            this.ticket.setMaterial(null);
        }
        if (ticket != null) {
            ticket.setMaterial(this);
        }
        this.ticket = ticket;
    }

    public Material ticket(Ticket ticket) {
        this.setTicket(ticket);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Material)) {
            return false;
        }
        return getId() != null && getId().equals(((Material) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Material{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", purchaseDate='" + getPurchaseDate() + "'" +
            ", warrantyEndDate='" + getWarrantyEndDate() + "'" +
            ", manufacturer='" + getManufacturer() + "'" +
            ", model='" + getModel() + "'" +
            ", statusMaterial='" + getStatusMaterial() + "'" +
            ", lastMaintenanceDate='" + getLastMaintenanceDate() + "'" +
            ", note='" + getNote() + "'" +
            ", serialNumber=" + getSerialNumber() +
            "}";
    }
}
