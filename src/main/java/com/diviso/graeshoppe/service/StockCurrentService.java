package com.diviso.graeshoppe.service;

import com.diviso.graeshoppe.service.dto.StockCurrentDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing StockCurrent.
 */
public interface StockCurrentService {

    /**
     * Save a stockCurrent.
     *
     * @param stockCurrentDTO the entity to save
     * @return the persisted entity
     */
    StockCurrentDTO save(StockCurrentDTO stockCurrentDTO);

    /**
     * Get all the stockCurrents.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StockCurrentDTO> findAll(Pageable pageable);
    /**
     * Get all the StockCurrentDTO where Product is null.
     *
     * @return the list of entities
     */
    List<StockCurrentDTO> findAllWhereProductIsNull();


    /**
     * Get the "id" stockCurrent.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<StockCurrentDTO> findOne(Long id);

    /**
     * Delete the "id" stockCurrent.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the stockCurrent corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<StockCurrentDTO> search(String query, Pageable pageable);

	Optional<StockCurrentDTO> findByProductId(Long id);
	 StockCurrentDTO updateStockCurrent(StockCurrentDTO stockCurrentDTO);
}
