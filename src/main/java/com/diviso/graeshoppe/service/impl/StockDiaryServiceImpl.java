package com.diviso.graeshoppe.service.impl;

import com.diviso.graeshoppe.service.StockDiaryService;
import com.diviso.graeshoppe.domain.StockCurrent;
import com.diviso.graeshoppe.domain.StockDiary;
import com.diviso.graeshoppe.repository.StockCurrentRepository;
import com.diviso.graeshoppe.repository.StockDiaryRepository;
import com.diviso.graeshoppe.repository.search.StockDiarySearchRepository;
import com.diviso.graeshoppe.service.dto.StockCurrentDTO;
import com.diviso.graeshoppe.service.dto.StockDiaryDTO;
import com.diviso.graeshoppe.service.mapper.StockDiaryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing StockDiary.
 */
@Service
@Transactional
public class StockDiaryServiceImpl implements StockDiaryService {

    private final Logger log = LoggerFactory.getLogger(StockDiaryServiceImpl.class);

    private final StockDiaryRepository stockDiaryRepository;
    private final StockCurrentRepository stockCurrentRepository;
    private final StockCurrentServiceImpl stockCurrentServiceImpl;
    

    private final StockDiaryMapper stockDiaryMapper;

    private final StockDiarySearchRepository stockDiarySearchRepository;

    public StockDiaryServiceImpl(StockCurrentServiceImpl stockCurrentServiceImpl,StockCurrentRepository stockCurrentRepository,StockDiaryRepository stockDiaryRepository, StockDiaryMapper stockDiaryMapper, StockDiarySearchRepository stockDiarySearchRepository) {
        this.stockDiaryRepository = stockDiaryRepository;
        this.stockDiaryMapper = stockDiaryMapper;
        this.stockCurrentRepository=stockCurrentRepository;
        this.stockDiarySearchRepository = stockDiarySearchRepository;
        this.stockCurrentServiceImpl=stockCurrentServiceImpl;
    }

    /**
     * Save a stockDiary.
     *
     * @param stockDiaryDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public StockDiaryDTO save(StockDiaryDTO stockDiaryDTO) {
        log.debug("Request to save StockDiary : {}", stockDiaryDTO);
        StockDiary stockDiary = stockDiaryMapper.toEntity(stockDiaryDTO);
        stockDiary = stockDiaryRepository.save(stockDiary);
        StockDiaryDTO result = stockDiaryMapper.toDto(stockDiary);
        stockDiarySearchRepository.save(stockDiary);
        stockDiarySearchRepository.save(stockDiary);
        return result;
    }

    /**
     * Get all the stockDiaries.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockDiaryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockDiaries");
        return stockDiaryRepository.findAll(pageable)
            .map(stockDiaryMapper::toDto);
    }


    /**
     * Get one stockDiary by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<StockDiaryDTO> findOne(Long id) {
        log.debug("Request to get StockDiary : {}", id);
        return stockDiaryRepository.findById(id)
            .map(stockDiaryMapper::toDto);
    }

    /**
     * Delete the stockDiary by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StockDiary : {}", id);        stockDiaryRepository.deleteById(id);
        stockDiarySearchRepository.deleteById(id);
    }

    /**
     * Search for the stockDiary corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StockDiaryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of StockDiaries for query {}", query);
        return stockDiarySearchRepository.search(queryStringQuery(query), pageable)
            .map(stockDiaryMapper::toDto);
    }

	@Override
	public Page<StockDiaryDTO> findByProductId(Long id, Pageable pageable) {
		// TODO Auto-generated method stub
		return stockDiaryRepository.findByProductId(id,pageable)
        .map(stockDiaryMapper::toDto);
	}

	@Override
	public StockDiaryDTO createStockOfProduct(StockDiaryDTO stockDiaryDTO) {
		
		Pageable pageable=null;
		Page<StockDiaryDTO> stockPage= findByProductId(stockDiaryDTO.getProductId(), pageable);
		StockDiaryDTO savedStockDiaryDTO=null;
		StockCurrentDTO stockCurrentDTO=new StockCurrentDTO();
		StockCurrentDTO savedStockCurrent=null;
		if(!stockPage.hasContent())
		{
			savedStockDiaryDTO=save(stockDiaryDTO);//stockDiary saved
			stockCurrentDTO.setProductId(savedStockDiaryDTO.getProductId());
			stockCurrentDTO.setUnits(savedStockDiaryDTO.getUnits());
			savedStockCurrent=stockCurrentServiceImpl.save(stockCurrentDTO);//stock current saved
		}
		else
		{
			savedStockDiaryDTO=save(stockDiaryDTO);//stockDiary saved
			stockCurrentDTO=stockCurrentServiceImpl.findByProductId(savedStockDiaryDTO.getProductId()).get();
			stockCurrentDTO.setUnits(stockCurrentDTO.getUnits()+savedStockDiaryDTO.getUnits());//stock units addition
			savedStockCurrent=stockCurrentServiceImpl.save(stockCurrentDTO);//stock current saved
			
		}
		
		
		
		
		return savedStockDiaryDTO;
	}
}
