package com.diviso.graeshoppe.service.impl;

import com.diviso.graeshoppe.service.BarcodeService;
import com.diviso.graeshoppe.domain.Barcode;
import com.diviso.graeshoppe.repository.BarcodeRepository;
import com.diviso.graeshoppe.repository.search.BarcodeSearchRepository;
import com.diviso.graeshoppe.service.dto.BarcodeDTO;
import com.diviso.graeshoppe.service.mapper.BarcodeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Barcode.
 */
@Service
@Transactional
public class BarcodeServiceImpl implements BarcodeService {

    private final Logger log = LoggerFactory.getLogger(BarcodeServiceImpl.class);

    private final BarcodeRepository barcodeRepository;

    private final BarcodeMapper barcodeMapper;

    private final BarcodeSearchRepository barcodeSearchRepository;

    public BarcodeServiceImpl(BarcodeRepository barcodeRepository, BarcodeMapper barcodeMapper, BarcodeSearchRepository barcodeSearchRepository) {
        this.barcodeRepository = barcodeRepository;
        this.barcodeMapper = barcodeMapper;
        this.barcodeSearchRepository = barcodeSearchRepository;
    }

    /**
     * Save a barcode.
     *
     * @param barcodeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public BarcodeDTO save(BarcodeDTO barcodeDTO) {
        log.debug("Request to save Barcode : {}", barcodeDTO);
        Barcode barcode = barcodeMapper.toEntity(barcodeDTO);
        barcode = barcodeRepository.save(barcode);
        BarcodeDTO result = barcodeMapper.toDto(barcode);
        barcodeSearchRepository.save(barcode);
        return result;
    }

    /**
     * Get all the barcodes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BarcodeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Barcodes");
        return barcodeRepository.findAll(pageable)
            .map(barcodeMapper::toDto);
    }


    /**
     * Get one barcode by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<BarcodeDTO> findOne(Long id) {
        log.debug("Request to get Barcode : {}", id);
        return barcodeRepository.findById(id)
            .map(barcodeMapper::toDto);
    }

    /**
     * Delete the barcode by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Barcode : {}", id);        barcodeRepository.deleteById(id);
        barcodeSearchRepository.deleteById(id);
    }

    /**
     * Search for the barcode corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BarcodeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Barcodes for query {}", query);
        return barcodeSearchRepository.search(queryStringQuery(query), pageable)
            .map(barcodeMapper::toDto);
    }
}
