package com.diviso.graeshoppe.service;

import com.diviso.graeshoppe.service.dto.LabelDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Label.
 */
public interface LabelService {

    /**
     * Save a label.
     *
     * @param labelDTO the entity to save
     * @return the persisted entity
     */
    LabelDTO save(LabelDTO labelDTO);

    /**
     * Get all the labels.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<LabelDTO> findAll(Pageable pageable);


    /**
     * Get the "id" label.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<LabelDTO> findOne(Long id);

    /**
     * Delete the "id" label.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the label corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<LabelDTO> search(String query, Pageable pageable);
}
