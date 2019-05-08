package com.diviso.graeshoppe.web.rest;

import com.diviso.graeshoppe.ProductmicroserviceApp;

import com.diviso.graeshoppe.domain.StockDiary;
import com.diviso.graeshoppe.repository.StockDiaryRepository;
import com.diviso.graeshoppe.repository.search.StockDiarySearchRepository;
import com.diviso.graeshoppe.service.StockDiaryService;
import com.diviso.graeshoppe.service.dto.StockDiaryDTO;
import com.diviso.graeshoppe.service.mapper.StockDiaryMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Test class for the StockDiaryResource REST controller.
 *
 * @see StockDiaryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductmicroserviceApp.class)
public class StockDiaryResourceIntTest {

    private static final LocalDate DEFAULT_DATE_OF_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_CREATION = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_UNITS = 1D;
    private static final Double UPDATED_UNITS = 2D;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Boolean DEFAULT_IS_BUY = false;
    private static final Boolean UPDATED_IS_BUY = true;

    @Autowired
    private StockDiaryRepository stockDiaryRepository;

    @Autowired
    private StockDiaryMapper stockDiaryMapper;

    @Autowired
    private StockDiaryService stockDiaryService;

    /**
     * This repository is mocked in the com.diviso.graeshoppe.repository.search test package.
     *
     * @see com.diviso.graeshoppe.repository.search.StockDiarySearchRepositoryMockConfiguration
     */
    @Autowired
    private StockDiarySearchRepository mockStockDiarySearchRepository;

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

    private MockMvc restStockDiaryMockMvc;

    private StockDiary stockDiary;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StockDiaryResource stockDiaryResource = new StockDiaryResource(stockDiaryService);
        this.restStockDiaryMockMvc = MockMvcBuilders.standaloneSetup(stockDiaryResource)
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
    public static StockDiary createEntity(EntityManager em) {
        StockDiary stockDiary = new StockDiary()
            .dateOfCreation(DEFAULT_DATE_OF_CREATION)
            .units(DEFAULT_UNITS)
            .price(DEFAULT_PRICE)
            .isBuy(DEFAULT_IS_BUY);
        return stockDiary;
    }

    @Before
    public void initTest() {
        stockDiary = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockDiary() throws Exception {
        int databaseSizeBeforeCreate = stockDiaryRepository.findAll().size();

        // Create the StockDiary
        StockDiaryDTO stockDiaryDTO = stockDiaryMapper.toDto(stockDiary);
        restStockDiaryMockMvc.perform(post("/api/stock-diaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockDiaryDTO)))
            .andExpect(status().isCreated());

        // Validate the StockDiary in the database
        List<StockDiary> stockDiaryList = stockDiaryRepository.findAll();
        assertThat(stockDiaryList).hasSize(databaseSizeBeforeCreate + 1);
        StockDiary testStockDiary = stockDiaryList.get(stockDiaryList.size() - 1);
        assertThat(testStockDiary.getDateOfCreation()).isEqualTo(DEFAULT_DATE_OF_CREATION);
        assertThat(testStockDiary.getUnits()).isEqualTo(DEFAULT_UNITS);
        assertThat(testStockDiary.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testStockDiary.isIsBuy()).isEqualTo(DEFAULT_IS_BUY);

        // Validate the StockDiary in Elasticsearch
        verify(mockStockDiarySearchRepository, times(1)).save(testStockDiary);
    }

    @Test
    @Transactional
    public void createStockDiaryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockDiaryRepository.findAll().size();

        // Create the StockDiary with an existing ID
        stockDiary.setId(1L);
        StockDiaryDTO stockDiaryDTO = stockDiaryMapper.toDto(stockDiary);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockDiaryMockMvc.perform(post("/api/stock-diaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockDiaryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StockDiary in the database
        List<StockDiary> stockDiaryList = stockDiaryRepository.findAll();
        assertThat(stockDiaryList).hasSize(databaseSizeBeforeCreate);

        // Validate the StockDiary in Elasticsearch
        verify(mockStockDiarySearchRepository, times(0)).save(stockDiary);
    }

    @Test
    @Transactional
    public void getAllStockDiaries() throws Exception {
        // Initialize the database
        stockDiaryRepository.saveAndFlush(stockDiary);

        // Get all the stockDiaryList
        restStockDiaryMockMvc.perform(get("/api/stock-diaries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockDiary.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateOfCreation").value(hasItem(DEFAULT_DATE_OF_CREATION.toString())))
            .andExpect(jsonPath("$.[*].units").value(hasItem(DEFAULT_UNITS.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].isBuy").value(hasItem(DEFAULT_IS_BUY.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getStockDiary() throws Exception {
        // Initialize the database
        stockDiaryRepository.saveAndFlush(stockDiary);

        // Get the stockDiary
        restStockDiaryMockMvc.perform(get("/api/stock-diaries/{id}", stockDiary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockDiary.getId().intValue()))
            .andExpect(jsonPath("$.dateOfCreation").value(DEFAULT_DATE_OF_CREATION.toString()))
            .andExpect(jsonPath("$.units").value(DEFAULT_UNITS.doubleValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.isBuy").value(DEFAULT_IS_BUY.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingStockDiary() throws Exception {
        // Get the stockDiary
        restStockDiaryMockMvc.perform(get("/api/stock-diaries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockDiary() throws Exception {
        // Initialize the database
        stockDiaryRepository.saveAndFlush(stockDiary);

        int databaseSizeBeforeUpdate = stockDiaryRepository.findAll().size();

        // Update the stockDiary
        StockDiary updatedStockDiary = stockDiaryRepository.findById(stockDiary.getId()).get();
        // Disconnect from session so that the updates on updatedStockDiary are not directly saved in db
        em.detach(updatedStockDiary);
        updatedStockDiary
            .dateOfCreation(UPDATED_DATE_OF_CREATION)
            .units(UPDATED_UNITS)
            .price(UPDATED_PRICE)
            .isBuy(UPDATED_IS_BUY);
        StockDiaryDTO stockDiaryDTO = stockDiaryMapper.toDto(updatedStockDiary);

        restStockDiaryMockMvc.perform(put("/api/stock-diaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockDiaryDTO)))
            .andExpect(status().isOk());

        // Validate the StockDiary in the database
        List<StockDiary> stockDiaryList = stockDiaryRepository.findAll();
        assertThat(stockDiaryList).hasSize(databaseSizeBeforeUpdate);
        StockDiary testStockDiary = stockDiaryList.get(stockDiaryList.size() - 1);
        assertThat(testStockDiary.getDateOfCreation()).isEqualTo(UPDATED_DATE_OF_CREATION);
        assertThat(testStockDiary.getUnits()).isEqualTo(UPDATED_UNITS);
        assertThat(testStockDiary.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testStockDiary.isIsBuy()).isEqualTo(UPDATED_IS_BUY);

        // Validate the StockDiary in Elasticsearch
        verify(mockStockDiarySearchRepository, times(1)).save(testStockDiary);
    }

    @Test
    @Transactional
    public void updateNonExistingStockDiary() throws Exception {
        int databaseSizeBeforeUpdate = stockDiaryRepository.findAll().size();

        // Create the StockDiary
        StockDiaryDTO stockDiaryDTO = stockDiaryMapper.toDto(stockDiary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockDiaryMockMvc.perform(put("/api/stock-diaries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockDiaryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StockDiary in the database
        List<StockDiary> stockDiaryList = stockDiaryRepository.findAll();
        assertThat(stockDiaryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the StockDiary in Elasticsearch
        verify(mockStockDiarySearchRepository, times(0)).save(stockDiary);
    }

    @Test
    @Transactional
    public void deleteStockDiary() throws Exception {
        // Initialize the database
        stockDiaryRepository.saveAndFlush(stockDiary);

        int databaseSizeBeforeDelete = stockDiaryRepository.findAll().size();

        // Delete the stockDiary
        restStockDiaryMockMvc.perform(delete("/api/stock-diaries/{id}", stockDiary.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockDiary> stockDiaryList = stockDiaryRepository.findAll();
        assertThat(stockDiaryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the StockDiary in Elasticsearch
        verify(mockStockDiarySearchRepository, times(1)).deleteById(stockDiary.getId());
    }

    @Test
    @Transactional
    public void searchStockDiary() throws Exception {
        // Initialize the database
        stockDiaryRepository.saveAndFlush(stockDiary);
        when(mockStockDiarySearchRepository.search(queryStringQuery("id:" + stockDiary.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(stockDiary), PageRequest.of(0, 1), 1));
        // Search the stockDiary
        restStockDiaryMockMvc.perform(get("/api/_search/stock-diaries?query=id:" + stockDiary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockDiary.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateOfCreation").value(hasItem(DEFAULT_DATE_OF_CREATION.toString())))
            .andExpect(jsonPath("$.[*].units").value(hasItem(DEFAULT_UNITS.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].isBuy").value(hasItem(DEFAULT_IS_BUY.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockDiary.class);
        StockDiary stockDiary1 = new StockDiary();
        stockDiary1.setId(1L);
        StockDiary stockDiary2 = new StockDiary();
        stockDiary2.setId(stockDiary1.getId());
        assertThat(stockDiary1).isEqualTo(stockDiary2);
        stockDiary2.setId(2L);
        assertThat(stockDiary1).isNotEqualTo(stockDiary2);
        stockDiary1.setId(null);
        assertThat(stockDiary1).isNotEqualTo(stockDiary2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockDiaryDTO.class);
        StockDiaryDTO stockDiaryDTO1 = new StockDiaryDTO();
        stockDiaryDTO1.setId(1L);
        StockDiaryDTO stockDiaryDTO2 = new StockDiaryDTO();
        assertThat(stockDiaryDTO1).isNotEqualTo(stockDiaryDTO2);
        stockDiaryDTO2.setId(stockDiaryDTO1.getId());
        assertThat(stockDiaryDTO1).isEqualTo(stockDiaryDTO2);
        stockDiaryDTO2.setId(2L);
        assertThat(stockDiaryDTO1).isNotEqualTo(stockDiaryDTO2);
        stockDiaryDTO1.setId(null);
        assertThat(stockDiaryDTO1).isNotEqualTo(stockDiaryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(stockDiaryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(stockDiaryMapper.fromId(null)).isNull();
    }
}
