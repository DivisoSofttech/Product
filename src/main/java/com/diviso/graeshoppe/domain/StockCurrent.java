package com.diviso.graeshoppe.domain;



import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A StockCurrent.
 */
@Entity
@Table(name = "stock_current")
@Document(indexName = "stockcurrent")
public class StockCurrent implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "units")
    private Double units;

    @OneToOne
    @JoinColumn(unique = true)
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getUnits() {
        return units;
    }

    public StockCurrent units(Double units) {
        this.units = units;
        return this;
    }

    public void setUnits(Double units) {
        this.units = units;
    }

    public Product getProduct() {
        return product;
    }

    public StockCurrent product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
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
        StockCurrent stockCurrent = (StockCurrent) o;
        if (stockCurrent.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stockCurrent.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StockCurrent{" +
            "id=" + getId() +
            ", units=" + getUnits() +
            "}";
    }
}
