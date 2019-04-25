package com.diviso.graeshoppe.web.rest;
import com.diviso.graeshoppe.service.UomService;
import com.diviso.graeshoppe.web.rest.errors.BadRequestAlertException;
import com.diviso.graeshoppe.web.rest.util.HeaderUtil;
import com.diviso.graeshoppe.web.rest.util.PaginationUtil;
import com.diviso.graeshoppe.service.dto.UomDTO;
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
 * REST controller for managing Uom.
 */
@RestController
@RequestMapping("/api")
public class UomResource {

    private final Logger log = LoggerFactory.getLogger(UomResource.class);

    private static final String ENTITY_NAME = "productmicroserviceUom";

    private final UomService uomService;

    public UomResource(UomService uomService) {
        this.uomService = uomService;
    }

    /**
     * POST  /uoms : Create a new uom.
     *
     * @param uomDTO the uomDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new uomDTO, or with status 400 (Bad Request) if the uom has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/uoms")
    public ResponseEntity<UomDTO> createUom(@Valid @RequestBody UomDTO uomDTO) throws URISyntaxException {
        log.debug("REST request to save Uom : {}", uomDTO);
        if (uomDTO.getId() != null) {
            throw new BadRequestAlertException("A new uom cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UomDTO result = uomService.save(uomDTO);
        return ResponseEntity.created(new URI("/api/uoms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /uoms : Updates an existing uom.
     *
     * @param uomDTO the uomDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated uomDTO,
     * or with status 400 (Bad Request) if the uomDTO is not valid,
     * or with status 500 (Internal Server Error) if the uomDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/uoms")
    public ResponseEntity<UomDTO> updateUom(@Valid @RequestBody UomDTO uomDTO) throws URISyntaxException {
        log.debug("REST request to update Uom : {}", uomDTO);
        if (uomDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UomDTO result = uomService.save(uomDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, uomDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /uoms : get all the uoms.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of uoms in body
     */
    @GetMapping("/uoms")
    public ResponseEntity<List<UomDTO>> getAllUoms(Pageable pageable) {
        log.debug("REST request to get a page of Uoms");
        Page<UomDTO> page = uomService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/uoms");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /uoms/:id : get the "id" uom.
     *
     * @param id the id of the uomDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the uomDTO, or with status 404 (Not Found)
     */
    @GetMapping("/uoms/{id}")
    public ResponseEntity<UomDTO> getUom(@PathVariable Long id) {
        log.debug("REST request to get Uom : {}", id);
        Optional<UomDTO> uomDTO = uomService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uomDTO);
    }

    /**
     * DELETE  /uoms/:id : delete the "id" uom.
     *
     * @param id the id of the uomDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/uoms/{id}")
    public ResponseEntity<Void> deleteUom(@PathVariable Long id) {
        log.debug("REST request to delete Uom : {}", id);
        uomService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/uoms?query=:query : search for the uom corresponding
     * to the query.
     *
     * @param query the query of the uom search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/uoms")
    public ResponseEntity<List<UomDTO>> searchUoms(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Uoms for query {}", query);
        Page<UomDTO> page = uomService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/uoms");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
