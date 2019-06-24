package com.diviso.graeshoppe.web.rest;
import com.diviso.graeshoppe.service.StockService;
import com.diviso.graeshoppe.web.rest.errors.BadRequestAlertException;
import com.diviso.graeshoppe.web.rest.util.HeaderUtil;
import com.diviso.graeshoppe.web.rest.util.PaginationUtil;
import com.diviso.graeshoppe.service.dto.StockDTO;
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
 * REST controller for managing Stock.
 */
@RestController
@RequestMapping("/api")
public class StockResource {

    private final Logger log = LoggerFactory.getLogger(StockResource.class);

    private static final String ENTITY_NAME = "productmicroserviceStock";

    private final StockService stockService;

    public StockResource(StockService stockService) {
        this.stockService = stockService;
    }

    /**
     * POST  /stocks : Create a new stock.
     *
     * @param stockDTO the stockDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockDTO, or with status 400 (Bad Request) if the stock has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stocks")
    public ResponseEntity<StockDTO> createStock(@Valid @RequestBody StockDTO stockDTO) throws URISyntaxException {
        log.debug("REST request to save Stock : {}", stockDTO);
        if (stockDTO.getId() != null) {
            throw new BadRequestAlertException("A new stock cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StockDTO result = stockService.save(stockDTO);
        return ResponseEntity.created(new URI("/api/stocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stocks : Updates an existing stock.
     *
     * @param stockDTO the stockDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockDTO,
     * or with status 400 (Bad Request) if the stockDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stocks")
    public ResponseEntity<StockDTO> updateStock(@Valid @RequestBody StockDTO stockDTO) throws URISyntaxException {
        log.debug("REST request to update Stock : {}", stockDTO);
        if (stockDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StockDTO result = stockService.save(stockDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stocks : get all the stocks.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of stocks in body
     */
    @GetMapping("/stocks")
    public ResponseEntity<List<StockDTO>> getAllStocks(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Stocks");
        Page<StockDTO> page;
        if (eagerload) {
            page = stockService.findAllWithEagerRelationships(pageable);
        } else {
            page = stockService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/stocks?eagerload=%b", eagerload));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /stocks/:id : get the "id" stock.
     *
     * @param id the id of the stockDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stocks/{id}")
    public ResponseEntity<StockDTO> getStock(@PathVariable Long id) {
        log.debug("REST request to get Stock : {}", id);
        Optional<StockDTO> stockDTO = stockService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stockDTO);
    }

    /**
     * DELETE  /stocks/:id : delete the "id" stock.
     *
     * @param id the id of the stockDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stocks/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        log.debug("REST request to delete Stock : {}", id);
        stockService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/stocks?query=:query : search for the stock corresponding
     * to the query.
     *
     * @param query the query of the stock search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/stocks")
    public ResponseEntity<List<StockDTO>> searchStocks(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Stocks for query {}", query);
        Page<StockDTO> page = stockService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/stocks");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
 
   
}
