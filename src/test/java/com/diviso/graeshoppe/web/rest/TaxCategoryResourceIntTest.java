package com.diviso.graeshoppe.web.rest;

import com.diviso.graeshoppe.ProductmicroserviceApp;

import com.diviso.graeshoppe.domain.TaxCategory;
import com.diviso.graeshoppe.repository.TaxCategoryRepository;
import com.diviso.graeshoppe.repository.search.TaxCategorySearchRepository;
import com.diviso.graeshoppe.service.TaxCategoryService;
import com.diviso.graeshoppe.service.dto.TaxCategoryDTO;
import com.diviso.graeshoppe.service.mapper.TaxCategoryMapper;
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
 * Test class for the TaxCategoryResource REST controller.
 *
 * @see TaxCategoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductmicroserviceApp.class)
public class TaxCategoryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private TaxCategoryRepository taxCategoryRepository;

    @Autowired
    private TaxCategoryMapper taxCategoryMapper;

    @Autowired
    private TaxCategoryService taxCategoryService;

    /**
     * This repository is mocked in the com.diviso.graeshoppe.repository.search test package.
     *
     * @see com.diviso.graeshoppe.repository.search.TaxCategorySearchRepositoryMockConfiguration
     */
    @Autowired
    private TaxCategorySearchRepository mockTaxCategorySearchRepository;

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

    private MockMvc restTaxCategoryMockMvc;

    private TaxCategory taxCategory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TaxCategoryResource taxCategoryResource = new TaxCategoryResource(taxCategoryService);
        this.restTaxCategoryMockMvc = MockMvcBuilders.standaloneSetup(taxCategoryResource)
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
    public static TaxCategory createEntity(EntityManager em) {
        TaxCategory taxCategory = new TaxCategory()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return taxCategory;
    }

    @Before
    public void initTest() {
        taxCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createTaxCategory() throws Exception {
        int databaseSizeBeforeCreate = taxCategoryRepository.findAll().size();

        // Create the TaxCategory
        TaxCategoryDTO taxCategoryDTO = taxCategoryMapper.toDto(taxCategory);
        restTaxCategoryMockMvc.perform(post("/api/tax-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taxCategoryDTO)))
            .andExpect(status().isCreated());

        // Validate the TaxCategory in the database
        List<TaxCategory> taxCategoryList = taxCategoryRepository.findAll();
        assertThat(taxCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        TaxCategory testTaxCategory = taxCategoryList.get(taxCategoryList.size() - 1);
        assertThat(testTaxCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTaxCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the TaxCategory in Elasticsearch
        verify(mockTaxCategorySearchRepository, times(1)).save(testTaxCategory);
    }

    @Test
    @Transactional
    public void createTaxCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = taxCategoryRepository.findAll().size();

        // Create the TaxCategory with an existing ID
        taxCategory.setId(1L);
        TaxCategoryDTO taxCategoryDTO = taxCategoryMapper.toDto(taxCategory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaxCategoryMockMvc.perform(post("/api/tax-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taxCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TaxCategory in the database
        List<TaxCategory> taxCategoryList = taxCategoryRepository.findAll();
        assertThat(taxCategoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the TaxCategory in Elasticsearch
        verify(mockTaxCategorySearchRepository, times(0)).save(taxCategory);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxCategoryRepository.findAll().size();
        // set the field null
        taxCategory.setName(null);

        // Create the TaxCategory, which fails.
        TaxCategoryDTO taxCategoryDTO = taxCategoryMapper.toDto(taxCategory);

        restTaxCategoryMockMvc.perform(post("/api/tax-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taxCategoryDTO)))
            .andExpect(status().isBadRequest());

        List<TaxCategory> taxCategoryList = taxCategoryRepository.findAll();
        assertThat(taxCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTaxCategories() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);

        // Get all the taxCategoryList
        restTaxCategoryMockMvc.perform(get("/api/tax-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getTaxCategory() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);

        // Get the taxCategory
        restTaxCategoryMockMvc.perform(get("/api/tax-categories/{id}", taxCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(taxCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTaxCategory() throws Exception {
        // Get the taxCategory
        restTaxCategoryMockMvc.perform(get("/api/tax-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTaxCategory() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);

        int databaseSizeBeforeUpdate = taxCategoryRepository.findAll().size();

        // Update the taxCategory
        TaxCategory updatedTaxCategory = taxCategoryRepository.findById(taxCategory.getId()).get();
        // Disconnect from session so that the updates on updatedTaxCategory are not directly saved in db
        em.detach(updatedTaxCategory);
        updatedTaxCategory
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        TaxCategoryDTO taxCategoryDTO = taxCategoryMapper.toDto(updatedTaxCategory);

        restTaxCategoryMockMvc.perform(put("/api/tax-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taxCategoryDTO)))
            .andExpect(status().isOk());

        // Validate the TaxCategory in the database
        List<TaxCategory> taxCategoryList = taxCategoryRepository.findAll();
        assertThat(taxCategoryList).hasSize(databaseSizeBeforeUpdate);
        TaxCategory testTaxCategory = taxCategoryList.get(taxCategoryList.size() - 1);
        assertThat(testTaxCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTaxCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the TaxCategory in Elasticsearch
        verify(mockTaxCategorySearchRepository, times(1)).save(testTaxCategory);
    }

    @Test
    @Transactional
    public void updateNonExistingTaxCategory() throws Exception {
        int databaseSizeBeforeUpdate = taxCategoryRepository.findAll().size();

        // Create the TaxCategory
        TaxCategoryDTO taxCategoryDTO = taxCategoryMapper.toDto(taxCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxCategoryMockMvc.perform(put("/api/tax-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taxCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TaxCategory in the database
        List<TaxCategory> taxCategoryList = taxCategoryRepository.findAll();
        assertThat(taxCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TaxCategory in Elasticsearch
        verify(mockTaxCategorySearchRepository, times(0)).save(taxCategory);
    }

    @Test
    @Transactional
    public void deleteTaxCategory() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);

        int databaseSizeBeforeDelete = taxCategoryRepository.findAll().size();

        // Delete the taxCategory
        restTaxCategoryMockMvc.perform(delete("/api/tax-categories/{id}", taxCategory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TaxCategory> taxCategoryList = taxCategoryRepository.findAll();
        assertThat(taxCategoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TaxCategory in Elasticsearch
        verify(mockTaxCategorySearchRepository, times(1)).deleteById(taxCategory.getId());
    }

    @Test
    @Transactional
    public void searchTaxCategory() throws Exception {
        // Initialize the database
        taxCategoryRepository.saveAndFlush(taxCategory);
        when(mockTaxCategorySearchRepository.search(queryStringQuery("id:" + taxCategory.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(taxCategory), PageRequest.of(0, 1), 1));
        // Search the taxCategory
        restTaxCategoryMockMvc.perform(get("/api/_search/tax-categories?query=id:" + taxCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxCategory.class);
        TaxCategory taxCategory1 = new TaxCategory();
        taxCategory1.setId(1L);
        TaxCategory taxCategory2 = new TaxCategory();
        taxCategory2.setId(taxCategory1.getId());
        assertThat(taxCategory1).isEqualTo(taxCategory2);
        taxCategory2.setId(2L);
        assertThat(taxCategory1).isNotEqualTo(taxCategory2);
        taxCategory1.setId(null);
        assertThat(taxCategory1).isNotEqualTo(taxCategory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxCategoryDTO.class);
        TaxCategoryDTO taxCategoryDTO1 = new TaxCategoryDTO();
        taxCategoryDTO1.setId(1L);
        TaxCategoryDTO taxCategoryDTO2 = new TaxCategoryDTO();
        assertThat(taxCategoryDTO1).isNotEqualTo(taxCategoryDTO2);
        taxCategoryDTO2.setId(taxCategoryDTO1.getId());
        assertThat(taxCategoryDTO1).isEqualTo(taxCategoryDTO2);
        taxCategoryDTO2.setId(2L);
        assertThat(taxCategoryDTO1).isNotEqualTo(taxCategoryDTO2);
        taxCategoryDTO1.setId(null);
        assertThat(taxCategoryDTO1).isNotEqualTo(taxCategoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(taxCategoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(taxCategoryMapper.fromId(null)).isNull();
    }
}
