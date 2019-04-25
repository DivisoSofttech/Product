package com.diviso.graeshoppe.web.rest;
import com.diviso.graeshoppe.service.TaxTypeService;
import com.diviso.graeshoppe.web.rest.errors.BadRequestAlertException;
import com.diviso.graeshoppe.web.rest.util.HeaderUtil;
import com.diviso.graeshoppe.web.rest.util.PaginationUtil;
import com.diviso.graeshoppe.service.dto.TaxTypeDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing TaxType.
 */
@RestController
@RequestMapping("/api")
public class TaxTypeResource {

    private final Logger log = LoggerFactory.getLogger(TaxTypeResource.class);

    private static final String ENTITY_NAME = "productmicroserviceTaxType";

    private final TaxTypeService taxTypeService;

    public TaxTypeResource(TaxTypeService taxTypeService) {
        this.taxTypeService = taxTypeService;
    }

    /**
     * POST  /tax-types : Create a new taxType.
     *
     * @param taxTypeDTO the taxTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new taxTypeDTO, or with status 400 (Bad Request) if the taxType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tax-types")
    public ResponseEntity<TaxTypeDTO> createTaxType(@RequestBody TaxTypeDTO taxTypeDTO) throws URISyntaxException {
        log.debug("REST request to save TaxType : {}", taxTypeDTO);
        if (taxTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new taxType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TaxTypeDTO result = taxTypeService.save(taxTypeDTO);
        return ResponseEntity.created(new URI("/api/tax-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tax-types : Updates an existing taxType.
     *
     * @param taxTypeDTO the taxTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated taxTypeDTO,
     * or with status 400 (Bad Request) if the taxTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the taxTypeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tax-types")
    public ResponseEntity<TaxTypeDTO> updateTaxType(@RequestBody TaxTypeDTO taxTypeDTO) throws URISyntaxException {
        log.debug("REST request to update TaxType : {}", taxTypeDTO);
        if (taxTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TaxTypeDTO result = taxTypeService.save(taxTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, taxTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tax-types : get all the taxTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of taxTypes in body
     */
    @GetMapping("/tax-types")
    public ResponseEntity<List<TaxTypeDTO>> getAllTaxTypes(Pageable pageable) {
        log.debug("REST request to get a page of TaxTypes");
        Page<TaxTypeDTO> page = taxTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tax-types");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /tax-types/:id : get the "id" taxType.
     *
     * @param id the id of the taxTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the taxTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tax-types/{id}")
    public ResponseEntity<TaxTypeDTO> getTaxType(@PathVariable Long id) {
        log.debug("REST request to get TaxType : {}", id);
        Optional<TaxTypeDTO> taxTypeDTO = taxTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taxTypeDTO);
    }

    /**
     * DELETE  /tax-types/:id : delete the "id" taxType.
     *
     * @param id the id of the taxTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tax-types/{id}")
    public ResponseEntity<Void> deleteTaxType(@PathVariable Long id) {
        log.debug("REST request to delete TaxType : {}", id);
        taxTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/tax-types?query=:query : search for the taxType corresponding
     * to the query.
     *
     * @param query the query of the taxType search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/tax-types")
    public ResponseEntity<List<TaxTypeDTO>> searchTaxTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TaxTypes for query {}", query);
        Page<TaxTypeDTO> page = taxTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/tax-types");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
