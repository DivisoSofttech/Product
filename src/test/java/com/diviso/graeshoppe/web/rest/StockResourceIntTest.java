package com.diviso.graeshoppe.web.rest;

import com.diviso.graeshoppe.ProductmicroserviceApp;

import com.diviso.graeshoppe.domain.Stock;
import com.diviso.graeshoppe.repository.StockRepository;
import com.diviso.graeshoppe.repository.search.StockSearchRepository;
import com.diviso.graeshoppe.service.StockService;
import com.diviso.graeshoppe.service.dto.StockDTO;
import com.diviso.graeshoppe.service.mapper.StockMapper;
import com.diviso.graeshoppe.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static com.diviso.graeshoppe.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the StockResource REST controller.
 *
 * @see StockResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductmicroserviceApp.class)
public class StockResourceIntTest {

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final Long DEFAULT_DELIVERY_NOTE_REF = 1L;
    private static final Long UPDATED_DELIVERY_NOTE_REF = 2L;

    private static final LocalDate DEFAULT_DATE_OF_STOCK_UPDATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_STOCK_UPDATED = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_STORAGE_COST = 1D;
    private static final Double UPDATED_STORAGE_COST = 2D;

    @Autowired
    private StockRepository stockRepository;

    @Mock
    private StockRepository stockRepositoryMock;

    @Autowired
    private StockMapper stockMapper;

    @Mock
    private StockService stockServiceMock;

    @Autowired
    private StockService stockService;

    /**
     * This repository is mocked in the com.diviso.graeshoppe.repository.search test package.
     *
     * @see com.diviso.graeshoppe.repository.search.StockSearchRepositoryMockConfiguration
     */
    @Autowired
    private StockSearchRepository mockStockSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restStockMockMvc;

    private Stock stock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StockResource stockResource = new StockResource(stockService);
        this.restStockMockMvc = MockMvcBuilders.standaloneSetup(stockResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stock createEntity(EntityManager em) {
        Stock stock = new Stock()
            .reference(DEFAULT_REFERENCE)
            .deliveryNoteRef(DEFAULT_DELIVERY_NOTE_REF)
            .dateOfStockUpdated(DEFAULT_DATE_OF_STOCK_UPDATED)
            .storageCost(DEFAULT_STORAGE_COST);
        return stock;
    }

    @Before
    public void initTest() {
        stock = createEntity(em);
    }

    @Test
    @Transactional
    public void createStock() throws Exception {
        int databaseSizeBeforeCreate = stockRepository.findAll().size();

        // Create the Stock
        StockDTO stockDTO = stockMapper.toDto(stock);
        restStockMockMvc.perform(post("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockDTO)))
            .andExpect(status().isCreated());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeCreate + 1);
        Stock testStock = stockList.get(stockList.size() - 1);
        assertThat(testStock.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testStock.getDeliveryNoteRef()).isEqualTo(DEFAULT_DELIVERY_NOTE_REF);
        assertThat(testStock.getDateOfStockUpdated()).isEqualTo(DEFAULT_DATE_OF_STOCK_UPDATED);
        assertThat(testStock.getStorageCost()).isEqualTo(DEFAULT_STORAGE_COST);

        // Validate the Stock in Elasticsearch
        verify(mockStockSearchRepository, times(1)).save(testStock);
    }

    @Test
    @Transactional
    public void createStockWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockRepository.findAll().size();

        // Create the Stock with an existing ID
        stock.setId(1L);
        StockDTO stockDTO = stockMapper.toDto(stock);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockMockMvc.perform(post("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeCreate);

        // Validate the Stock in Elasticsearch
        verify(mockStockSearchRepository, times(0)).save(stock);
    }

    @Test
    @Transactional
    public void checkReferenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockRepository.findAll().size();
        // set the field null
        stock.setReference(null);

        // Create the Stock, which fails.
        StockDTO stockDTO = stockMapper.toDto(stock);

        restStockMockMvc.perform(post("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockDTO)))
            .andExpect(status().isBadRequest());

        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStocks() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList
        restStockMockMvc.perform(get("/api/stocks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stock.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE.toString())))
            .andExpect(jsonPath("$.[*].deliveryNoteRef").value(hasItem(DEFAULT_DELIVERY_NOTE_REF.intValue())))
            .andExpect(jsonPath("$.[*].dateOfStockUpdated").value(hasItem(DEFAULT_DATE_OF_STOCK_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].storageCost").value(hasItem(DEFAULT_STORAGE_COST.doubleValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllStocksWithEagerRelationshipsIsEnabled() throws Exception {
        StockResource stockResource = new StockResource(stockServiceMock);
        when(stockServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restStockMockMvc = MockMvcBuilders.standaloneSetup(stockResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restStockMockMvc.perform(get("/api/stocks?eagerload=true"))
        .andExpect(status().isOk());

        verify(stockServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllStocksWithEagerRelationshipsIsNotEnabled() throws Exception {
        StockResource stockResource = new StockResource(stockServiceMock);
            when(stockServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restStockMockMvc = MockMvcBuilders.standaloneSetup(stockResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restStockMockMvc.perform(get("/api/stocks?eagerload=true"))
        .andExpect(status().isOk());

            verify(stockServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getStock() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get the stock
        restStockMockMvc.perform(get("/api/stocks/{id}", stock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stock.getId().intValue()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE.toString()))
            .andExpect(jsonPath("$.deliveryNoteRef").value(DEFAULT_DELIVERY_NOTE_REF.intValue()))
            .andExpect(jsonPath("$.dateOfStockUpdated").value(DEFAULT_DATE_OF_STOCK_UPDATED.toString()))
            .andExpect(jsonPath("$.storageCost").value(DEFAULT_STORAGE_COST.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingStock() throws Exception {
        // Get the stock
        restStockMockMvc.perform(get("/api/stocks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStock() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        int databaseSizeBeforeUpdate = stockRepository.findAll().size();

        // Update the stock
        Stock updatedStock = stockRepository.findById(stock.getId()).get();
        // Disconnect from session so that the updates on updatedStock are not directly saved in db
        em.detach(updatedStock);
        updatedStock
            .reference(UPDATED_REFERENCE)
            .deliveryNoteRef(UPDATED_DELIVERY_NOTE_REF)
            .dateOfStockUpdated(UPDATED_DATE_OF_STOCK_UPDATED)
            .storageCost(UPDATED_STORAGE_COST);
        StockDTO stockDTO = stockMapper.toDto(updatedStock);

        restStockMockMvc.perform(put("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockDTO)))
            .andExpect(status().isOk());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
        Stock testStock = stockList.get(stockList.size() - 1);
        assertThat(testStock.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testStock.getDeliveryNoteRef()).isEqualTo(UPDATED_DELIVERY_NOTE_REF);
        assertThat(testStock.getDateOfStockUpdated()).isEqualTo(UPDATED_DATE_OF_STOCK_UPDATED);
        assertThat(testStock.getStorageCost()).isEqualTo(UPDATED_STORAGE_COST);

        // Validate the Stock in Elasticsearch
        verify(mockStockSearchRepository, times(1)).save(testStock);
    }

    @Test
    @Transactional
    public void updateNonExistingStock() throws Exception {
        int databaseSizeBeforeUpdate = stockRepository.findAll().size();

        // Create the Stock
        StockDTO stockDTO = stockMapper.toDto(stock);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockMockMvc.perform(put("/api/stocks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Stock in Elasticsearch
        verify(mockStockSearchRepository, times(0)).save(stock);
    }

    @Test
    @Transactional
    public void deleteStock() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        int databaseSizeBeforeDelete = stockRepository.findAll().size();

        // Delete the stock
        restStockMockMvc.perform(delete("/api/stocks/{id}", stock.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Stock in Elasticsearch
        verify(mockStockSearchRepository, times(1)).deleteById(stock.getId());
    }

    @Test
    @Transactional
    public void searchStock() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);
        when(mockStockSearchRepository.search(queryStringQuery("id:" + stock.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(stock), PageRequest.of(0, 1), 1));
        // Search the stock
        restStockMockMvc.perform(get("/api/_search/stocks?query=id:" + stock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stock.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].deliveryNoteRef").value(hasItem(DEFAULT_DELIVERY_NOTE_REF.intValue())))
            .andExpect(jsonPath("$.[*].dateOfStockUpdated").value(hasItem(DEFAULT_DATE_OF_STOCK_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].storageCost").value(hasItem(DEFAULT_STORAGE_COST.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Stock.class);
        Stock stock1 = new Stock();
        stock1.setId(1L);
        Stock stock2 = new Stock();
        stock2.setId(stock1.getId());
        assertThat(stock1).isEqualTo(stock2);
        stock2.setId(2L);
        assertThat(stock1).isNotEqualTo(stock2);
        stock1.setId(null);
        assertThat(stock1).isNotEqualTo(stock2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockDTO.class);
        StockDTO stockDTO1 = new StockDTO();
        stockDTO1.setId(1L);
        StockDTO stockDTO2 = new StockDTO();
        assertThat(stockDTO1).isNotEqualTo(stockDTO2);
        stockDTO2.setId(stockDTO1.getId());
        assertThat(stockDTO1).isEqualTo(stockDTO2);
        stockDTO2.setId(2L);
        assertThat(stockDTO1).isNotEqualTo(stockDTO2);
        stockDTO1.setId(null);
        assertThat(stockDTO1).isNotEqualTo(stockDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(stockMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(stockMapper.fromId(null)).isNull();
    }
}
