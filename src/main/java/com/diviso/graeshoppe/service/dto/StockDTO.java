package com.diviso.graeshoppe.service.dto;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Stock entity.
 */
public class StockDTO implements Serializable {

    private Long id;

    @NotNull
    private String reference;

    private Long deliveryNoteRef;

    private LocalDate dateOfStockUpdated;

    private Double storageCost;


    private Long statusId;

    private Set<StockLineDTO> stockLines = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Long getDeliveryNoteRef() {
        return deliveryNoteRef;
    }

    public void setDeliveryNoteRef(Long deliveryNoteRef) {
        this.deliveryNoteRef = deliveryNoteRef;
    }

    public LocalDate getDateOfStockUpdated() {
        return dateOfStockUpdated;
    }

    public void setDateOfStockUpdated(LocalDate dateOfStockUpdated) {
        this.dateOfStockUpdated = dateOfStockUpdated;
    }

    public Double getStorageCost() {
        return storageCost;
    }

    public void setStorageCost(Double storageCost) {
        this.storageCost = storageCost;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public Set<StockLineDTO> getStockLines() {
        return stockLines;
    }

    public void setStockLines(Set<StockLineDTO> stockLines) {
        this.stockLines = stockLines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockDTO stockDTO = (StockDTO) o;
        if (stockDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stockDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StockDTO{" +
            "id=" + getId() +
            ", reference='" + getReference() + "'" +
            ", deliveryNoteRef=" + getDeliveryNoteRef() +
            ", dateOfStockUpdated='" + getDateOfStockUpdated() + "'" +
            ", storageCost=" + getStorageCost() +
            ", status=" + getStatusId() +
            "}";
    }
}
