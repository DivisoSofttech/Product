package com.diviso.graeshoppe.service;

import com.diviso.graeshoppe.service.dto.UomDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Uom.
 */
public interface UomService {

    /**
     * Save a uom.
     *
     * @param uomDTO the entity to save
     * @return the persisted entity
     */
    UomDTO save(UomDTO uomDTO);

    /**
     * Get all the uoms.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<UomDTO> findAll(Pageable pageable);


    /**
     * Get the "id" uom.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<UomDTO> findOne(Long id);

    /**
     * Delete the "id" uom.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the uom corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<UomDTO> search(String query, Pageable pageable);
}
