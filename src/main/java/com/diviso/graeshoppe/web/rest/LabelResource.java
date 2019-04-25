package com.diviso.graeshoppe.web.rest;
import com.diviso.graeshoppe.service.LabelService;
import com.diviso.graeshoppe.web.rest.errors.BadRequestAlertException;
import com.diviso.graeshoppe.web.rest.util.HeaderUtil;
import com.diviso.graeshoppe.web.rest.util.PaginationUtil;
import com.diviso.graeshoppe.service.dto.LabelDTO;
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
 * REST controller for managing Label.
 */
@RestController
@RequestMapping("/api")
public class LabelResource {

    private final Logger log = LoggerFactory.getLogger(LabelResource.class);

    private static final String ENTITY_NAME = "productmicroserviceLabel";

    private final LabelService labelService;

    public LabelResource(LabelService labelService) {
        this.labelService = labelService;
    }

    /**
     * POST  /labels : Create a new label.
     *
     * @param labelDTO the labelDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new labelDTO, or with status 400 (Bad Request) if the label has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/labels")
    public ResponseEntity<LabelDTO> createLabel(@Valid @RequestBody LabelDTO labelDTO) throws URISyntaxException {
        log.debug("REST request to save Label : {}", labelDTO);
        if (labelDTO.getId() != null) {
            throw new BadRequestAlertException("A new label cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LabelDTO result = labelService.save(labelDTO);
        return ResponseEntity.created(new URI("/api/labels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /labels : Updates an existing label.
     *
     * @param labelDTO the labelDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated labelDTO,
     * or with status 400 (Bad Request) if the labelDTO is not valid,
     * or with status 500 (Internal Server Error) if the labelDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/labels")
    public ResponseEntity<LabelDTO> updateLabel(@Valid @RequestBody LabelDTO labelDTO) throws URISyntaxException {
        log.debug("REST request to update Label : {}", labelDTO);
        if (labelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LabelDTO result = labelService.save(labelDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, labelDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /labels : get all the labels.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of labels in body
     */
    @GetMapping("/labels")
    public ResponseEntity<List<LabelDTO>> getAllLabels(Pageable pageable) {
        log.debug("REST request to get a page of Labels");
        Page<LabelDTO> page = labelService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/labels");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /labels/:id : get the "id" label.
     *
     * @param id the id of the labelDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the labelDTO, or with status 404 (Not Found)
     */
    @GetMapping("/labels/{id}")
    public ResponseEntity<LabelDTO> getLabel(@PathVariable Long id) {
        log.debug("REST request to get Label : {}", id);
        Optional<LabelDTO> labelDTO = labelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(labelDTO);
    }

    /**
     * DELETE  /labels/:id : delete the "id" label.
     *
     * @param id the id of the labelDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/labels/{id}")
    public ResponseEntity<Void> deleteLabel(@PathVariable Long id) {
        log.debug("REST request to delete Label : {}", id);
        labelService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/labels?query=:query : search for the label corresponding
     * to the query.
     *
     * @param query the query of the label search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/labels")
    public ResponseEntity<List<LabelDTO>> searchLabels(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Labels for query {}", query);
        Page<LabelDTO> page = labelService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/labels");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
