package com.diviso.graeshoppe.service.impl;

import com.diviso.graeshoppe.service.TaxTypeService;
import com.diviso.graeshoppe.domain.TaxType;
import com.diviso.graeshoppe.repository.TaxTypeRepository;
import com.diviso.graeshoppe.repository.search.TaxTypeSearchRepository;
import com.diviso.graeshoppe.service.dto.TaxTypeDTO;
import com.diviso.graeshoppe.service.mapper.TaxTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TaxType.
 */
@Service
@Transactional
public class TaxTypeServiceImpl implements TaxTypeService {

    private final Logger log = LoggerFactory.getLogger(TaxTypeServiceImpl.class);

    private final TaxTypeRepository taxTypeRepository;

    private final TaxTypeMapper taxTypeMapper;

    private final TaxTypeSearchRepository taxTypeSearchRepository;

    public TaxTypeServiceImpl(TaxTypeRepository taxTypeRepository, TaxTypeMapper taxTypeMapper, TaxTypeSearchRepository taxTypeSearchRepository) {
        this.taxTypeRepository = taxTypeRepository;
        this.taxTypeMapper = taxTypeMapper;
        this.taxTypeSearchRepository = taxTypeSearchRepository;
    }

    /**
     * Save a taxType.
     *
     * @param taxTypeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TaxTypeDTO save(TaxTypeDTO taxTypeDTO) {
        log.debug("Request to save TaxType : {}", taxTypeDTO);
        TaxType taxType = taxTypeMapper.toEntity(taxTypeDTO);
        taxType = taxTypeRepository.save(taxType);
        TaxTypeDTO result = taxTypeMapper.toDto(taxType);
        taxTypeSearchRepository.save(taxType);
        return result;
    }

    /**
     * Get all the taxTypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TaxTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TaxTypes");
        return taxTypeRepository.findAll(pageable)
            .map(taxTypeMapper::toDto);
    }


    /**
     * Get one taxType by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TaxTypeDTO> findOne(Long id) {
        log.debug("Request to get TaxType : {}", id);
        return taxTypeRepository.findById(id)
            .map(taxTypeMapper::toDto);
    }

    /**
     * Delete the taxType by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TaxType : {}", id);        taxTypeRepository.deleteById(id);
        taxTypeSearchRepository.deleteById(id);
    }

    /**
     * Search for the taxType corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TaxTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TaxTypes for query {}", query);
        return taxTypeSearchRepository.search(queryStringQuery(query), pageable)
            .map(taxTypeMapper::toDto);
    }
}
