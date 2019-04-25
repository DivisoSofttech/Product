package com.diviso.graeshoppe.service;

import com.diviso.graeshoppe.service.dto.BarcodeTypeDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing BarcodeType.
 */
public interface BarcodeTypeService {

    /**
     * Save a barcodeType.
     *
     * @param barcodeTypeDTO the entity to save
     * @return the persisted entity
     */
    BarcodeTypeDTO save(BarcodeTypeDTO barcodeTypeDTO);

    /**
     * Get all the barcodeTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<BarcodeTypeDTO> findAll(Pageable pageable);


    /**
     * Get the "id" barcodeType.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<BarcodeTypeDTO> findOne(Long id);

    /**
     * Delete the "id" barcodeType.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the barcodeType corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<BarcodeTypeDTO> search(String query, Pageable pageable);
}
