package com.diviso.graeshoppe.service.impl;

import com.diviso.graeshoppe.service.BarcodeTypeService;
import com.diviso.graeshoppe.domain.BarcodeType;
import com.diviso.graeshoppe.repository.BarcodeTypeRepository;
import com.diviso.graeshoppe.repository.search.BarcodeTypeSearchRepository;
import com.diviso.graeshoppe.service.dto.BarcodeTypeDTO;
import com.diviso.graeshoppe.service.mapper.BarcodeTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing BarcodeType.
 */
@Service
@Transactional
public class BarcodeTypeServiceImpl implements BarcodeTypeService {

    private final Logger log = LoggerFactory.getLogger(BarcodeTypeServiceImpl.class);

    private final BarcodeTypeRepository barcodeTypeRepository;

    private final BarcodeTypeMapper barcodeTypeMapper;

    private final BarcodeTypeSearchRepository barcodeTypeSearchRepository;

    public BarcodeTypeServiceImpl(BarcodeTypeRepository barcodeTypeRepository, BarcodeTypeMapper barcodeTypeMapper, BarcodeTypeSearchRepository barcodeTypeSearchRepository) {
        this.barcodeTypeRepository = barcodeTypeRepository;
        this.barcodeTypeMapper = barcodeTypeMapper;
        this.barcodeTypeSearchRepository = barcodeTypeSearchRepository;
    }

    /**
     * Save a barcodeType.
     *
     * @param barcodeTypeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public BarcodeTypeDTO save(BarcodeTypeDTO barcodeTypeDTO) {
        log.debug("Request to save BarcodeType : {}", barcodeTypeDTO);
        BarcodeType barcodeType = barcodeTypeMapper.toEntity(barcodeTypeDTO);
        barcodeType = barcodeTypeRepository.save(barcodeType);
        BarcodeTypeDTO result = barcodeTypeMapper.toDto(barcodeType);
        barcodeTypeSearchRepository.save(barcodeType);
        return result;
    }

    /**
     * Get all the barcodeTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BarcodeTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BarcodeTypes");
        return barcodeTypeRepository.findAll(pageable)
            .map(barcodeTypeMapper::toDto);
    }


    /**
     * Get one barcodeType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<BarcodeTypeDTO> findOne(Long id) {
        log.debug("Request to get BarcodeType : {}", id);
        return barcodeTypeRepository.findById(id)
            .map(barcodeTypeMapper::toDto);
    }

    /**
     * Delete the barcodeType by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete BarcodeType : {}", id);        barcodeTypeRepository.deleteById(id);
        barcodeTypeSearchRepository.deleteById(id);
    }

    /**
     * Search for the barcodeType corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BarcodeTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BarcodeTypes for query {}", query);
        return barcodeTypeSearchRepository.search(queryStringQuery(query), pageable)
            .map(barcodeTypeMapper::toDto);
    }
}
