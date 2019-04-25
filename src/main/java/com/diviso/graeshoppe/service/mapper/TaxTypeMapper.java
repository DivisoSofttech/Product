package com.diviso.graeshoppe.service.mapper;

import com.diviso.graeshoppe.domain.*;
import com.diviso.graeshoppe.service.dto.TaxTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TaxType and its DTO TaxTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {TaxMapper.class})
public interface TaxTypeMapper extends EntityMapper<TaxTypeDTO, TaxType> {

    @Mapping(source = "tax.id", target = "taxId")
    TaxTypeDTO toDto(TaxType taxType);

    @Mapping(source = "taxId", target = "tax")
    TaxType toEntity(TaxTypeDTO taxTypeDTO);

    default TaxType fromId(Long id) {
        if (id == null) {
            return null;
        }
        TaxType taxType = new TaxType();
        taxType.setId(id);
        return taxType;
    }
}
