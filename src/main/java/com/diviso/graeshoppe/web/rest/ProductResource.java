package com.diviso.graeshoppe.web.rest;
import com.diviso.graeshoppe.domain.Product;
import com.diviso.graeshoppe.service.ProductService;
import com.diviso.graeshoppe.web.rest.errors.BadRequestAlertException;
import com.diviso.graeshoppe.web.rest.util.HeaderUtil;
import com.diviso.graeshoppe.web.rest.util.PaginationUtil;

import com.diviso.graeshoppe.service.dto.ProductDTO;
import com.diviso.graeshoppe.service.mapper.ProductMapper;

import io.github.jhipster.web.util.ResponseUtil;
import net.sf.jasperreports.engine.JRException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * REST controller for managing Product.
 */
@RestController
@RequestMapping("/api")
public class ProductResource {

    private final Logger log = LoggerFactory.getLogger(ProductResource.class);

    private static final String ENTITY_NAME = "productmicroserviceProduct";
    
    @Autowired
    private ProductMapper productMapper; 

  
    private final ProductService productService;

    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    /**
     * POST  /products : Create a new product.
     *
     * @param productDTO the productDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productDTO, or with status 400 (Bad Request) if the product has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/products")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) throws URISyntaxException {
        log.debug("REST request to save Product : {}", productDTO);
        if (productDTO.getId() != null) {
            throw new BadRequestAlertException("A new product cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductDTO result = productService.save(productDTO);
        return ResponseEntity.created(new URI("/api/products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /products : Updates an existing product.
     *
     * @param productDTO the productDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productDTO,
     * or with status 400 (Bad Request) if the productDTO is not valid,
     * or with status 500 (Internal Server Error) if the productDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/products")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDTO) throws URISyntaxException {
        log.debug("REST request to update Product : {}", productDTO);
        if (productDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProductDTO result = productService.save(productDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, productDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /products : get all the products.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of products in body
     */
    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getAllProducts(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Products");
        Page<ProductDTO> page;
        if (eagerload) {
            page = productService.findAllWithEagerRelationships(pageable);
        } else {
            page = productService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/products?eagerload=%b", eagerload));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /products/:id : get the "id" product.
     *
     * @param id the id of the productDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productDTO, or with status 404 (Not Found)
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        log.debug("REST request to get Product : {}", id);
        Optional<ProductDTO> productDTO = productService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productDTO);
    }

    /**
     * DELETE  /products/:id : delete the "id" product.
     *
     * @param id the id of the productDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.debug("REST request to delete Product : {}", id);
        productService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/products?query=:query : search for the product corresponding
     * to the query.
     *
     * @param query the query of the product search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/products")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Products for query {}", query);
        Page<ProductDTO> page = productService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/products");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
    @PostMapping("/products/toDto")
    public ResponseEntity<List<ProductDTO>> listToDto(@RequestBody List<Product> products) {
    	 log.debug("REST request to convert to DTO");
    	List<ProductDTO> dtos = new ArrayList<>();
    	products.forEach(a -> {dtos.add(productMapper.toDto(a));});
    	return ResponseEntity.ok().body(dtos);
    }
    
    
    /**
     * GET  /pdf/ProdutsReport : get the pdf of all the products.
     *  
     * @return the byte[]
     * @return the ResponseEntity with status 200 (OK) and the pdf of products in body
     */
    
	 @GetMapping("/pdf/produtsReport") 
	 public ResponseEntity<byte[]>  getPdfAllProdutsWithPrice() {
	 
	  log.debug("REST request to get a pdf of products");
	 
	  byte[] pdfContents = null;
	 
	  try
      {
		pdfContents=productService.getPdfAllProdutsWithPrice();
      }catch (JRException e) {
           e.printStackTrace();
       }
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/pdf"));
        String fileName ="productsReport.pdf";
		headers.add("content-disposition", "attachment; filename=" + fileName);
		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(
		            pdfContents, headers, HttpStatus.OK);	        
       return response;
	 }
} 
	 
