package com.diviso.graeshoppe.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the StockCurrent entity.
 */
public class StockCurrentDTO implements Serializable {

    private Long id;

    private Double units;


    private Long productId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getUnits() {
        return units;
    }

    public void setUnits(Double units) {
        this.units = units;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockCurrentDTO stockCurrentDTO = (StockCurrentDTO) o;
        if (stockCurrentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stockCurrentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StockCurrentDTO{" +
            "id=" + getId() +
            ", units=" + getUnits() +
            ", product=" + getProductId() +
            "}";
    }
}
