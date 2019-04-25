package com.diviso.graeshoppe.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the TaxType entity.
 */
public class TaxTypeDTO implements Serializable {

    private Long id;

    private String taxType;


    private Long taxId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public Long getTaxId() {
        return taxId;
    }

    public void setTaxId(Long taxId) {
        this.taxId = taxId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TaxTypeDTO taxTypeDTO = (TaxTypeDTO) o;
        if (taxTypeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), taxTypeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TaxTypeDTO{" +
            "id=" + getId() +
            ", taxType='" + getTaxType() + "'" +
            ", tax=" + getTaxId() +
            "}";
    }
}
