package com.diviso.graeshoppe.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.diviso.graeshoppe.domain.Product;
import com.diviso.graeshoppe.domain.StockCurrent;
import com.diviso.graeshoppe.repository.ProductRepository;
import com.diviso.graeshoppe.repository.StockCurrentRepository;
import com.diviso.graeshoppe.repository.search.ProductSearchRepository;
import com.diviso.graeshoppe.repository.search.StockCurrentSearchRepository;
import com.diviso.graeshoppe.security.SecurityUtils;
import com.diviso.graeshoppe.service.ProductService;
import com.diviso.graeshoppe.service.dto.ProductDTO;
import com.diviso.graeshoppe.service.mapper.ProductMapper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 * Service Implementation for managing Product.
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final ProductSearchRepository productSearchRepository;
    
    @Autowired
    StockCurrentSearchRepository stockCurrentSearchRepository;
    
    @Autowired
    StockCurrentRepository stockCurrentRepository;  
    
    @Autowired
	DataSource dataSource;
   
	/*
	 * @Autowired Connection connection;
	 */

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, ProductSearchRepository productSearchRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productSearchRepository = productSearchRepository;
    }

    /**
     * Save a product.
     *
     * @param productDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ProductDTO save(ProductDTO productDTO) {
        log.debug("Request to save Product : {}", productDTO);
        Product product = productMapper.toEntity(productDTO);
        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        product.setUserId(currentUserLogin.get());
        product = productRepository.save(product);
        ProductDTO result = productMapper.toDto(product);
        productSearchRepository.save(product);
        return result;
    }

    /**
     * Get all the products.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Products");
        return productRepository.findAll(pageable)
            .map(productMapper::toDto);
    }

    /**
     * Get all the Product with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<ProductDTO> findAllWithEagerRelationships(Pageable pageable) {
        return productRepository.findAllWithEagerRelationships(pageable).map(productMapper::toDto);
    }
    

    /**
     * Get one product by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDTO> findOne(Long id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findOneWithEagerRelationships(id)
            .map(productMapper::toDto);
    }

    /**
     * Delete the product by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Product : {}", id);      

        Optional<StockCurrent> stockcurrent =stockCurrentRepository.findByProductId(id);
        stockcurrent.ifPresent(stockcurrent1 -> {
        	 stockCurrentRepository.delete(stockcurrent1);
        	 stockCurrentSearchRepository.delete(stockcurrent1);
        });
        productRepository.deleteById(id);
        productSearchRepository.deleteById(id);
    }

    /**
     * Search for the product corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Products for query {}", query);
        return productSearchRepository.search(queryStringQuery(query), pageable)
            .map(productMapper::toDto);
    }
    
    /**
     * Get produtsReport.
     *			     
     * @return the byte[]
	 * @throws JRException 
     */
    @Override   
    public byte[] getPdfAllProdutsWithPrice() throws JRException {
    	
    	   log.debug("Request to pdf of all products");
		
		   JasperReport jr = JasperCompileManager.compileReport("productsWithprice.jrxml");
			
	       //Preparing parameters
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("product", jr);
						
			Connection conn=null;
			
			try {
				conn = dataSource.getConnection();
				
				//System.out.println(conn.getClientInfo()+"-----------------------"+conn.getMetaData().getURL()+"_________________________________");
							 			
			   } catch (SQLException e) {				
				  e.printStackTrace();
			
			   }
			JasperPrint jp=JasperFillManager.fillReport(jr, parameters, conn);
			
			return JasperExportManager.exportReportToPdf(jp);
			
			
	        } 	 
    
        }

    
    

    
    
    
	 

