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
 * A TaxCategory.
 */
@Entity
@Table(name = "tax_category")
@Document(indexName = "taxcategory")
public class TaxCategory implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "taxCategory")
    private Set<Tax> taxes = new HashSet<>();
    @OneToMany(mappedBy = "taxCategory")
    private Set<Product> products = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public TaxCategory name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public TaxCategory description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Tax> getTaxes() {
        return taxes;
    }

    public TaxCategory taxes(Set<Tax> taxes) {
        this.taxes = taxes;
        return this;
    }

    public TaxCategory addTaxes(Tax tax) {
        this.taxes.add(tax);
        tax.setTaxCategory(this);
        return this;
    }

    public TaxCategory removeTaxes(Tax tax) {
        this.taxes.remove(tax);
        tax.setTaxCategory(null);
        return this;
    }

    public void setTaxes(Set<Tax> taxes) {
        this.taxes = taxes;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public TaxCategory products(Set<Product> products) {
        this.products = products;
        return this;
    }

    public TaxCategory addProducts(Product product) {
        this.products.add(product);
        product.setTaxCategory(this);
        return this;
    }

    public TaxCategory removeProducts(Product product) {
        this.products.remove(product);
        product.setTaxCategory(null);
        return this;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
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
        TaxCategory taxCategory = (TaxCategory) o;
        if (taxCategory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), taxCategory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TaxCategory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
