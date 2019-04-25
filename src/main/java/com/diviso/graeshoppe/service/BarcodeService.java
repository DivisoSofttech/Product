package com.diviso.graeshoppe.service;

import com.diviso.graeshoppe.service.dto.BarcodeDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Barcode.
 */
public interface BarcodeService {

    /**
     * Save a barcode.
     *
     * @param barcodeDTO the entity to save
     * @return the persisted entity
     */
    BarcodeDTO save(BarcodeDTO barcodeDTO);

    /**
     * Get all the barcodes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<BarcodeDTO> findAll(Pageable pageable);


    /**
     * Get the "id" barcode.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<BarcodeDTO> findOne(Long id);

    /**
     * Delete the "id" barcode.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the barcode corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<BarcodeDTO> search(String query, Pageable pageable);
}
