package com.diviso.graeshoppe.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Status.
 */
@Entity
@Table(name = "status")
@Document(indexName = "status")
public class Status implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference")
    private String reference;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "status",cascade=CascadeType.ALL)
    private Set<Product> products = new HashSet<>();
   
    @OneToMany(mappedBy = "status",cascade=CascadeType.ALL)
    private Set<Stock> stocks = new HashSet<>();
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

    public Status reference(String reference) {
        this.reference = reference;
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getName() {
        return name;
    }

    public Status name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Status description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public Status products(Set<Product> products) {
        this.products = products;
        return this;
    }

    public Status addProducts(Product product) {
        this.products.add(product);
        product.setStatus(this);
        return this;
    }

    public Status removeProducts(Product product) {
        this.products.remove(product);
        product.setStatus(null);
        return this;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Set<Stock> getStocks() {
        return stocks;
    }

    public Status stocks(Set<Stock> stocks) {
        this.stocks = stocks;
        return this;
    }

    public Status addStocks(Stock stock) {
        this.stocks.add(stock);
        stock.setStatus(this);
        return this;
    }

    public Status removeStocks(Stock stock) {
        this.stocks.remove(stock);
        stock.setStatus(null);
        return this;
    }

    public void setStocks(Set<Stock> stocks) {
        this.stocks = stocks;
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
        Status status = (Status) o;
        if (status.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), status.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Status{" +
            "id=" + getId() +
            ", reference='" + getReference() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
