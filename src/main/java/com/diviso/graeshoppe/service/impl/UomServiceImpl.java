package com.diviso.graeshoppe.service.impl;

import com.diviso.graeshoppe.service.UomService;
import com.diviso.graeshoppe.domain.Uom;
import com.diviso.graeshoppe.repository.UomRepository;
import com.diviso.graeshoppe.repository.search.UomSearchRepository;
import com.diviso.graeshoppe.service.dto.UomDTO;
import com.diviso.graeshoppe.service.mapper.UomMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Uom.
 */
@Service
@Transactional
public class UomServiceImpl implements UomService {

    private final Logger log = LoggerFactory.getLogger(UomServiceImpl.class);

    private final UomRepository uomRepository;

    private final UomMapper uomMapper;

    private final UomSearchRepository uomSearchRepository;

    public UomServiceImpl(UomRepository uomRepository, UomMapper uomMapper, UomSearchRepository uomSearchRepository) {
        this.uomRepository = uomRepository;
        this.uomMapper = uomMapper;
        this.uomSearchRepository = uomSearchRepository;
    }

    /**
     * Save a uom.
     *
     * @param uomDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public UomDTO save(UomDTO uomDTO) {
        log.debug("Request to save Uom : {}", uomDTO);
        Uom uom = uomMapper.toEntity(uomDTO);
        uom = uomRepository.save(uom);
        UomDTO result = uomMapper.toDto(uom);
        uomSearchRepository.save(uom);
        return result;
    }

    /**
     * Get all the uoms.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UomDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Uoms");
        return uomRepository.findAll(pageable)
            .map(uomMapper::toDto);
    }


    /**
     * Get one uom by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UomDTO> findOne(Long id) {
        log.debug("Request to get Uom : {}", id);
        return uomRepository.findById(id)
            .map(uomMapper::toDto);
    }

    /**
     * Delete the uom by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Uom : {}", id);        uomRepository.deleteById(id);
        uomSearchRepository.deleteById(id);
    }

    /**
     * Search for the uom corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UomDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Uoms for query {}", query);
        return uomSearchRepository.search(queryStringQuery(query), pageable)
            .map(uomMapper::toDto);
    }
}
