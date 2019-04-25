package com.diviso.graeshoppe.repository.search;

import com.diviso.graeshoppe.domain.Stock;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Stock entity.
 */
public interface StockSearchRepository extends ElasticsearchRepository<Stock, Long> {
}
