package com.diviso.graeshoppe.service.mapper;

import com.diviso.graeshoppe.domain.*;
import com.diviso.graeshoppe.service.dto.BarcodeTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity BarcodeType and its DTO BarcodeTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {BarcodeMapper.class})
public interface BarcodeTypeMapper extends EntityMapper<BarcodeTypeDTO, BarcodeType> {

    @Mapping(source = "barcode.id", target = "barcodeId")
    BarcodeTypeDTO toDto(BarcodeType barcodeType);

    @Mapping(source = "barcodeId", target = "barcode")
    BarcodeType toEntity(BarcodeTypeDTO barcodeTypeDTO);

    default BarcodeType fromId(Long id) {
        if (id == null) {
            return null;
        }
        BarcodeType barcodeType = new BarcodeType();
        barcodeType.setId(id);
        return barcodeType;
    }
}
