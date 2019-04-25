package com.diviso.graeshoppe.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TaxType.
 */
@Entity
@Table(name = "tax_type")
@Document(indexName = "taxtype")
public class TaxType implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tax_type")
    private String taxType;

    @ManyToOne
    @JsonIgnoreProperties("taxTypes")
    private Tax tax;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaxType() {
        return taxType;
    }

    public TaxType taxType(String taxType) {
        this.taxType = taxType;
        return this;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public Tax getTax() {
        return tax;
    }

    public TaxType tax(Tax tax) {
        this.tax = tax;
        return this;
    }

    public void setTax(Tax tax) {
        this.tax = tax;
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
        TaxType taxType = (TaxType) o;
        if (taxType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), taxType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TaxType{" +
            "id=" + getId() +
            ", taxType='" + getTaxType() + "'" +
            "}";
    }
}
