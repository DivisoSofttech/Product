package com.diviso.graeshoppe.service.mapper;

import com.diviso.graeshoppe.domain.*;
import com.diviso.graeshoppe.service.dto.StockDiaryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity StockDiary and its DTO StockDiaryDTO.
 */
@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface StockDiaryMapper extends EntityMapper<StockDiaryDTO, StockDiary> {

    @Mapping(source = "product.id", target = "productId")
    StockDiaryDTO toDto(StockDiary stockDiary);

    @Mapping(source = "productId", target = "product")
    StockDiary toEntity(StockDiaryDTO stockDiaryDTO);

    default StockDiary fromId(Long id) {
        if (id == null) {
            return null;
        }
        StockDiary stockDiary = new StockDiary();
        stockDiary.setId(id);
        return stockDiary;
    }
}
