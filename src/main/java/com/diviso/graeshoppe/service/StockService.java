package com.diviso.graeshoppe.service;

import com.diviso.graeshoppe.service.dto.StockDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Stock.
 */
public interface StockService {

    /**
     * Save a stock.
     *
     * @param stockDTO the entity to save
     * @return the persisted entity
     */
    StockDTO save(StockDTO stockDTO);

    /**
     * Get all the stocks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StockDTO> findAll(Pageable pageable);

    /**
     * Get all the Stock with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<StockDTO> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" stock.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<StockDTO> findOne(Long id);

    /**
     * Delete the "id" stock.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

	/**
	 * @param query
	 * @param pageable
	 * @return
	 */
	Page<StockDTO> search(String query, Pageable pageable);

    /**
     * Search for the stock corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    
}
