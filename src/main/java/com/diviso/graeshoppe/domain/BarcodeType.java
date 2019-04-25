package com.diviso.graeshoppe.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A BarcodeType.
 */
@Entity
@Table(name = "barcode_type")
@Document(indexName = "barcodetype")
public class BarcodeType implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "barcode_type")
    private String barcodeType;

    @ManyToOne
    @JsonIgnoreProperties("barcodeTypes")
    private Barcode barcode;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBarcodeType() {
        return barcodeType;
    }

    public BarcodeType barcodeType(String barcodeType) {
        this.barcodeType = barcodeType;
        return this;
    }

    public void setBarcodeType(String barcodeType) {
        this.barcodeType = barcodeType;
    }

    public Barcode getBarcode() {
        return barcode;
    }

    public BarcodeType barcode(Barcode barcode) {
        this.barcode = barcode;
        return this;
    }

    public void setBarcode(Barcode barcode) {
        this.barcode = barcode;
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
        BarcodeType barcodeType = (BarcodeType) o;
        if (barcodeType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), barcodeType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BarcodeType{" +
            "id=" + getId() +
            ", barcodeType='" + getBarcodeType() + "'" +
            "}";
    }
}
