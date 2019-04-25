package com.diviso.graeshoppe.web.rest;
import com.diviso.graeshoppe.service.BarcodeTypeService;
import com.diviso.graeshoppe.web.rest.errors.BadRequestAlertException;
import com.diviso.graeshoppe.web.rest.util.HeaderUtil;
import com.diviso.graeshoppe.web.rest.util.PaginationUtil;
import com.diviso.graeshoppe.service.dto.BarcodeTypeDTO;
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
 * REST controller for managing BarcodeType.
 */
@RestController
@RequestMapping("/api")
public class BarcodeTypeResource {

    private final Logger log = LoggerFactory.getLogger(BarcodeTypeResource.class);

    private static final String ENTITY_NAME = "productmicroserviceBarcodeType";

    private final BarcodeTypeService barcodeTypeService;

    public BarcodeTypeResource(BarcodeTypeService barcodeTypeService) {
        this.barcodeTypeService = barcodeTypeService;
    }

    /**
     * POST  /barcode-types : Create a new barcodeType.
     *
     * @param barcodeTypeDTO the barcodeTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new barcodeTypeDTO, or with status 400 (Bad Request) if the barcodeType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/barcode-types")
    public ResponseEntity<BarcodeTypeDTO> createBarcodeType(@RequestBody BarcodeTypeDTO barcodeTypeDTO) throws URISyntaxException {
        log.debug("REST request to save BarcodeType : {}", barcodeTypeDTO);
        if (barcodeTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new barcodeType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BarcodeTypeDTO result = barcodeTypeService.save(barcodeTypeDTO);
        return ResponseEntity.created(new URI("/api/barcode-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /barcode-types : Updates an existing barcodeType.
     *
     * @param barcodeTypeDTO the barcodeTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated barcodeTypeDTO,
     * or with status 400 (Bad Request) if the barcodeTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the barcodeTypeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/barcode-types")
    public ResponseEntity<BarcodeTypeDTO> updateBarcodeType(@RequestBody BarcodeTypeDTO barcodeTypeDTO) throws URISyntaxException {
        log.debug("REST request to update BarcodeType : {}", barcodeTypeDTO);
        if (barcodeTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BarcodeTypeDTO result = barcodeTypeService.save(barcodeTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, barcodeTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /barcode-types : get all the barcodeTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of barcodeTypes in body
     */
    @GetMapping("/barcode-types")
    public ResponseEntity<List<BarcodeTypeDTO>> getAllBarcodeTypes(Pageable pageable) {
        log.debug("REST request to get a page of BarcodeTypes");
        Page<BarcodeTypeDTO> page = barcodeTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/barcode-types");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /barcode-types/:id : get the "id" barcodeType.
     *
     * @param id the id of the barcodeTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the barcodeTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/barcode-types/{id}")
    public ResponseEntity<BarcodeTypeDTO> getBarcodeType(@PathVariable Long id) {
        log.debug("REST request to get BarcodeType : {}", id);
        Optional<BarcodeTypeDTO> barcodeTypeDTO = barcodeTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(barcodeTypeDTO);
    }

    /**
     * DELETE  /barcode-types/:id : delete the "id" barcodeType.
     *
     * @param id the id of the barcodeTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/barcode-types/{id}")
    public ResponseEntity<Void> deleteBarcodeType(@PathVariable Long id) {
        log.debug("REST request to delete BarcodeType : {}", id);
        barcodeTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/barcode-types?query=:query : search for the barcodeType corresponding
     * to the query.
     *
     * @param query the query of the barcodeType search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/barcode-types")
    public ResponseEntity<List<BarcodeTypeDTO>> searchBarcodeTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of BarcodeTypes for query {}", query);
        Page<BarcodeTypeDTO> page = barcodeTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/barcode-types");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
