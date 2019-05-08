package com.diviso.graeshoppe.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A StockDiary.
 */
@Entity
@Table(name = "stock_diary")
@Document(indexName = "stockdiary")
public class StockDiary implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_of_creation")
    private LocalDate dateOfCreation;

    @Column(name = "units")
    private Double units;

    @Column(name = "price")
    private Double price;

    @Column(name = "is_buy")
    private Boolean isBuy;

    @ManyToOne
    @JsonIgnoreProperties("stockDiaries")
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateOfCreation() {
        return dateOfCreation;
    }

    public StockDiary dateOfCreation(LocalDate dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
        return this;
    }

    public void setDateOfCreation(LocalDate dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public Double getUnits() {
        return units;
    }

    public StockDiary units(Double units) {
        this.units = units;
        return this;
    }

    public void setUnits(Double units) {
        this.units = units;
    }

    public Double getPrice() {
        return price;
    }

    public StockDiary price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean isIsBuy() {
        return isBuy;
    }

    public StockDiary isBuy(Boolean isBuy) {
        this.isBuy = isBuy;
        return this;
    }

    public void setIsBuy(Boolean isBuy) {
        this.isBuy = isBuy;
    }

    public Product getProduct() {
        return product;
    }

    public StockDiary product(Product product) {
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
        StockDiary stockDiary = (StockDiary) o;
        if (stockDiary.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stockDiary.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StockDiary{" +
            "id=" + getId() +
            ", dateOfCreation='" + getDateOfCreation() + "'" +
            ", units=" + getUnits() +
            ", price=" + getPrice() +
            ", isBuy='" + isIsBuy() + "'" +
            "}";
    }
}
