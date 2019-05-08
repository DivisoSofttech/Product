package com.diviso.graeshoppe.service;

import com.diviso.graeshoppe.service.dto.StockDiaryDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing StockDiary.
 */
public interface StockDiaryService {

    /**
     * Save a stockDiary.
     *
     * @param stockDiaryDTO the entity to save
     * @return the persisted entity
     */
    StockDiaryDTO save(StockDiaryDTO stockDiaryDTO);

    /**
     * Get all the stockDiaries.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StockDiaryDTO> findAll(Pageable pageable);


    /**
     * Get the "id" stockDiary.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<StockDiaryDTO> findOne(Long id);

    /**
     * Delete the "id" stockDiary.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the stockDiary corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StockDiaryDTO> search(String query, Pageable pageable);

    Page<StockDiaryDTO> findByProductId(Long id, Pageable pageable);

	StockDiaryDTO createStockOfProduct(StockDiaryDTO stockDiaryDTO);
}
