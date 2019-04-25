package com.diviso.graeshoppe.service;

import com.diviso.graeshoppe.service.dto.TaxTypeDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing TaxType.
 */
public interface TaxTypeService {

    /**
     * Save a taxType.
     *
     * @param taxTypeDTO the entity to save
     * @return the persisted entity
     */
    TaxTypeDTO save(TaxTypeDTO taxTypeDTO);

    /**
     * Get all the taxTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TaxTypeDTO> findAll(Pageable pageable);


    /**
     * Get the "id" taxType.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TaxTypeDTO> findOne(Long id);

    /**
     * Delete the "id" taxType.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the taxType corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TaxTypeDTO> search(String query, Pageable pageable);
}
