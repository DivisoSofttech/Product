package com.diviso.graeshoppe.web.rest;
import com.diviso.graeshoppe.service.StockCurrentService;
import com.diviso.graeshoppe.web.rest.errors.BadRequestAlertException;
import com.diviso.graeshoppe.web.rest.util.HeaderUtil;
import com.diviso.graeshoppe.web.rest.util.PaginationUtil;
import com.diviso.graeshoppe.service.dto.StockCurrentDTO;
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
 * REST controller for managing StockCurrent.
 */
@RestController
@RequestMapping("/api")
public class StockCurrentResource {

    private final Logger log = LoggerFactory.getLogger(StockCurrentResource.class);

    private static final String ENTITY_NAME = "productmicroserviceStockCurrent";

    private final StockCurrentService stockCurrentService;

    public StockCurrentResource(StockCurrentService stockCurrentService) {
        this.stockCurrentService = stockCurrentService;
    }

    /**
     * POST  /stock-currents : Create a new stockCurrent.
     *
     * @param stockCurrentDTO the stockCurrentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockCurrentDTO, or with status 400 (Bad Request) if the stockCurrent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-currents")
    public ResponseEntity<StockCurrentDTO> createStockCurrent(@RequestBody StockCurrentDTO stockCurrentDTO) throws URISyntaxException {
        log.debug("REST request to save StockCurrent : {}", stockCurrentDTO);
        if (stockCurrentDTO.getId() != null) {
            throw new BadRequestAlertException("A new stockCurrent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StockCurrentDTO result1 = stockCurrentService.save(stockCurrentDTO);
        if (result1.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StockCurrentDTO result = stockCurrentService.save(result1);
        return ResponseEntity.created(new URI("/api/stock-currents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-currents : Updates an existing stockCurrent.
     *
     * @param stockCurrentDTO the stockCurrentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockCurrentDTO,
     * or with status 400 (Bad Request) if the stockCurrentDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockCurrentDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-currents")
    public ResponseEntity<StockCurrentDTO> updateStockCurrent(@RequestBody StockCurrentDTO stockCurrentDTO) throws URISyntaxException {
        log.debug("REST request to update StockCurrent : {}", stockCurrentDTO);
        if (stockCurrentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StockCurrentDTO result = stockCurrentService.save(stockCurrentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockCurrentDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-currents : get all the stockCurrents.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of stockCurrents in body
     */
    @GetMapping("/stock-currents")
    public ResponseEntity<List<StockCurrentDTO>> getAllStockCurrents(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("product-is-null".equals(filter)) {
            log.debug("REST request to get all StockCurrents where product is null");
            return new ResponseEntity<>(stockCurrentService.findAllWhereProductIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of StockCurrents");
        Page<StockCurrentDTO> page = stockCurrentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-currents");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /stock-currents/:id : get the "id" stockCurrent.
     *
     * @param id the id of the stockCurrentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockCurrentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-currents/{id}")
    public ResponseEntity<StockCurrentDTO> getStockCurrent(@PathVariable Long id) {
        log.debug("REST request to get StockCurrent : {}", id);
        Optional<StockCurrentDTO> stockCurrentDTO = stockCurrentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stockCurrentDTO);
    }
    /**
     * GET  /stock-currents/product/:id : get the "id" stockCurrent.
     *
     * @param id the id of the stockCurrentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockCurrentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-currents/product/{id}")
    public ResponseEntity<StockCurrentDTO> getStockCurrentByProductId(@PathVariable Long id) {
        log.debug("REST request to get StockCurrent : {}", id);
        Optional<StockCurrentDTO> stockCurrentDTO = stockCurrentService.findByProductId(id);
        return ResponseUtil.wrapOrNotFound(stockCurrentDTO);
    }

    /**
     * DELETE  /stock-currents/:id : delete the "id" stockCurrent.
     *
     * @param id the id of the stockCurrentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-currents/{id}")
    public ResponseEntity<Void> deleteStockCurrent(@PathVariable Long id) {
        log.debug("REST request to delete StockCurrent : {}", id);
        stockCurrentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/stock-currents?query=:query : search for the stockCurrent corresponding
     * to the query.
     *
     * @param query the query of the stockCurrent search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/stock-currents")
    public ResponseEntity<List<StockCurrentDTO>> searchStockCurrents(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of StockCurrents for query {}", query);
        Page<StockCurrentDTO> page = stockCurrentService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/stock-currents");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
