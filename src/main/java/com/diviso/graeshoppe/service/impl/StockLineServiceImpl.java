package com.diviso.graeshoppe.service.impl;

import com.diviso.graeshoppe.service.StockLineService;
import com.diviso.graeshoppe.domain.StockLine;
import com.diviso.graeshoppe.repository.StockLineRepository;
import com.diviso.graeshoppe.repository.search.StockLineSearchRepository;
import com.diviso.graeshoppe.service.dto.StockLineDTO;
import com.diviso.graeshoppe.service.mapper.StockLineMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing StockLine.
 */
@Service
@Transactional
public class StockLineServiceImpl implements StockLineService {

    private final Logger log = LoggerFactory.getLogger(StockLineServiceImpl.class);

    private final StockLineRepository stockLineRepository;

    private final StockLineMapper stockLineMapper;

    private final StockLineSearchRepository stockLineSearchRepository;

    public StockLineServiceImpl(StockLineRepository stockLineRepository, StockLineMapper stockLineMapper, StockLineSearchRepository stockLineSearchRepository) {
        this.stockLineRepository = stockLineRepository;
        this.stockLineMapper = stockLineMapper;
        this.stockLineSearchRepository = stockLineSearchRepository;
    }

    /**
     * Save a stockLine.
     *
     * @param stockLineDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockLineDTO save(StockLineDTO stockLineDTO) {
        log.debug("Request to save StockLine : {}", stockLineDTO);
        StockLine stockLine = stockLineMapper.toEntity(stockLineDTO);
        stockLine = stockLineRepository.save(stockLine);
        StockLineDTO result = stockLineMapper.toDto(stockLine);
        stockLineSearchRepository.save(stockLine);
        return result;
    }

    /**
     * Get all the stockLines.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockLineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockLines");
        return stockLineRepository.findAll(pageable)
            .map(stockLineMapper::toDto);
    }


    /**
     * Get one stockLine by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<StockLineDTO> findOne(Long id) {
        log.debug("Request to get StockLine : {}", id);
        return stockLineRepository.findById(id)
            .map(stockLineMapper::toDto);
    }

    /**
     * Delete the stockLine by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockLine : {}", id);        stockLineRepository.deleteById(id);
        stockLineSearchRepository.deleteById(id);
    }

    /**
     * Search for the stockLine corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockLineDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of StockLines for query {}", query);
        return stockLineSearchRepository.search(queryStringQuery(query), pageable)
            .map(stockLineMapper::toDto);
    }
}
