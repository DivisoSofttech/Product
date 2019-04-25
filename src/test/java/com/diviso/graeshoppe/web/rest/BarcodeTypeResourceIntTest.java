package com.diviso.graeshoppe.web.rest;

import com.diviso.graeshoppe.ProductmicroserviceApp;

import com.diviso.graeshoppe.domain.BarcodeType;
import com.diviso.graeshoppe.repository.BarcodeTypeRepository;
import com.diviso.graeshoppe.repository.search.BarcodeTypeSearchRepository;
import com.diviso.graeshoppe.service.BarcodeTypeService;
import com.diviso.graeshoppe.service.dto.BarcodeTypeDTO;
import com.diviso.graeshoppe.service.mapper.BarcodeTypeMapper;
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
 * Test class for the BarcodeTypeResource REST controller.
 *
 * @see BarcodeTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductmicroserviceApp.class)
public class BarcodeTypeResourceIntTest {

    private static final String DEFAULT_BARCODE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_BARCODE_TYPE = "BBBBBBBBBB";

    @Autowired
    private BarcodeTypeRepository barcodeTypeRepository;

    @Autowired
    private BarcodeTypeMapper barcodeTypeMapper;

    @Autowired
    private BarcodeTypeService barcodeTypeService;

    /**
     * This repository is mocked in the com.diviso.graeshoppe.repository.search test package.
     *
     * @see com.diviso.graeshoppe.repository.search.BarcodeTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private BarcodeTypeSearchRepository mockBarcodeTypeSearchRepository;

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

    private MockMvc restBarcodeTypeMockMvc;

    private BarcodeType barcodeType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BarcodeTypeResource barcodeTypeResource = new BarcodeTypeResource(barcodeTypeService);
        this.restBarcodeTypeMockMvc = MockMvcBuilders.standaloneSetup(barcodeTypeResource)
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
    public static BarcodeType createEntity(EntityManager em) {
        BarcodeType barcodeType = new BarcodeType()
            .barcodeType(DEFAULT_BARCODE_TYPE);
        return barcodeType;
    }

    @Before
    public void initTest() {
        barcodeType = createEntity(em);
    }

    @Test
    @Transactional
    public void createBarcodeType() throws Exception {
        int databaseSizeBeforeCreate = barcodeTypeRepository.findAll().size();

        // Create the BarcodeType
        BarcodeTypeDTO barcodeTypeDTO = barcodeTypeMapper.toDto(barcodeType);
        restBarcodeTypeMockMvc.perform(post("/api/barcode-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(barcodeTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the BarcodeType in the database
        List<BarcodeType> barcodeTypeList = barcodeTypeRepository.findAll();
        assertThat(barcodeTypeList).hasSize(databaseSizeBeforeCreate + 1);
        BarcodeType testBarcodeType = barcodeTypeList.get(barcodeTypeList.size() - 1);
        assertThat(testBarcodeType.getBarcodeType()).isEqualTo(DEFAULT_BARCODE_TYPE);

        // Validate the BarcodeType in Elasticsearch
        verify(mockBarcodeTypeSearchRepository, times(1)).save(testBarcodeType);
    }

    @Test
    @Transactional
    public void createBarcodeTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = barcodeTypeRepository.findAll().size();

        // Create the BarcodeType with an existing ID
        barcodeType.setId(1L);
        BarcodeTypeDTO barcodeTypeDTO = barcodeTypeMapper.toDto(barcodeType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBarcodeTypeMockMvc.perform(post("/api/barcode-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(barcodeTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BarcodeType in the database
        List<BarcodeType> barcodeTypeList = barcodeTypeRepository.findAll();
        assertThat(barcodeTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the BarcodeType in Elasticsearch
        verify(mockBarcodeTypeSearchRepository, times(0)).save(barcodeType);
    }

    @Test
    @Transactional
    public void getAllBarcodeTypes() throws Exception {
        // Initialize the database
        barcodeTypeRepository.saveAndFlush(barcodeType);

        // Get all the barcodeTypeList
        restBarcodeTypeMockMvc.perform(get("/api/barcode-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(barcodeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcodeType").value(hasItem(DEFAULT_BARCODE_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getBarcodeType() throws Exception {
        // Initialize the database
        barcodeTypeRepository.saveAndFlush(barcodeType);

        // Get the barcodeType
        restBarcodeTypeMockMvc.perform(get("/api/barcode-types/{id}", barcodeType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(barcodeType.getId().intValue()))
            .andExpect(jsonPath("$.barcodeType").value(DEFAULT_BARCODE_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBarcodeType() throws Exception {
        // Get the barcodeType
        restBarcodeTypeMockMvc.perform(get("/api/barcode-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBarcodeType() throws Exception {
        // Initialize the database
        barcodeTypeRepository.saveAndFlush(barcodeType);

        int databaseSizeBeforeUpdate = barcodeTypeRepository.findAll().size();

        // Update the barcodeType
        BarcodeType updatedBarcodeType = barcodeTypeRepository.findById(barcodeType.getId()).get();
        // Disconnect from session so that the updates on updatedBarcodeType are not directly saved in db
        em.detach(updatedBarcodeType);
        updatedBarcodeType
            .barcodeType(UPDATED_BARCODE_TYPE);
        BarcodeTypeDTO barcodeTypeDTO = barcodeTypeMapper.toDto(updatedBarcodeType);

        restBarcodeTypeMockMvc.perform(put("/api/barcode-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(barcodeTypeDTO)))
            .andExpect(status().isOk());

        // Validate the BarcodeType in the database
        List<BarcodeType> barcodeTypeList = barcodeTypeRepository.findAll();
        assertThat(barcodeTypeList).hasSize(databaseSizeBeforeUpdate);
        BarcodeType testBarcodeType = barcodeTypeList.get(barcodeTypeList.size() - 1);
        assertThat(testBarcodeType.getBarcodeType()).isEqualTo(UPDATED_BARCODE_TYPE);

        // Validate the BarcodeType in Elasticsearch
        verify(mockBarcodeTypeSearchRepository, times(1)).save(testBarcodeType);
    }

    @Test
    @Transactional
    public void updateNonExistingBarcodeType() throws Exception {
        int databaseSizeBeforeUpdate = barcodeTypeRepository.findAll().size();

        // Create the BarcodeType
        BarcodeTypeDTO barcodeTypeDTO = barcodeTypeMapper.toDto(barcodeType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBarcodeTypeMockMvc.perform(put("/api/barcode-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(barcodeTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BarcodeType in the database
        List<BarcodeType> barcodeTypeList = barcodeTypeRepository.findAll();
        assertThat(barcodeTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BarcodeType in Elasticsearch
        verify(mockBarcodeTypeSearchRepository, times(0)).save(barcodeType);
    }

    @Test
    @Transactional
    public void deleteBarcodeType() throws Exception {
        // Initialize the database
        barcodeTypeRepository.saveAndFlush(barcodeType);

        int databaseSizeBeforeDelete = barcodeTypeRepository.findAll().size();

        // Delete the barcodeType
        restBarcodeTypeMockMvc.perform(delete("/api/barcode-types/{id}", barcodeType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BarcodeType> barcodeTypeList = barcodeTypeRepository.findAll();
        assertThat(barcodeTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the BarcodeType in Elasticsearch
        verify(mockBarcodeTypeSearchRepository, times(1)).deleteById(barcodeType.getId());
    }

    @Test
    @Transactional
    public void searchBarcodeType() throws Exception {
        // Initialize the database
        barcodeTypeRepository.saveAndFlush(barcodeType);
        when(mockBarcodeTypeSearchRepository.search(queryStringQuery("id:" + barcodeType.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(barcodeType), PageRequest.of(0, 1), 1));
        // Search the barcodeType
        restBarcodeTypeMockMvc.perform(get("/api/_search/barcode-types?query=id:" + barcodeType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(barcodeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].barcodeType").value(hasItem(DEFAULT_BARCODE_TYPE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BarcodeType.class);
        BarcodeType barcodeType1 = new BarcodeType();
        barcodeType1.setId(1L);
        BarcodeType barcodeType2 = new BarcodeType();
        barcodeType2.setId(barcodeType1.getId());
        assertThat(barcodeType1).isEqualTo(barcodeType2);
        barcodeType2.setId(2L);
        assertThat(barcodeType1).isNotEqualTo(barcodeType2);
        barcodeType1.setId(null);
        assertThat(barcodeType1).isNotEqualTo(barcodeType2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BarcodeTypeDTO.class);
        BarcodeTypeDTO barcodeTypeDTO1 = new BarcodeTypeDTO();
        barcodeTypeDTO1.setId(1L);
        BarcodeTypeDTO barcodeTypeDTO2 = new BarcodeTypeDTO();
        assertThat(barcodeTypeDTO1).isNotEqualTo(barcodeTypeDTO2);
        barcodeTypeDTO2.setId(barcodeTypeDTO1.getId());
        assertThat(barcodeTypeDTO1).isEqualTo(barcodeTypeDTO2);
        barcodeTypeDTO2.setId(2L);
        assertThat(barcodeTypeDTO1).isNotEqualTo(barcodeTypeDTO2);
        barcodeTypeDTO1.setId(null);
        assertThat(barcodeTypeDTO1).isNotEqualTo(barcodeTypeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(barcodeTypeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(barcodeTypeMapper.fromId(null)).isNull();
    }
}
