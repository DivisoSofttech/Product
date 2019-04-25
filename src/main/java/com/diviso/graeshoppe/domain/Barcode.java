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
 * A Barcode.
 */
@Entity
@Table(name = "barcode")
@Document(indexName = "barcode")
public class Barcode implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "barcode")
    private Set<BarcodeType> barcodeTypes = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Barcode code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public Barcode description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<BarcodeType> getBarcodeTypes() {
        return barcodeTypes;
    }

    public Barcode barcodeTypes(Set<BarcodeType> barcodeTypes) {
        this.barcodeTypes = barcodeTypes;
        return this;
    }

    public Barcode addBarcodeType(BarcodeType barcodeType) {
        this.barcodeTypes.add(barcodeType);
        barcodeType.setBarcode(this);
        return this;
    }

    public Barcode removeBarcodeType(BarcodeType barcodeType) {
        this.barcodeTypes.remove(barcodeType);
        barcodeType.setBarcode(null);
        return this;
    }

    public void setBarcodeTypes(Set<BarcodeType> barcodeTypes) {
        this.barcodeTypes = barcodeTypes;
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
        Barcode barcode = (Barcode) o;
        if (barcode.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), barcode.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Barcode{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
