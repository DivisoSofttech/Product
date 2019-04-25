package com.diviso.graeshoppe.service.impl;

import com.diviso.graeshoppe.service.LabelService;
import com.diviso.graeshoppe.domain.Label;
import com.diviso.graeshoppe.repository.LabelRepository;
import com.diviso.graeshoppe.repository.search.LabelSearchRepository;
import com.diviso.graeshoppe.service.dto.LabelDTO;
import com.diviso.graeshoppe.service.mapper.LabelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Label.
 */
@Service
@Transactional
public class LabelServiceImpl implements LabelService {

    private final Logger log = LoggerFactory.getLogger(LabelServiceImpl.class);

    private final LabelRepository labelRepository;

    private final LabelMapper labelMapper;

    private final LabelSearchRepository labelSearchRepository;

    public LabelServiceImpl(LabelRepository labelRepository, LabelMapper labelMapper, LabelSearchRepository labelSearchRepository) {
        this.labelRepository = labelRepository;
        this.labelMapper = labelMapper;
        this.labelSearchRepository = labelSearchRepository;
    }

    /**
     * Save a label.
     *
     * @param labelDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public LabelDTO save(LabelDTO labelDTO) {
        log.debug("Request to save Label : {}", labelDTO);
        Label label = labelMapper.toEntity(labelDTO);
        label = labelRepository.save(label);
        LabelDTO result = labelMapper.toDto(label);
        labelSearchRepository.save(label);
        return result;
    }

    /**
     * Get all the labels.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LabelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Labels");
        return labelRepository.findAll(pageable)
            .map(labelMapper::toDto);
    }


    /**
     * Get one label by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<LabelDTO> findOne(Long id) {
        log.debug("Request to get Label : {}", id);
        return labelRepository.findById(id)
            .map(labelMapper::toDto);
    }

    /**
     * Delete the label by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Label : {}", id);        labelRepository.deleteById(id);
        labelSearchRepository.deleteById(id);
    }

    /**
     * Search for the label corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LabelDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Labels for query {}", query);
        return labelSearchRepository.search(queryStringQuery(query), pageable)
            .map(labelMapper::toDto);
    }
}
