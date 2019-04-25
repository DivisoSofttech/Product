package com.diviso.graeshoppe.web.rest;
import com.diviso.graeshoppe.service.TaxService;
import com.diviso.graeshoppe.web.rest.errors.BadRequestAlertException;
import com.diviso.graeshoppe.web.rest.util.HeaderUtil;
import com.diviso.graeshoppe.web.rest.util.PaginationUtil;
import com.diviso.graeshoppe.service.dto.TaxDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Tax.
 */
@RestController
@RequestMapping("/api")
public class TaxResource {

    private final Logger log = LoggerFactory.getLogger(TaxResource.class);

    private static final String ENTITY_NAME = "productmicroserviceTax";

    private final TaxService taxService;

    public TaxResource(TaxService taxService) {
        this.taxService = taxService;
    }

    /**
     * POST  /taxes : Create a new tax.
     *
     * @param taxDTO the taxDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new taxDTO, or with status 400 (Bad Request) if the tax has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/taxes")
    public ResponseEntity<TaxDTO> createTax(@Valid @RequestBody TaxDTO taxDTO) throws URISyntaxException {
        log.debug("REST request to save Tax : {}", taxDTO);
        if (taxDTO.getId() != null) {
            throw new BadRequestAlertException("A new tax cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TaxDTO result = taxService.save(taxDTO);
        return ResponseEntity.created(new URI("/api/taxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /taxes : Updates an existing tax.
     *
     * @param taxDTO the taxDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated taxDTO,
     * or with status 400 (Bad Request) if the taxDTO is not valid,
     * or with status 500 (Internal Server Error) if the taxDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/taxes")
    public ResponseEntity<TaxDTO> updateTax(@Valid @RequestBody TaxDTO taxDTO) throws URISyntaxException {
        log.debug("REST request to update Tax : {}", taxDTO);
        if (taxDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TaxDTO result = taxService.save(taxDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, taxDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /taxes : get all the taxes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of taxes in body
     */
    @GetMapping("/taxes")
    public ResponseEntity<List<TaxDTO>> getAllTaxes(Pageable pageable) {
        log.debug("REST request to get a page of Taxes");
        Page<TaxDTO> page = taxService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/taxes");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /taxes/:id : get the "id" tax.
     *
     * @param id the id of the taxDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the taxDTO, or with status 404 (Not Found)
     */
    @GetMapping("/taxes/{id}")
    public ResponseEntity<TaxDTO> getTax(@PathVariable Long id) {
        log.debug("REST request to get Tax : {}", id);
        Optional<TaxDTO> taxDTO = taxService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taxDTO);
    }

    /**
     * DELETE  /taxes/:id : delete the "id" tax.
     *
     * @param id the id of the taxDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/taxes/{id}")
    public ResponseEntity<Void> deleteTax(@PathVariable Long id) {
        log.debug("REST request to delete Tax : {}", id);
        taxService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/taxes?query=:query : search for the tax corresponding
     * to the query.
     *
     * @param query the query of the tax search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/taxes")
    public ResponseEntity<List<TaxDTO>> searchTaxes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Taxes for query {}", query);
        Page<TaxDTO> page = taxService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/taxes");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
