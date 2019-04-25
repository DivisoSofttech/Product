package com.diviso.graeshoppe.service.mapper;

import com.diviso.graeshoppe.domain.*;
import com.diviso.graeshoppe.service.dto.BarcodeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Barcode and its DTO BarcodeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BarcodeMapper extends EntityMapper<BarcodeDTO, Barcode> {


    @Mapping(target = "barcodeTypes", ignore = true)
    Barcode toEntity(BarcodeDTO barcodeDTO);

    default Barcode fromId(Long id) {
        if (id == null) {
            return null;
        }
        Barcode barcode = new Barcode();
        barcode.setId(id);
        return barcode;
    }
}
