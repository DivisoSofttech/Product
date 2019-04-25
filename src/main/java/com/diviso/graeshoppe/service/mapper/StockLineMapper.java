package com.diviso.graeshoppe.service.mapper;

import com.diviso.graeshoppe.domain.*;
import com.diviso.graeshoppe.service.dto.StockLineDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity StockLine and its DTO StockLineDTO.
 */
@Mapper(componentModel = "spring", uses = {ProductMapper.class, UomMapper.class})
public interface StockLineMapper extends EntityMapper<StockLineDTO, StockLine> {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "uom.id", target = "uomId")
    StockLineDTO toDto(StockLine stockLine);

    @Mapping(source = "productId", target = "product")
    @Mapping(source = "uomId", target = "uom")
    StockLine toEntity(StockLineDTO stockLineDTO);

    default StockLine fromId(Long id) {
        if (id == null) {
            return null;
        }
        StockLine stockLine = new StockLine();
        stockLine.setId(id);
        return stockLine;
    }
}
