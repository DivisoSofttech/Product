package com.diviso.graeshoppe.service;

import com.diviso.graeshoppe.service.dto.TaxCategoryDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing TaxCategory.
 */
public interface TaxCategoryService {

    /**
     * Save a taxCategory.
     *
     * @param taxCategoryDTO the entity to save
     * @return the persisted entity
     */
    TaxCategoryDTO save(TaxCategoryDTO taxCategoryDTO);

    /**
     * Get all the taxCategories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TaxCategoryDTO> findAll(Pageable pageable);


    /**
     * Get the "id" taxCategory.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TaxCategoryDTO> findOne(Long id);

    /**
     * Delete the "id" taxCategory.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the taxCategory corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TaxCategoryDTO> search(String query, Pageable pageable);
}
