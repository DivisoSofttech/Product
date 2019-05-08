package com.diviso.graeshoppe.web.rest;
import com.diviso.graeshoppe.service.StockDiaryService;
import com.diviso.graeshoppe.web.rest.errors.BadRequestAlertException;
import com.diviso.graeshoppe.web.rest.util.HeaderUtil;
import com.diviso.graeshoppe.web.rest.util.PaginationUtil;
import com.diviso.graeshoppe.service.dto.StockDiaryDTO;
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
 * REST controller for managing StockDiary.
 */
@RestController
@RequestMapping("/api")
public class StockDiaryResource {

    private final Logger log = LoggerFactory.getLogger(StockDiaryResource.class);

    private static final String ENTITY_NAME = "productmicroserviceStockDiary";

    private final StockDiaryService stockDiaryService;

    public StockDiaryResource(StockDiaryService stockDiaryService) {
        this.stockDiaryService = stockDiaryService;
    }

    /**
     * POST  /stock-diaries : Create a new stockDiary.
     *
     * @param stockDiaryDTO the stockDiaryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockDiaryDTO, or with status 400 (Bad Request) if the stockDiary has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-diaries")
    public ResponseEntity<StockDiaryDTO> createStockDiary(@RequestBody StockDiaryDTO stockDiaryDTO) throws URISyntaxException {
        log.debug("REST request to save StockDiary : {}", stockDiaryDTO);
        if (stockDiaryDTO.getId() != null) {
            throw new BadRequestAlertException("A new stockDiary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StockDiaryDTO result = stockDiaryService.save(stockDiaryDTO);
        return ResponseEntity.created(new URI("/api/stock-diaries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    /**
     * POST  /stock-diaries/productStock : Create a new stockDiary.
     *
     * @param stockDiaryDTO the stockDiaryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockDiaryDTO, or with status 400 (Bad Request) if the stockDiary has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-diaries/productStock")
    public ResponseEntity<StockDiaryDTO> createStockOfProduct(@RequestBody StockDiaryDTO stockDiaryDTO) throws URISyntaxException {
        log.debug("REST request to save StockDiary : {}", stockDiaryDTO);
        if (stockDiaryDTO.getId() != null) {
            throw new BadRequestAlertException("A new stockDiary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StockDiaryDTO result = stockDiaryService.createStockOfProduct(stockDiaryDTO);
        return ResponseEntity.created(new URI("/api/stock-diaries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-diaries : Updates an existing stockDiary.
     *
     * @param stockDiaryDTO the stockDiaryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockDiaryDTO,
     * or with status 400 (Bad Request) if the stockDiaryDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockDiaryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-diaries")
    public ResponseEntity<StockDiaryDTO> updateStockDiary(@RequestBody StockDiaryDTO stockDiaryDTO) throws URISyntaxException {
        log.debug("REST request to update StockDiary : {}", stockDiaryDTO);
        if (stockDiaryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StockDiaryDTO result = stockDiaryService.save(stockDiaryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockDiaryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-diaries : get all the stockDiaries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockDiaries in body
     */
    @GetMapping("/stock-diaries")
    public ResponseEntity<List<StockDiaryDTO>> getAllStockDiaries(Pageable pageable) {
        log.debug("REST request to get a page of StockDiaries");
        Page<StockDiaryDTO> page = stockDiaryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-diaries");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /stock-diaries/:id : get the "id" stockDiary.
     *
     * @param id the id of the stockDiaryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockDiaryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-diaries/{id}")
    public ResponseEntity<StockDiaryDTO> getStockDiary(@PathVariable Long id) {
        log.debug("REST request to get StockDiary : {}", id);
        Optional<StockDiaryDTO> stockDiaryDTO = stockDiaryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stockDiaryDTO);
    }
    /**
     * GET  /stock-diaries/product/: : get the "product id" stockDiary.
     *
     * @param id the id of the product then the stockDiaryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockDiaryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-diaries/product/{id}")
    public ResponseEntity<List<StockDiaryDTO>> getStockDiaryByProductId(@PathVariable Long id,Pageable pageable) {
        log.debug("REST request to get StockDiary using productId : {}", id);
        Page<StockDiaryDTO> page = stockDiaryService.findByProductId(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-diaries");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * DELETE  /stock-diaries/:id : delete the "id" stockDiary.
     *
     * @param id the id of the stockDiaryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-diaries/{id}")
    public ResponseEntity<Void> deleteStockDiary(@PathVariable Long id) {
        log.debug("REST request to delete StockDiary : {}", id);
        stockDiaryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/stock-diaries?query=:query : search for the stockDiary corresponding
     * to the query.
     *
     * @param query the query of the stockDiary search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/stock-diaries")
    public ResponseEntity<List<StockDiaryDTO>> searchStockDiaries(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of StockDiaries for query {}", query);
        Page<StockDiaryDTO> page = stockDiaryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/stock-diaries");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
