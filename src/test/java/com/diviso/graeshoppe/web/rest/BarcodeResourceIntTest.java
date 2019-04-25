package com.diviso.graeshoppe.web.rest;

import com.diviso.graeshoppe.ProductmicroserviceApp;

import com.diviso.graeshoppe.domain.Barcode;
import com.diviso.graeshoppe.repository.BarcodeRepository;
import com.diviso.graeshoppe.repository.search.BarcodeSearchRepository;
import com.diviso.graeshoppe.service.BarcodeService;
import com.diviso.graeshoppe.service.dto.BarcodeDTO;
import com.diviso.graeshoppe.service.mapper.BarcodeMapper;
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
 * Test class for the BarcodeResource REST controller.
 *
 * @see BarcodeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductmicroserviceApp.class)
public class BarcodeResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private BarcodeRepository barcodeRepository;

    @Autowired
    private BarcodeMapper barcodeMapper;

    @Autowired
    private BarcodeService barcodeService;

    /**
     * This repository is mocked in the com.diviso.graeshoppe.repository.search test package.
     *
     * @see com.diviso.graeshoppe.repository.search.BarcodeSearchRepositoryMockConfiguration
     */
    @Autowired
    private BarcodeSearchRepository mockBarcodeSearchRepository;

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

    private MockMvc restBarcodeMockMvc;

    private Barcode barcode;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BarcodeResource barcodeResource = new BarcodeResource(barcodeService);
        this.restBarcodeMockMvc = MockMvcBuilders.standaloneSetup(barcodeResource)
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
    public static Barcode createEntity(EntityManager em) {
        Barcode barcode = new Barcode()
            .code(DEFAULT_CODE)
            .description(DEFAULT_DESCRIPTION);
        return barcode;
    }

    @Before
    public void initTest() {
        barcode = createEntity(em);
    }

    @Test
    @Transactional
    public void createBarcode() throws Exception {
        int databaseSizeBeforeCreate = barcodeRepository.findAll().size();

        // Create the Barcode
        BarcodeDTO barcodeDTO = barcodeMapper.toDto(barcode);
        restBarcodeMockMvc.perform(post("/api/barcodes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(barcodeDTO)))
            .andExpect(status().isCreated());

        // Validate the Barcode in the database
        List<Barcode> barcodeList = barcodeRepository.findAll();
        assertThat(barcodeList).hasSize(databaseSizeBeforeCreate + 1);
        Barcode testBarcode = barcodeList.get(barcodeList.size() - 1);
        assertThat(testBarcode.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testBarcode.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Barcode in Elasticsearch
        verify(mockBarcodeSearchRepository, times(1)).save(testBarcode);
    }

    @Test
    @Transactional
    public void createBarcodeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = barcodeRepository.findAll().size();

        // Create the Barcode with an existing ID
        barcode.setId(1L);
        BarcodeDTO barcodeDTO = barcodeMapper.toDto(barcode);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBarcodeMockMvc.perform(post("/api/barcodes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(barcodeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Barcode in the database
        List<Barcode> barcodeList = barcodeRepository.findAll();
        assertThat(barcodeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Barcode in Elasticsearch
        verify(mockBarcodeSearchRepository, times(0)).save(barcode);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = barcodeRepository.findAll().size();
        // set the field null
        barcode.setCode(null);

        // Create the Barcode, which fails.
        BarcodeDTO barcodeDTO = barcodeMapper.toDto(barcode);

        restBarcodeMockMvc.perform(post("/api/barcodes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(barcodeDTO)))
            .andExpect(status().isBadRequest());

        List<Barcode> barcodeList = barcodeRepository.findAll();
        assertThat(barcodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBarcodes() throws Exception {
        // Initialize the database
        barcodeRepository.saveAndFlush(barcode);

        // Get all the barcodeList
        restBarcodeMockMvc.perform(get("/api/barcodes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(barcode.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getBarcode() throws Exception {
        // Initialize the database
        barcodeRepository.saveAndFlush(barcode);

        // Get the barcode
        restBarcodeMockMvc.perform(get("/api/barcodes/{id}", barcode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(barcode.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBarcode() throws Exception {
        // Get the barcode
        restBarcodeMockMvc.perform(get("/api/barcodes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBarcode() throws Exception {
        // Initialize the database
        barcodeRepository.saveAndFlush(barcode);

        int databaseSizeBeforeUpdate = barcodeRepository.findAll().size();

        // Update the barcode
        Barcode updatedBarcode = barcodeRepository.findById(barcode.getId()).get();
        // Disconnect from session so that the updates on updatedBarcode are not directly saved in db
        em.detach(updatedBarcode);
        updatedBarcode
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION);
        BarcodeDTO barcodeDTO = barcodeMapper.toDto(updatedBarcode);

        restBarcodeMockMvc.perform(put("/api/barcodes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(barcodeDTO)))
            .andExpect(status().isOk());

        // Validate the Barcode in the database
        List<Barcode> barcodeList = barcodeRepository.findAll();
        assertThat(barcodeList).hasSize(databaseSizeBeforeUpdate);
        Barcode testBarcode = barcodeList.get(barcodeList.size() - 1);
        assertThat(testBarcode.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBarcode.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Barcode in Elasticsearch
        verify(mockBarcodeSearchRepository, times(1)).save(testBarcode);
    }

    @Test
    @Transactional
    public void updateNonExistingBarcode() throws Exception {
        int databaseSizeBeforeUpdate = barcodeRepository.findAll().size();

        // Create the Barcode
        BarcodeDTO barcodeDTO = barcodeMapper.toDto(barcode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBarcodeMockMvc.perform(put("/api/barcodes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(barcodeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Barcode in the database
        List<Barcode> barcodeList = barcodeRepository.findAll();
        assertThat(barcodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Barcode in Elasticsearch
        verify(mockBarcodeSearchRepository, times(0)).save(barcode);
    }

    @Test
    @Transactional
    public void deleteBarcode() throws Exception {
        // Initialize the database
        barcodeRepository.saveAndFlush(barcode);

        int databaseSizeBeforeDelete = barcodeRepository.findAll().size();

        // Delete the barcode
        restBarcodeMockMvc.perform(delete("/api/barcodes/{id}", barcode.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Barcode> barcodeList = barcodeRepository.findAll();
        assertThat(barcodeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Barcode in Elasticsearch
        verify(mockBarcodeSearchRepository, times(1)).deleteById(barcode.getId());
    }

    @Test
    @Transactional
    public void searchBarcode() throws Exception {
        // Initialize the database
        barcodeRepository.saveAndFlush(barcode);
        when(mockBarcodeSearchRepository.search(queryStringQuery("id:" + barcode.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(barcode), PageRequest.of(0, 1), 1));
        // Search the barcode
        restBarcodeMockMvc.perform(get("/api/_search/barcodes?query=id:" + barcode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(barcode.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Barcode.class);
        Barcode barcode1 = new Barcode();
        barcode1.setId(1L);
        Barcode barcode2 = new Barcode();
        barcode2.setId(barcode1.getId());
        assertThat(barcode1).isEqualTo(barcode2);
        barcode2.setId(2L);
        assertThat(barcode1).isNotEqualTo(barcode2);
        barcode1.setId(null);
        assertThat(barcode1).isNotEqualTo(barcode2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BarcodeDTO.class);
        BarcodeDTO barcodeDTO1 = new BarcodeDTO();
        barcodeDTO1.setId(1L);
        BarcodeDTO barcodeDTO2 = new BarcodeDTO();
        assertThat(barcodeDTO1).isNotEqualTo(barcodeDTO2);
        barcodeDTO2.setId(barcodeDTO1.getId());
        assertThat(barcodeDTO1).isEqualTo(barcodeDTO2);
        barcodeDTO2.setId(2L);
        assertThat(barcodeDTO1).isNotEqualTo(barcodeDTO2);
        barcodeDTO1.setId(null);
        assertThat(barcodeDTO1).isNotEqualTo(barcodeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(barcodeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(barcodeMapper.fromId(null)).isNull();
    }
}
