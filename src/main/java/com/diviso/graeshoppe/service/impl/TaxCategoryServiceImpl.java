package com.diviso.graeshoppe.service.impl;

import com.diviso.graeshoppe.service.TaxCategoryService;
import com.diviso.graeshoppe.domain.TaxCategory;
import com.diviso.graeshoppe.repository.TaxCategoryRepository;
import com.diviso.graeshoppe.repository.search.TaxCategorySearchRepository;
import com.diviso.graeshoppe.service.dto.TaxCategoryDTO;
import com.diviso.graeshoppe.service.mapper.TaxCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TaxCategory.
 */
@Service
@Transactional
public class TaxCategoryServiceImpl implements TaxCategoryService {

    private final Logger log = LoggerFactory.getLogger(TaxCategoryServiceImpl.class);

    private final TaxCategoryRepository taxCategoryRepository;

    private final TaxCategoryMapper taxCategoryMapper;

    private final TaxCategorySearchRepository taxCategorySearchRepository;

    public TaxCategoryServiceImpl(TaxCategoryRepository taxCategoryRepository, TaxCategoryMapper taxCategoryMapper, TaxCategorySearchRepository taxCategorySearchRepository) {
        this.taxCategoryRepository = taxCategoryRepository;
        this.taxCategoryMapper = taxCategoryMapper;
        this.taxCategorySearchRepository = taxCategorySearchRepository;
    }

    /**
     * Save a taxCategory.
     *
     * @param taxCategoryDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TaxCategoryDTO save(TaxCategoryDTO taxCategoryDTO) {
        log.debug("Request to save TaxCategory : {}", taxCategoryDTO);
        TaxCategory taxCategory = taxCategoryMapper.toEntity(taxCategoryDTO);
        taxCategory = taxCategoryRepository.save(taxCategory);
        TaxCategoryDTO result = taxCategoryMapper.toDto(taxCategory);
        taxCategorySearchRepository.save(taxCategory);
        return result;
    }

    /**
     * Get all the taxCategories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TaxCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TaxCategories");
        return taxCategoryRepository.findAll(pageable)
            .map(taxCategoryMapper::toDto);
    }


    /**
     * Get one taxCategory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TaxCategoryDTO> findOne(Long id) {
        log.debug("Request to get TaxCategory : {}", id);
        return taxCategoryRepository.findById(id)
            .map(taxCategoryMapper::toDto);
    }

    /**
     * Delete the taxCategory by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TaxCategory : {}", id);        taxCategoryRepository.deleteById(id);
        taxCategorySearchRepository.deleteById(id);
    }

    /**
     * Search for the taxCategory corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TaxCategoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TaxCategories for query {}", query);
        return taxCategorySearchRepository.search(queryStringQuery(query), pageable)
            .map(taxCategoryMapper::toDto);
    }
}
