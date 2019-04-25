package com.diviso.graeshoppe.web.rest;

import com.diviso.graeshoppe.ProductmicroserviceApp;

import com.diviso.graeshoppe.domain.StockLine;
import com.diviso.graeshoppe.repository.StockLineRepository;
import com.diviso.graeshoppe.repository.search.StockLineSearchRepository;
import com.diviso.graeshoppe.service.StockLineService;
import com.diviso.graeshoppe.service.dto.StockLineDTO;
import com.diviso.graeshoppe.service.mapper.StockLineMapper;
import com.diviso.graeshoppe.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
 * Test class for the StockLineResource REST controller.
 *
 * @see StockLineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductmicroserviceApp.class)
public class StockLineResourceIntTest {

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final Double DEFAULT_BUY_PRICE = 1D;
    private static final Double UPDATED_BUY_PRICE = 2D;

    private static final Double DEFAULT_SELL_PRICE_INCLUSIVE = 1D;
    private static final Double UPDATED_SELL_PRICE_INCLUSIVE = 2D;

    private static final Double DEFAULT_SELL_PRICE_EXCLUSIVE = 1D;
    private static final Double UPDATED_SELL_PRICE_EXCLUSIVE = 2D;

    private static final Double DEFAULT_GROSS_PROFIT = 1D;
    private static final Double UPDATED_GROSS_PROFIT = 2D;

    private static final Double DEFAULT_MARGIN = 1D;
    private static final Double UPDATED_MARGIN = 2D;

    private static final Double DEFAULT_UNITS = 1D;
    private static final Double UPDATED_UNITS = 2D;

    private static final Long DEFAULT_INFRASTRUCTURE_ID = 1L;
    private static final Long UPDATED_INFRASTRUCTURE_ID = 2L;

    private static final String DEFAULT_LOCATION_ID = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION_ID = "BBBBBBBBBB";

    private static final Long DEFAULT_SUPPLIER_REF = 1L;
    private static final Long UPDATED_SUPPLIER_REF = 2L;

    @Autowired
    private StockLineRepository stockLineRepository;

    @Autowired
    private StockLineMapper stockLineMapper;

    @Autowired
    private StockLineService stockLineService;

    /**
     * This repository is mocked in the com.diviso.graeshoppe.repository.search test package.
     *
     * @see com.diviso.graeshoppe.repository.search.StockLineSearchRepositoryMockConfiguration
     */
    @Autowired
    private StockLineSearchRepository mockStockLineSearchRepository;

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

    private MockMvc restStockLineMockMvc;

    private StockLine stockLine;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StockLineResource stockLineResource = new StockLineResource(stockLineService);
        this.restStockLineMockMvc = MockMvcBuilders.standaloneSetup(stockLineResource)
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
    public static StockLine createEntity(EntityManager em) {
        StockLine stockLine = new StockLine()
            .reference(DEFAULT_REFERENCE)
            .buyPrice(DEFAULT_BUY_PRICE)
            .sellPriceInclusive(DEFAULT_SELL_PRICE_INCLUSIVE)
            .sellPriceExclusive(DEFAULT_SELL_PRICE_EXCLUSIVE)
            .grossProfit(DEFAULT_GROSS_PROFIT)
            .margin(DEFAULT_MARGIN)
            .units(DEFAULT_UNITS)
            .infrastructureId(DEFAULT_INFRASTRUCTURE_ID)
            .locationId(DEFAULT_LOCATION_ID)
            .supplierRef(DEFAULT_SUPPLIER_REF);
        return stockLine;
    }

    @Before
    public void initTest() {
        stockLine = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockLine() throws Exception {
        int databaseSizeBeforeCreate = stockLineRepository.findAll().size();

        // Create the StockLine
        StockLineDTO stockLineDTO = stockLineMapper.toDto(stockLine);
        restStockLineMockMvc.perform(post("/api/stock-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockLineDTO)))
            .andExpect(status().isCreated());

        // Validate the StockLine in the database
        List<StockLine> stockLineList = stockLineRepository.findAll();
        assertThat(stockLineList).hasSize(databaseSizeBeforeCreate + 1);
        StockLine testStockLine = stockLineList.get(stockLineList.size() - 1);
        assertThat(testStockLine.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testStockLine.getBuyPrice()).isEqualTo(DEFAULT_BUY_PRICE);
        assertThat(testStockLine.getSellPriceInclusive()).isEqualTo(DEFAULT_SELL_PRICE_INCLUSIVE);
        assertThat(testStockLine.getSellPriceExclusive()).isEqualTo(DEFAULT_SELL_PRICE_EXCLUSIVE);
        assertThat(testStockLine.getGrossProfit()).isEqualTo(DEFAULT_GROSS_PROFIT);
        assertThat(testStockLine.getMargin()).isEqualTo(DEFAULT_MARGIN);
        assertThat(testStockLine.getUnits()).isEqualTo(DEFAULT_UNITS);
        assertThat(testStockLine.getInfrastructureId()).isEqualTo(DEFAULT_INFRASTRUCTURE_ID);
        assertThat(testStockLine.getLocationId()).isEqualTo(DEFAULT_LOCATION_ID);
        assertThat(testStockLine.getSupplierRef()).isEqualTo(DEFAULT_SUPPLIER_REF);

        // Validate the StockLine in Elasticsearch
        verify(mockStockLineSearchRepository, times(1)).save(testStockLine);
    }

    @Test
    @Transactional
    public void createStockLineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockLineRepository.findAll().size();

        // Create the StockLine with an existing ID
        stockLine.setId(1L);
        StockLineDTO stockLineDTO = stockLineMapper.toDto(stockLine);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockLineMockMvc.perform(post("/api/stock-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockLineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StockLine in the database
        List<StockLine> stockLineList = stockLineRepository.findAll();
        assertThat(stockLineList).hasSize(databaseSizeBeforeCreate);

        // Validate the StockLine in Elasticsearch
        verify(mockStockLineSearchRepository, times(0)).save(stockLine);
    }

    @Test
    @Transactional
    public void checkReferenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockLineRepository.findAll().size();
        // set the field null
        stockLine.setReference(null);

        // Create the StockLine, which fails.
        StockLineDTO stockLineDTO = stockLineMapper.toDto(stockLine);

        restStockLineMockMvc.perform(post("/api/stock-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockLineDTO)))
            .andExpect(status().isBadRequest());

        List<StockLine> stockLineList = stockLineRepository.findAll();
        assertThat(stockLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUnitsIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockLineRepository.findAll().size();
        // set the field null
        stockLine.setUnits(null);

        // Create the StockLine, which fails.
        StockLineDTO stockLineDTO = stockLineMapper.toDto(stockLine);

        restStockLineMockMvc.perform(post("/api/stock-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockLineDTO)))
            .andExpect(status().isBadRequest());

        List<StockLine> stockLineList = stockLineRepository.findAll();
        assertThat(stockLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockLines() throws Exception {
        // Initialize the database
        stockLineRepository.saveAndFlush(stockLine);

        // Get all the stockLineList
        restStockLineMockMvc.perform(get("/api/stock-lines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE.toString())))
            .andExpect(jsonPath("$.[*].buyPrice").value(hasItem(DEFAULT_BUY_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].sellPriceInclusive").value(hasItem(DEFAULT_SELL_PRICE_INCLUSIVE.doubleValue())))
            .andExpect(jsonPath("$.[*].sellPriceExclusive").value(hasItem(DEFAULT_SELL_PRICE_EXCLUSIVE.doubleValue())))
            .andExpect(jsonPath("$.[*].grossProfit").value(hasItem(DEFAULT_GROSS_PROFIT.doubleValue())))
            .andExpect(jsonPath("$.[*].margin").value(hasItem(DEFAULT_MARGIN.doubleValue())))
            .andExpect(jsonPath("$.[*].units").value(hasItem(DEFAULT_UNITS.doubleValue())))
            .andExpect(jsonPath("$.[*].infrastructureId").value(hasItem(DEFAULT_INFRASTRUCTURE_ID.intValue())))
            .andExpect(jsonPath("$.[*].locationId").value(hasItem(DEFAULT_LOCATION_ID.toString())))
            .andExpect(jsonPath("$.[*].supplierRef").value(hasItem(DEFAULT_SUPPLIER_REF.intValue())));
    }
    
    @Test
    @Transactional
    public void getStockLine() throws Exception {
        // Initialize the database
        stockLineRepository.saveAndFlush(stockLine);

        // Get the stockLine
        restStockLineMockMvc.perform(get("/api/stock-lines/{id}", stockLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockLine.getId().intValue()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE.toString()))
            .andExpect(jsonPath("$.buyPrice").value(DEFAULT_BUY_PRICE.doubleValue()))
            .andExpect(jsonPath("$.sellPriceInclusive").value(DEFAULT_SELL_PRICE_INCLUSIVE.doubleValue()))
            .andExpect(jsonPath("$.sellPriceExclusive").value(DEFAULT_SELL_PRICE_EXCLUSIVE.doubleValue()))
            .andExpect(jsonPath("$.grossProfit").value(DEFAULT_GROSS_PROFIT.doubleValue()))
            .andExpect(jsonPath("$.margin").value(DEFAULT_MARGIN.doubleValue()))
            .andExpect(jsonPath("$.units").value(DEFAULT_UNITS.doubleValue()))
            .andExpect(jsonPath("$.infrastructureId").value(DEFAULT_INFRASTRUCTURE_ID.intValue()))
            .andExpect(jsonPath("$.locationId").value(DEFAULT_LOCATION_ID.toString()))
            .andExpect(jsonPath("$.supplierRef").value(DEFAULT_SUPPLIER_REF.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingStockLine() throws Exception {
        // Get the stockLine
        restStockLineMockMvc.perform(get("/api/stock-lines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockLine() throws Exception {
        // Initialize the database
        stockLineRepository.saveAndFlush(stockLine);

        int databaseSizeBeforeUpdate = stockLineRepository.findAll().size();

        // Update the stockLine
        StockLine updatedStockLine = stockLineRepository.findById(stockLine.getId()).get();
        // Disconnect from session so that the updates on updatedStockLine are not directly saved in db
        em.detach(updatedStockLine);
        updatedStockLine
            .reference(UPDATED_REFERENCE)
            .buyPrice(UPDATED_BUY_PRICE)
            .sellPriceInclusive(UPDATED_SELL_PRICE_INCLUSIVE)
            .sellPriceExclusive(UPDATED_SELL_PRICE_EXCLUSIVE)
            .grossProfit(UPDATED_GROSS_PROFIT)
            .margin(UPDATED_MARGIN)
            .units(UPDATED_UNITS)
            .infrastructureId(UPDATED_INFRASTRUCTURE_ID)
            .locationId(UPDATED_LOCATION_ID)
            .supplierRef(UPDATED_SUPPLIER_REF);
        StockLineDTO stockLineDTO = stockLineMapper.toDto(updatedStockLine);

        restStockLineMockMvc.perform(put("/api/stock-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockLineDTO)))
            .andExpect(status().isOk());

        // Validate the StockLine in the database
        List<StockLine> stockLineList = stockLineRepository.findAll();
        assertThat(stockLineList).hasSize(databaseSizeBeforeUpdate);
        StockLine testStockLine = stockLineList.get(stockLineList.size() - 1);
        assertThat(testStockLine.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testStockLine.getBuyPrice()).isEqualTo(UPDATED_BUY_PRICE);
        assertThat(testStockLine.getSellPriceInclusive()).isEqualTo(UPDATED_SELL_PRICE_INCLUSIVE);
        assertThat(testStockLine.getSellPriceExclusive()).isEqualTo(UPDATED_SELL_PRICE_EXCLUSIVE);
        assertThat(testStockLine.getGrossProfit()).isEqualTo(UPDATED_GROSS_PROFIT);
        assertThat(testStockLine.getMargin()).isEqualTo(UPDATED_MARGIN);
        assertThat(testStockLine.getUnits()).isEqualTo(UPDATED_UNITS);
        assertThat(testStockLine.getInfrastructureId()).isEqualTo(UPDATED_INFRASTRUCTURE_ID);
        assertThat(testStockLine.getLocationId()).isEqualTo(UPDATED_LOCATION_ID);
        assertThat(testStockLine.getSupplierRef()).isEqualTo(UPDATED_SUPPLIER_REF);

        // Validate the StockLine in Elasticsearch
        verify(mockStockLineSearchRepository, times(1)).save(testStockLine);
    }

    @Test
    @Transactional
    public void updateNonExistingStockLine() throws Exception {
        int databaseSizeBeforeUpdate = stockLineRepository.findAll().size();

        // Create the StockLine
        StockLineDTO stockLineDTO = stockLineMapper.toDto(stockLine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockLineMockMvc.perform(put("/api/stock-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockLineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StockLine in the database
        List<StockLine> stockLineList = stockLineRepository.findAll();
        assertThat(stockLineList).hasSize(databaseSizeBeforeUpdate);

        // Validate the StockLine in Elasticsearch
        verify(mockStockLineSearchRepository, times(0)).save(stockLine);
    }

    @Test
    @Transactional
    public void deleteStockLine() throws Exception {
        // Initialize the database
        stockLineRepository.saveAndFlush(stockLine);

        int databaseSizeBeforeDelete = stockLineRepository.findAll().size();

        // Delete the stockLine
        restStockLineMockMvc.perform(delete("/api/stock-lines/{id}", stockLine.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockLine> stockLineList = stockLineRepository.findAll();
        assertThat(stockLineList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the StockLine in Elasticsearch
        verify(mockStockLineSearchRepository, times(1)).deleteById(stockLine.getId());
    }

    @Test
    @Transactional
    public void searchStockLine() throws Exception {
        // Initialize the database
        stockLineRepository.saveAndFlush(stockLine);
        when(mockStockLineSearchRepository.search(queryStringQuery("id:" + stockLine.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(stockLine), PageRequest.of(0, 1), 1));
        // Search the stockLine
        restStockLineMockMvc.perform(get("/api/_search/stock-lines?query=id:" + stockLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].buyPrice").value(hasItem(DEFAULT_BUY_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].sellPriceInclusive").value(hasItem(DEFAULT_SELL_PRICE_INCLUSIVE.doubleValue())))
            .andExpect(jsonPath("$.[*].sellPriceExclusive").value(hasItem(DEFAULT_SELL_PRICE_EXCLUSIVE.doubleValue())))
            .andExpect(jsonPath("$.[*].grossProfit").value(hasItem(DEFAULT_GROSS_PROFIT.doubleValue())))
            .andExpect(jsonPath("$.[*].margin").value(hasItem(DEFAULT_MARGIN.doubleValue())))
            .andExpect(jsonPath("$.[*].units").value(hasItem(DEFAULT_UNITS.doubleValue())))
            .andExpect(jsonPath("$.[*].infrastructureId").value(hasItem(DEFAULT_INFRASTRUCTURE_ID.intValue())))
            .andExpect(jsonPath("$.[*].locationId").value(hasItem(DEFAULT_LOCATION_ID)))
            .andExpect(jsonPath("$.[*].supplierRef").value(hasItem(DEFAULT_SUPPLIER_REF.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockLine.class);
        StockLine stockLine1 = new StockLine();
        stockLine1.setId(1L);
        StockLine stockLine2 = new StockLine();
        stockLine2.setId(stockLine1.getId());
        assertThat(stockLine1).isEqualTo(stockLine2);
        stockLine2.setId(2L);
        assertThat(stockLine1).isNotEqualTo(stockLine2);
        stockLine1.setId(null);
        assertThat(stockLine1).isNotEqualTo(stockLine2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockLineDTO.class);
        StockLineDTO stockLineDTO1 = new StockLineDTO();
        stockLineDTO1.setId(1L);
        StockLineDTO stockLineDTO2 = new StockLineDTO();
        assertThat(stockLineDTO1).isNotEqualTo(stockLineDTO2);
        stockLineDTO2.setId(stockLineDTO1.getId());
        assertThat(stockLineDTO1).isEqualTo(stockLineDTO2);
        stockLineDTO2.setId(2L);
        assertThat(stockLineDTO1).isNotEqualTo(stockLineDTO2);
        stockLineDTO1.setId(null);
        assertThat(stockLineDTO1).isNotEqualTo(stockLineDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(stockLineMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(stockLineMapper.fromId(null)).isNull();
    }
}
