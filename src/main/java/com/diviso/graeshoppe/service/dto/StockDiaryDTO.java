package com.diviso.graeshoppe.service.dto;
import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the StockDiary entity.
 */
public class StockDiaryDTO implements Serializable {

    private Long id;

    private LocalDate dateOfCreation;

    private Double units;

    private Double price;

    private Boolean isBuy;


    private Long productId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDate dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public Double getUnits() {
        return units;
    }

    public void setUnits(Double units) {
        this.units = units;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean isIsBuy() {
        return isBuy;
    }

    public void setIsBuy(Boolean isBuy) {
        this.isBuy = isBuy;
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

        StockDiaryDTO stockDiaryDTO = (StockDiaryDTO) o;
        if (stockDiaryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stockDiaryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StockDiaryDTO{" +
            "id=" + getId() +
            ", dateOfCreation='" + getDateOfCreation() + "'" +
            ", units=" + getUnits() +
            ", price=" + getPrice() +
            ", isBuy='" + isIsBuy() + "'" +
            ", product=" + getProductId() +
            "}";
    }
}
