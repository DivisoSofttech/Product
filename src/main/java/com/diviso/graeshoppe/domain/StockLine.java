package com.diviso.graeshoppe.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A StockLine.
 */
@Entity
@Table(name = "stock_line")
@Document(indexName = "stockline")
public class StockLine implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "reference", nullable = false)
    private String reference;

    @Column(name = "buy_price")
    private Double buyPrice;

    @Column(name = "sell_price_inclusive")
    private Double sellPriceInclusive;

    @Column(name = "sell_price_exclusive")
    private Double sellPriceExclusive;

    @Column(name = "gross_profit")
    private Double grossProfit;

    @Column(name = "margin")
    private Double margin;

    @NotNull
    @Column(name = "units", nullable = false)
    private Double units;

    @Column(name = "infrastructure_id")
    private Long infrastructureId;

    @Column(name = "location_id")
    private String locationId;

    @Column(name = "supplier_ref")
    private Long supplierRef;

    @ManyToOne
    @JsonIgnoreProperties("stockLines")
    private Product product;

    @ManyToOne
    @JsonIgnoreProperties("stockLines")
    private Uom uom;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public StockLine reference(String reference) {
        this.reference = reference;
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Double getBuyPrice() {
        return buyPrice;
    }

    public StockLine buyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
        return this;
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Double getSellPriceInclusive() {
        return sellPriceInclusive;
    }

    public StockLine sellPriceInclusive(Double sellPriceInclusive) {
        this.sellPriceInclusive = sellPriceInclusive;
        return this;
    }

    public void setSellPriceInclusive(Double sellPriceInclusive) {
        this.sellPriceInclusive = sellPriceInclusive;
    }

    public Double getSellPriceExclusive() {
        return sellPriceExclusive;
    }

    public StockLine sellPriceExclusive(Double sellPriceExclusive) {
        this.sellPriceExclusive = sellPriceExclusive;
        return this;
    }

    public void setSellPriceExclusive(Double sellPriceExclusive) {
        this.sellPriceExclusive = sellPriceExclusive;
    }

    public Double getGrossProfit() {
        return grossProfit;
    }

    public StockLine grossProfit(Double grossProfit) {
        this.grossProfit = grossProfit;
        return this;
    }

    public void setGrossProfit(Double grossProfit) {
        this.grossProfit = grossProfit;
    }

    public Double getMargin() {
        return margin;
    }

    public StockLine margin(Double margin) {
        this.margin = margin;
        return this;
    }

    public void setMargin(Double margin) {
        this.margin = margin;
    }

    public Double getUnits() {
        return units;
    }

    public StockLine units(Double units) {
        this.units = units;
        return this;
    }

    public void setUnits(Double units) {
        this.units = units;
    }

    public Long getInfrastructureId() {
        return infrastructureId;
    }

    public StockLine infrastructureId(Long infrastructureId) {
        this.infrastructureId = infrastructureId;
        return this;
    }

    public void setInfrastructureId(Long infrastructureId) {
        this.infrastructureId = infrastructureId;
    }

    public String getLocationId() {
        return locationId;
    }

    public StockLine locationId(String locationId) {
        this.locationId = locationId;
        return this;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public Long getSupplierRef() {
        return supplierRef;
    }

    public StockLine supplierRef(Long supplierRef) {
        this.supplierRef = supplierRef;
        return this;
    }

    public void setSupplierRef(Long supplierRef) {
        this.supplierRef = supplierRef;
    }

    public Product getProduct() {
        return product;
    }

    public StockLine product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Uom getUom() {
        return uom;
    }

    public StockLine uom(Uom uom) {
        this.uom = uom;
        return this;
    }

    public void setUom(Uom uom) {
        this.uom = uom;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockLine stockLine = (StockLine) o;
        if (stockLine.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stockLine.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StockLine{" +
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
            "}";
    }
}
