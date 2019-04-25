package com.diviso.graeshoppe.service;

import com.diviso.graeshoppe.service.dto.StockLineDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing StockLine.
 */
public interface StockLineService {

    /**
     * Save a stockLine.
     *
     * @param stockLineDTO the entity to save
     * @return the persisted entity
     */
    StockLineDTO save(StockLineDTO stockLineDTO);

    /**
     * Get all the stockLines.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StockLineDTO> findAll(Pageable pageable);


    /**
     * Get the "id" stockLine.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<StockLineDTO> findOne(Long id);

    /**
     * Delete the "id" stockLine.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the stockLine corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StockLineDTO> search(String query, Pageable pageable);
}
