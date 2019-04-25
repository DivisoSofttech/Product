package com.diviso.graeshoppe.web.rest;
import com.diviso.graeshoppe.service.BarcodeService;
import com.diviso.graeshoppe.web.rest.errors.BadRequestAlertException;
import com.diviso.graeshoppe.web.rest.util.HeaderUtil;
import com.diviso.graeshoppe.web.rest.util.PaginationUtil;
import com.diviso.graeshoppe.service.dto.BarcodeDTO;
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
 * REST controller for managing Barcode.
 */
@RestController
@RequestMapping("/api")
public class BarcodeResource {

    private final Logger log = LoggerFactory.getLogger(BarcodeResource.class);

    private static final String ENTITY_NAME = "productmicroserviceBarcode";

    private final BarcodeService barcodeService;

    public BarcodeResource(BarcodeService barcodeService) {
        this.barcodeService = barcodeService;
    }

    /**
     * POST  /barcodes : Create a new barcode.
     *
     * @param barcodeDTO the barcodeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new barcodeDTO, or with status 400 (Bad Request) if the barcode has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/barcodes")
    public ResponseEntity<BarcodeDTO> createBarcode(@Valid @RequestBody BarcodeDTO barcodeDTO) throws URISyntaxException {
        log.debug("REST request to save Barcode : {}", barcodeDTO);
        if (barcodeDTO.getId() != null) {
            throw new BadRequestAlertException("A new barcode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BarcodeDTO result = barcodeService.save(barcodeDTO);
        return ResponseEntity.created(new URI("/api/barcodes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /barcodes : Updates an existing barcode.
     *
     * @param barcodeDTO the barcodeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated barcodeDTO,
     * or with status 400 (Bad Request) if the barcodeDTO is not valid,
     * or with status 500 (Internal Server Error) if the barcodeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/barcodes")
    public ResponseEntity<BarcodeDTO> updateBarcode(@Valid @RequestBody BarcodeDTO barcodeDTO) throws URISyntaxException {
        log.debug("REST request to update Barcode : {}", barcodeDTO);
        if (barcodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BarcodeDTO result = barcodeService.save(barcodeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, barcodeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /barcodes : get all the barcodes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of barcodes in body
     */
    @GetMapping("/barcodes")
    public ResponseEntity<List<BarcodeDTO>> getAllBarcodes(Pageable pageable) {
        log.debug("REST request to get a page of Barcodes");
        Page<BarcodeDTO> page = barcodeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/barcodes");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /barcodes/:id : get the "id" barcode.
     *
     * @param id the id of the barcodeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the barcodeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/barcodes/{id}")
    public ResponseEntity<BarcodeDTO> getBarcode(@PathVariable Long id) {
        log.debug("REST request to get Barcode : {}", id);
        Optional<BarcodeDTO> barcodeDTO = barcodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(barcodeDTO);
    }

    /**
     * DELETE  /barcodes/:id : delete the "id" barcode.
     *
     * @param id the id of the barcodeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/barcodes/{id}")
    public ResponseEntity<Void> deleteBarcode(@PathVariable Long id) {
        log.debug("REST request to delete Barcode : {}", id);
        barcodeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/barcodes?query=:query : search for the barcode corresponding
     * to the query.
     *
     * @param query the query of the barcode search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/barcodes")
    public ResponseEntity<List<BarcodeDTO>> searchBarcodes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Barcodes for query {}", query);
        Page<BarcodeDTO> page = barcodeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/barcodes");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
