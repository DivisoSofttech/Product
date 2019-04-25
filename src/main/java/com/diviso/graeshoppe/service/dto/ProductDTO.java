package com.diviso.graeshoppe.service.dto;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Product entity.
 */
public class ProductDTO implements Serializable {

    private Long id;

    @NotNull
    private String reference;

    @NotNull
    private String searchkey;

    @NotNull
    private String name;

    @Lob
    private byte[] image;

    private String imageContentType;
    private String description;

    private String sku;

    private String mpn;

    private Boolean visible;

    private LocalDate dateOfMfd;

    private LocalDate dateOfExpiry;

    private Double maximumStockLevel;

    private Double reOrderLevel;


    private Long barcodeId;

    private Set<LabelDTO> labels = new HashSet<>();

    private Set<CategoryDTO> categories = new HashSet<>();

    private Long statusId;

    private Long taxCategoryId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getSearchkey() {
        return searchkey;
    }

    public void setSearchkey(String searchkey) {
        this.searchkey = searchkey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getMpn() {
        return mpn;
    }

    public void setMpn(String mpn) {
        this.mpn = mpn;
    }

    public Boolean isVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public LocalDate getDateOfMfd() {
        return dateOfMfd;
    }

    public void setDateOfMfd(LocalDate dateOfMfd) {
        this.dateOfMfd = dateOfMfd;
    }

    public LocalDate getDateOfExpiry() {
        return dateOfExpiry;
    }

    public void setDateOfExpiry(LocalDate dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
    }

    public Double getMaximumStockLevel() {
        return maximumStockLevel;
    }

    public void setMaximumStockLevel(Double maximumStockLevel) {
        this.maximumStockLevel = maximumStockLevel;
    }

    public Double getReOrderLevel() {
        return reOrderLevel;
    }

    public void setReOrderLevel(Double reOrderLevel) {
        this.reOrderLevel = reOrderLevel;
    }

    public Long getBarcodeId() {
        return barcodeId;
    }

    public void setBarcodeId(Long barcodeId) {
        this.barcodeId = barcodeId;
    }

    public Set<LabelDTO> getLabels() {
        return labels;
    }

    public void setLabels(Set<LabelDTO> labels) {
        this.labels = labels;
    }

    public Set<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryDTO> categories) {
        this.categories = categories;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public Long getTaxCategoryId() {
        return taxCategoryId;
    }

    public void setTaxCategoryId(Long taxCategoryId) {
        this.taxCategoryId = taxCategoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (productDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + getId() +
            ", reference='" + getReference() + "'" +
            ", searchkey='" + getSearchkey() + "'" +
            ", name='" + getName() + "'" +
            ", image='" + getImage() + "'" +
            ", description='" + getDescription() + "'" +
            ", sku='" + getSku() + "'" +
            ", mpn='" + getMpn() + "'" +
            ", visible='" + isVisible() + "'" +
            ", dateOfMfd='" + getDateOfMfd() + "'" +
            ", dateOfExpiry='" + getDateOfExpiry() + "'" +
            ", maximumStockLevel=" + getMaximumStockLevel() +
            ", reOrderLevel=" + getReOrderLevel() +
            ", barcode=" + getBarcodeId() +
            ", status=" + getStatusId() +
            ", taxCategory=" + getTaxCategoryId() +
            "}";
    }
}
