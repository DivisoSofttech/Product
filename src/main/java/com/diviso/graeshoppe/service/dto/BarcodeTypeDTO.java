package com.diviso.graeshoppe.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the BarcodeType entity.
 */
public class BarcodeTypeDTO implements Serializable {

    private Long id;

    private String barcodeType;


    private Long barcodeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBarcodeType() {
        return barcodeType;
    }

    public void setBarcodeType(String barcodeType) {
        this.barcodeType = barcodeType;
    }

    public Long getBarcodeId() {
        return barcodeId;
    }

    public void setBarcodeId(Long barcodeId) {
        this.barcodeId = barcodeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BarcodeTypeDTO barcodeTypeDTO = (BarcodeTypeDTO) o;
        if (barcodeTypeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), barcodeTypeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BarcodeTypeDTO{" +
            "id=" + getId() +
            ", barcodeType='" + getBarcodeType() + "'" +
            ", barcode=" + getBarcodeId() +
            "}";
    }
}
