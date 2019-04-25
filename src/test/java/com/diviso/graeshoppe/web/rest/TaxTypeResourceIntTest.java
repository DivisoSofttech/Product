package com.diviso.graeshoppe.web.rest;

import com.diviso.graeshoppe.ProductmicroserviceApp;

import com.diviso.graeshoppe.domain.TaxType;
import com.diviso.graeshoppe.repository.TaxTypeRepository;
import com.diviso.graeshoppe.repository.search.TaxTypeSearchRepository;
import com.diviso.graeshoppe.service.TaxTypeService;
import com.diviso.graeshoppe.service.dto.TaxTypeDTO;
import com.diviso.graeshoppe.service.mapper.TaxTypeMapper;
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
 * Test class for the TaxTypeResource REST controller.
 *
 * @see TaxTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductmicroserviceApp.class)
public class TaxTypeResourceIntTest {

    private static final String DEFAULT_TAX_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TAX_TYPE = "BBBBBBBBBB";

    @Autowired
    private TaxTypeRepository taxTypeRepository;

    @Autowired
    private TaxTypeMapper taxTypeMapper;

    @Autowired
    private TaxTypeService taxTypeService;

    /**
     * This repository is mocked in the com.diviso.graeshoppe.repository.search test package.
     *
     * @see com.diviso.graeshoppe.repository.search.TaxTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private TaxTypeSearchRepository mockTaxTypeSearchRepository;

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

    private MockMvc restTaxTypeMockMvc;

    private TaxType taxType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TaxTypeResource taxTypeResource = new TaxTypeResource(taxTypeService);
        this.restTaxTypeMockMvc = MockMvcBuilders.standaloneSetup(taxTypeResource)
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
    public static TaxType createEntity(EntityManager em) {
        TaxType taxType = new TaxType()
            .taxType(DEFAULT_TAX_TYPE);
        return taxType;
    }

    @Before
    public void initTest() {
        taxType = createEntity(em);
    }

    @Test
    @Transactional
    public void createTaxType() throws Exception {
        int databaseSizeBeforeCreate = taxTypeRepository.findAll().size();

        // Create the TaxType
        TaxTypeDTO taxTypeDTO = taxTypeMapper.toDto(taxType);
        restTaxTypeMockMvc.perform(post("/api/tax-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taxTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the TaxType in the database
        List<TaxType> taxTypeList = taxTypeRepository.findAll();
        assertThat(taxTypeList).hasSize(databaseSizeBeforeCreate + 1);
        TaxType testTaxType = taxTypeList.get(taxTypeList.size() - 1);
        assertThat(testTaxType.getTaxType()).isEqualTo(DEFAULT_TAX_TYPE);

        // Validate the TaxType in Elasticsearch
        verify(mockTaxTypeSearchRepository, times(1)).save(testTaxType);
    }

    @Test
    @Transactional
    public void createTaxTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = taxTypeRepository.findAll().size();

        // Create the TaxType with an existing ID
        taxType.setId(1L);
        TaxTypeDTO taxTypeDTO = taxTypeMapper.toDto(taxType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaxTypeMockMvc.perform(post("/api/tax-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taxTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TaxType in the database
        List<TaxType> taxTypeList = taxTypeRepository.findAll();
        assertThat(taxTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the TaxType in Elasticsearch
        verify(mockTaxTypeSearchRepository, times(0)).save(taxType);
    }

    @Test
    @Transactional
    public void getAllTaxTypes() throws Exception {
        // Initialize the database
        taxTypeRepository.saveAndFlush(taxType);

        // Get all the taxTypeList
        restTaxTypeMockMvc.perform(get("/api/tax-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxType.getId().intValue())))
            .andExpect(jsonPath("$.[*].taxType").value(hasItem(DEFAULT_TAX_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getTaxType() throws Exception {
        // Initialize the database
        taxTypeRepository.saveAndFlush(taxType);

        // Get the taxType
        restTaxTypeMockMvc.perform(get("/api/tax-types/{id}", taxType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(taxType.getId().intValue()))
            .andExpect(jsonPath("$.taxType").value(DEFAULT_TAX_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTaxType() throws Exception {
        // Get the taxType
        restTaxTypeMockMvc.perform(get("/api/tax-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTaxType() throws Exception {
        // Initialize the database
        taxTypeRepository.saveAndFlush(taxType);

        int databaseSizeBeforeUpdate = taxTypeRepository.findAll().size();

        // Update the taxType
        TaxType updatedTaxType = taxTypeRepository.findById(taxType.getId()).get();
        // Disconnect from session so that the updates on updatedTaxType are not directly saved in db
        em.detach(updatedTaxType);
        updatedTaxType
            .taxType(UPDATED_TAX_TYPE);
        TaxTypeDTO taxTypeDTO = taxTypeMapper.toDto(updatedTaxType);

        restTaxTypeMockMvc.perform(put("/api/tax-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taxTypeDTO)))
            .andExpect(status().isOk());

        // Validate the TaxType in the database
        List<TaxType> taxTypeList = taxTypeRepository.findAll();
        assertThat(taxTypeList).hasSize(databaseSizeBeforeUpdate);
        TaxType testTaxType = taxTypeList.get(taxTypeList.size() - 1);
        assertThat(testTaxType.getTaxType()).isEqualTo(UPDATED_TAX_TYPE);

        // Validate the TaxType in Elasticsearch
        verify(mockTaxTypeSearchRepository, times(1)).save(testTaxType);
    }

    @Test
    @Transactional
    public void updateNonExistingTaxType() throws Exception {
        int databaseSizeBeforeUpdate = taxTypeRepository.findAll().size();

        // Create the TaxType
        TaxTypeDTO taxTypeDTO = taxTypeMapper.toDto(taxType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxTypeMockMvc.perform(put("/api/tax-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taxTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TaxType in the database
        List<TaxType> taxTypeList = taxTypeRepository.findAll();
        assertThat(taxTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TaxType in Elasticsearch
        verify(mockTaxTypeSearchRepository, times(0)).save(taxType);
    }

    @Test
    @Transactional
    public void deleteTaxType() throws Exception {
        // Initialize the database
        taxTypeRepository.saveAndFlush(taxType);

        int databaseSizeBeforeDelete = taxTypeRepository.findAll().size();

        // Delete the taxType
        restTaxTypeMockMvc.perform(delete("/api/tax-types/{id}", taxType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TaxType> taxTypeList = taxTypeRepository.findAll();
        assertThat(taxTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TaxType in Elasticsearch
        verify(mockTaxTypeSearchRepository, times(1)).deleteById(taxType.getId());
    }

    @Test
    @Transactional
    public void searchTaxType() throws Exception {
        // Initialize the database
        taxTypeRepository.saveAndFlush(taxType);
        when(mockTaxTypeSearchRepository.search(queryStringQuery("id:" + taxType.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(taxType), PageRequest.of(0, 1), 1));
        // Search the taxType
        restTaxTypeMockMvc.perform(get("/api/_search/tax-types?query=id:" + taxType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxType.getId().intValue())))
            .andExpect(jsonPath("$.[*].taxType").value(hasItem(DEFAULT_TAX_TYPE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxType.class);
        TaxType taxType1 = new TaxType();
        taxType1.setId(1L);
        TaxType taxType2 = new TaxType();
        taxType2.setId(taxType1.getId());
        assertThat(taxType1).isEqualTo(taxType2);
        taxType2.setId(2L);
        assertThat(taxType1).isNotEqualTo(taxType2);
        taxType1.setId(null);
        assertThat(taxType1).isNotEqualTo(taxType2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxTypeDTO.class);
        TaxTypeDTO taxTypeDTO1 = new TaxTypeDTO();
        taxTypeDTO1.setId(1L);
        TaxTypeDTO taxTypeDTO2 = new TaxTypeDTO();
        assertThat(taxTypeDTO1).isNotEqualTo(taxTypeDTO2);
        taxTypeDTO2.setId(taxTypeDTO1.getId());
        assertThat(taxTypeDTO1).isEqualTo(taxTypeDTO2);
        taxTypeDTO2.setId(2L);
        assertThat(taxTypeDTO1).isNotEqualTo(taxTypeDTO2);
        taxTypeDTO1.setId(null);
        assertThat(taxTypeDTO1).isNotEqualTo(taxTypeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(taxTypeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(taxTypeMapper.fromId(null)).isNull();
    }
}
