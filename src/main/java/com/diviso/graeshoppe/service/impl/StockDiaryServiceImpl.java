package com.diviso.graeshoppe.service.impl;

import com.diviso.graeshoppe.service.StockCurrentService;
import com.diviso.graeshoppe.service.StockDiaryService;
import com.diviso.graeshoppe.domain.StockCurrent;
import com.diviso.graeshoppe.domain.StockDiary;
import com.diviso.graeshoppe.repository.StockCurrentRepository;
import com.diviso.graeshoppe.repository.StockDiaryRepository;
import com.diviso.graeshoppe.repository.search.StockDiarySearchRepository;
import com.diviso.graeshoppe.service.dto.StockCurrentDTO;
import com.diviso.graeshoppe.service.dto.StockDiaryDTO;
import com.diviso.graeshoppe.service.mapper.StockDiaryMapper;
import com.diviso.graeshoppe.web.rest.errors.BadRequestAlertException;
import com.diviso.graeshoppe.web.rest.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URISyntaxException;
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
    private final StockCurrentService stockCurrentServiceImpl;
    

    private final StockDiaryMapper stockDiaryMapper;

    private final StockDiarySearchRepository stockDiarySearchRepository;

    public StockDiaryServiceImpl(StockCurrentService stockCurrentServiceImpl,StockCurrentRepository stockCurrentRepository,StockDiaryRepository stockDiaryRepository, StockDiaryMapper stockDiaryMapper, StockDiarySearchRepository stockDiarySearchRepository) {
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
        //stockDiarySearchRepository.save(stockDiary);
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
    public StockDiaryDTO updateStockDiary(StockDiaryDTO stockDiaryDTO){
        log.debug("REST request to update StockDiary : {}", stockDiaryDTO);
        if (stockDiaryDTO.getId() == null) {
        	System.out.println("<<<<<<<<<<<<<<<EXCEPTION>>>>>>>>>>>>>>>>>>>>>>>>");
        }
        System.out.println("<<<<<<<<<<<<<<<update stock diary>>>>>>>>>>>>>>>>>>>>>>>>");
        return save(stockDiaryDTO);
    }
	
	
	@Override
	public StockDiaryDTO createStockOfProduct(StockDiaryDTO stockDiaryDTO) {
		
			
		StockDiaryDTO savedStockDiaryDTO=save(stockDiaryDTO);//stockDiary saved
		 savedStockDiaryDTO=updateStockDiary(savedStockDiaryDTO);
	
		StockCurrentDTO stockCurrentDTO=stockCurrentServiceImpl.findByProductId(savedStockDiaryDTO.getProductId()).get();
		stockCurrentDTO.setUnits(stockCurrentDTO.getUnits()+savedStockDiaryDTO.getUnits());//stock units addition
		StockCurrentDTO savedStockCurrent=stockCurrentServiceImpl.save(stockCurrentDTO);//stock current saved
		savedStockCurrent=stockCurrentServiceImpl.updateStockCurrent(savedStockCurrent);//stock current saved
		
		return savedStockDiaryDTO;
	}
}
