package com.diviso.graeshoppe.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the StockLine entity.
 */
public class StockLineDTO implements Serializable {

    private Long id;

    @NotNull
    private String reference;

    private Double buyPrice;

    private Double sellPriceInclusive;

    private Double sellPriceExclusive;

    private Double grossProfit;

    private Double margin;

    @NotNull
    private Double units;

    private Long infrastructureId;

    private String locationId;

    private Long supplierRef;


    private Long productId;

    private Long uomId;

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

    public Double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Double getSellPriceInclusive() {
        return sellPriceInclusive;
    }

    public void setSellPriceInclusive(Double sellPriceInclusive) {
        this.sellPriceInclusive = sellPriceInclusive;
    }

    public Double getSellPriceExclusive() {
        return sellPriceExclusive;
    }

    public void setSellPriceExclusive(Double sellPriceExclusive) {
        this.sellPriceExclusive = sellPriceExclusive;
    }

    public Double getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(Double grossProfit) {
        this.grossProfit = grossProfit;
    }

    public Double getMargin() {
        return margin;
    }

    public void setMargin(Double margin) {
        this.margin = margin;
    }

    public Double getUnits() {
        return units;
    }

    public void setUnits(Double units) {
        this.units = units;
    }

    public Long getInfrastructureId() {
        return infrastructureId;
    }

    public void setInfrastructureId(Long infrastructureId) {
        this.infrastructureId = infrastructureId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public Long getSupplierRef() {
        return supplierRef;
    }

    public void setSupplierRef(Long supplierRef) {
        this.supplierRef = supplierRef;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getUomId() {
        return uomId;
    }

    public void setUomId(Long uomId) {
        this.uomId = uomId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockLineDTO stockLineDTO = (StockLineDTO) o;
        if (stockLineDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stockLineDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StockLineDTO{" +
            "id=" + getId() +
            ", reference='" + getReference() + "'" +
            ", buyPrice=" + getBuyPrice() +
            ", sellPriceInclusive=" + getSellPriceInclusive() +
            ", sellPriceExclusive=" + getSellPriceExclusive() +
            ", grossProfit=" + getGrossProfit() +
            ", margin=" + getMargin() +
            ", units=" + getUnits() +
            ", infrastructureId=" + getInfrastructureId() +
            ", locationId='" + getLocationId() + "'" +
            ", supplierRef=" + getSupplierRef() +
            ", product=" + getProductId() +
            ", uom=" + getUomId() +
            "}";
    }
}
